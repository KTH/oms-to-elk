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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.rest.credentials.ServiceClientCredentials;

import se.kth.infosys.lumberjack.Event;
import se.kth.infosys.lumberjack.ProtocolAdapter;
import se.kth.infosys.lumberjack.protocol.LumberjackClient;
import se.kth.infosys.lumberjack.util.AdapterException;
import se.kth.integral.oms.AzureLogAnalytics;
import se.kth.integral.oms.implementation.AzureLogAnalyticsImpl;
import se.kth.integral.oms.models.SavedSearch;
import se.kth.integral.oms.models.SavedSearchesListResult;

public class OMS {    
    public static void main(String[] args) throws IOException, AdapterException  {
        final Properties properties = new Properties();
        final InputStream stream = 
                OMS.class.getClassLoader().getResourceAsStream("oms-to-elk.properties");
        properties.load(stream);
        stream.close();

        ServiceClientCredentials credentials = new ApplicationTokenCredentials(
                properties.getProperty("azure.clientId"),
                properties.getProperty("azure.tenantId"),
                properties.getProperty("azure.clientKey"),
                AzureEnvironment.AZURE);
        AzureLogAnalytics ala = new AzureLogAnalyticsImpl(credentials)
                .withSubscriptionId(properties.getProperty("azure.subscription"));
        //Workspaces workspaces = ala.workspaces();
        SavedSearchesListResult savedSearches = ala.savedSearches().listByWorkspace(
                properties.getProperty("azure.resource_group"),
                properties.getProperty("azure.oms_workspace"));
        for (SavedSearch savedSearch : savedSearches.value()) {
            System.out.println(savedSearch.query());
        }

        ProtocolAdapter logstash = new LumberjackClient(
                properties.getProperty("logstash.keystore"),
                properties.getProperty("logstash.server"),
                Integer.parseInt(properties.getProperty("logstash.port")),
                10000);

        Event event = new Event();
        event.addField("line", "{'v':0, 'msg':'test'}");
        List<Event> events = new ArrayList<Event>();
        events.add(event);
        logstash.sendEvents(events);
    }
}
