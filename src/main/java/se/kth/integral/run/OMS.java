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
        final Properties properties = new Properties();
        final InputStream stream = 
                OMS.class.getClassLoader().getResourceAsStream("oms-to-elk.properties");

        final Queue<JsonNode> queue = new ConcurrentLinkedQueue<JsonNode>();

        properties.load(stream);
        stream.close();

        ServiceClientCredentials credentials = new ApplicationTokenCredentials(
                properties.getProperty("azure.clientId"),
                properties.getProperty("azure.tenantId"),
                properties.getProperty("azure.clientKey"),
                AzureEnvironment.AZURE);
        AzureLogAnalytics ala = new AzureLogAnalyticsImpl(credentials)
                .withSubscriptionId(properties.getProperty("azure.subscription"));

        String savedQueryString = properties.getProperty("oms-to-elk.saved_query").trim();
        if (savedQueryString.contains(":")) {
            savedQueryString = savedQueryString.replace(":", "|");
        }

        QueryRetriever qr = new QueryRetriever(
                ala.savedSearches(), 
                properties.getProperty("azure.resource_group"),
                properties.getProperty("azure.oms_workspace"),
                properties.getProperty("oms-to-elk.saved_query"));

        LogRetriever lr = new LogRetriever(
                ala.workspaces(),
                properties.getProperty("azure.resource_group"),
                properties.getProperty("azure.oms_workspace"),
                qr,
                queue);
        
        LogForwarder lf = new LogForwarder(
                queue,
                properties.getProperty("logstash.server"), 
                Integer.valueOf(properties.getProperty("logstash.port")), 
                properties.getProperty("logstash.keystore"));

        new Thread(qr).start();
        new Thread(lr).start();
        new Thread(lf).start();
    }
}
