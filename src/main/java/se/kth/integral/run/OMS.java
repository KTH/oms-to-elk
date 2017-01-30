package se.kth.integral.run;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.rest.credentials.ServiceClientCredentials;

import se.kth.infosys.lumberjack.util.AdapterException;
import se.kth.integral.azure.opinsights.AzureLogAnalytics;
import se.kth.integral.azure.opinsights.implementation.AzureLogAnalyticsImpl;
import se.kth.integral.omstoelk.LogForwarder;
import se.kth.integral.omstoelk.LogRetriever;
import se.kth.integral.omstoelk.QueryRetriever;

public class OMS {
    public static void main(String[] args) throws IOException, AdapterException, InterruptedException  {
        final Queue<JsonNode> queue = new ConcurrentLinkedQueue<JsonNode>();

        final Properties properties = new Properties();
        final InputStream stream =  OMS.class.getClassLoader().getResourceAsStream("oms-to-elk.properties");
        properties.load(stream);
        stream.close();

        ServiceClientCredentials credentials = new ApplicationTokenCredentials(
                properties.getProperty("azure.clientId").trim(),
                properties.getProperty("azure.tenantId").trim(),
                properties.getProperty("azure.clientKey").trim(),
                AzureEnvironment.AZURE);
        AzureLogAnalytics ala = new AzureLogAnalyticsImpl(credentials)
                .withSubscriptionId(properties.getProperty("azure.subscription").trim());

        QueryRetriever qr = new QueryRetriever(
                ala.savedSearches(), 
                properties.getProperty("azure.resource_group").trim(),
                properties.getProperty("azure.oms_workspace").trim(),
                properties.getProperty("oms-to-elk.saved_query").trim());

        LogRetriever lr = new LogRetriever(
                ala.workspaces(),
                properties.getProperty("azure.resource_group").trim(),
                properties.getProperty("azure.oms_workspace").trim(),
                qr,
                queue);
        
        LogForwarder lf = new LogForwarder(
                queue,
                properties.getProperty("logstash.server").trim(), 
                Integer.valueOf(properties.getProperty("logstash.port").trim()), 
                properties.getProperty("logstash.keystore").trim());

        new Thread(qr).start();
        new Thread(lr).start();
        new Thread(lf).start();
    }
}
