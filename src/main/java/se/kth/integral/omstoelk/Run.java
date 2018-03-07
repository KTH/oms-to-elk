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

import com.google.gson.JsonObject;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.rest.RestClient;
import com.microsoft.rest.credentials.ServiceClientCredentials;

import se.kth.infosys.lumberjack.util.AdapterException;
import se.kth.integral.oms.AzureLogAnalyticspublicAPI;
import se.kth.integral.oms.implementation.AzureLogAnalyticspublicAPIImpl;

public class Run {
    public static void main(String[] args) throws IOException, AdapterException, InterruptedException  {
        Queue<JsonObject> queue = new ConcurrentLinkedQueue<JsonObject>();

        ClassLoader loader = Run.class.getClassLoader();

        InputStream stream =  loader.getResourceAsStream("oms-to-elk.properties");
        Properties omsToElkProperties = new Properties();
        omsToElkProperties.load(stream);
        stream.close();

        stream = loader.getResourceAsStream("azure.properties");
        Properties azureProperties = new Properties();
        azureProperties.load(stream);
        stream.close();

        stream = loader.getResourceAsStream("logstash.properties");
        Properties logstashProperties = new Properties();
        logstashProperties.load(stream);
        stream.close();

        Statistics statistics = new Statistics();
        StatisticsLogger sl = new StatisticsLogger(statistics);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(sl, 1, 1, TimeUnit.MINUTES);

        ServiceClientCredentials credentials = new ApplicationTokenCredentials(
                azureProperties.getProperty("clientId").trim(),
                azureProperties.getProperty("tenantId").trim(),
                azureProperties.getProperty("clientKey").trim(),
                AzureEnvironment.AZURE);

        RestClient restClient = new RestClient.Builder()
                .withCredentials(credentials).build();

        AzureLogAnalyticspublicAPI ala =
                new AzureLogAnalyticspublicAPIImpl(restClient).withWorkspaceId(azureProperties.getProperty("oms_workspace_id").trim());

        LogRetriever lr = new LogRetriever(
                ala.querys(),
                azureProperties.getProperty("query").trim(),
                queue);
        
        LogForwarder lf = new LogForwarder(
                statistics,
                queue,
                logstashProperties.getProperty("server").trim(), 
                Integer.valueOf(logstashProperties.getProperty("port").trim()), 
                logstashProperties.getProperty("keystore").trim());

        new Thread(lr).start();
        new Thread(lf).start();
    }
}
