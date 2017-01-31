package se.kth.integral.omstoelk;

/*
 * Copyright (c) 2017 Kungliga Tekniska högskolan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.rest.credentials.ServiceClientCredentials;

import se.kth.infosys.lumberjack.util.AdapterException;
import se.kth.integral.azure.opinsights.AzureLogAnalytics;
import se.kth.integral.azure.opinsights.implementation.AzureLogAnalyticsImpl;

public class Run {
    public static void main(String[] args) throws IOException, AdapterException, InterruptedException  {
        final Queue<JsonNode> queue = new ConcurrentLinkedQueue<JsonNode>();

        final ClassLoader loader = Run.class.getClassLoader();

        InputStream stream =  loader.getResourceAsStream("oms-to-elk.properties");
        final Properties omsToElkProperties = new Properties();
        omsToElkProperties.load(stream);
        stream.close();

        stream = loader.getResourceAsStream("azure.properties");
        final Properties azureProperties = new Properties();
        azureProperties.load(stream);
        stream.close();

        stream = loader.getResourceAsStream("logstash.properties");
        final Properties logstashProperties = new Properties();
        logstashProperties.load(stream);
        stream.close();

        final Statistics statistics = new Statistics();
        StatisticsLogger sl = new StatisticsLogger(statistics);
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(sl, 1, 1, TimeUnit.MINUTES);

        ServiceClientCredentials credentials = new ApplicationTokenCredentials(
                azureProperties.getProperty("clientId").trim(),
                azureProperties.getProperty("tenantId").trim(),
                azureProperties.getProperty("clientKey").trim(),
                AzureEnvironment.AZURE);
        AzureLogAnalytics ala = new AzureLogAnalyticsImpl(credentials)
                .withSubscriptionId(azureProperties.getProperty("subscription").trim());

        QueryRetriever qr = new QueryRetriever(
                statistics,
                ala.savedSearches(), 
                azureProperties.getProperty("resource_group").trim(),
                azureProperties.getProperty("oms_workspace").trim(),
                omsToElkProperties.getProperty("saved_query").trim());

        LogRetriever lr = new LogRetriever(
                ala.workspaces(),
                azureProperties.getProperty("resource_group").trim(),
                azureProperties.getProperty("oms_workspace").trim(),
                qr,
                queue);
        
        LogForwarder lf = new LogForwarder(
                statistics,
                queue,
                logstashProperties.getProperty("server").trim(), 
                Integer.valueOf(logstashProperties.getProperty("port").trim()), 
                logstashProperties.getProperty("keystore").trim());

        new Thread(qr).start();
        new Thread(lr).start();
        new Thread(lf).start();
    }
}
