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

import retrofit2.Retrofit;
import se.kth.infosys.lumberjack.Event;
import se.kth.infosys.lumberjack.ProtocolAdapter;
import se.kth.infosys.lumberjack.protocol.LumberjackClient;
import se.kth.infosys.lumberjack.util.AdapterException;
import se.kth.integral.oms.AzureLogAnalytics;
import se.kth.integral.oms.SavedSearches;
import se.kth.integral.oms.implementation.AzureLogAnalyticsImpl;
import se.kth.integral.oms.implementation.SavedSearchesImpl;
import se.kth.integral.oms.models.SavedSearch;
import se.kth.integral.oms.models.SavedSearchesListResult;
import se.kth.integral.oms.models.SearchResultsResponse;

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

        String savedQueryString = properties.getProperty("oms-to-elk.saved_query").trim();
        String queryName = "";
        String queryCategory = "";
        if (savedQueryString.contains(":")) {
            String[] savedQueryArr = savedQueryString.split(":");
            queryCategory = savedQueryArr[0];
            queryName = savedQueryArr[1];
        } else {
            queryName = savedQueryString;
        }

        SavedSearch savedSearch = null;
        SavedSearchesListResult searches = ala.savedSearches().listByWorkspace(
                properties.getProperty("azure.resource_group"),
                properties.getProperty("azure.oms_workspace"));
        for (SavedSearch search : searches.value()) {
            if (queryCategory.equals(search.category().trim()) &&
                    queryName.equals(search.displayName().trim())) {
                savedSearch = search;
            }
        }
        if (savedSearch == null) {
            System.out.println("query: "+ queryCategory + ":" + queryName + " not found" );
        }

        SearchResultsResponse searchResults = ala.savedSearches().getResults(
                properties.getProperty("azure.resource_group"),
                properties.getProperty("azure.oms_workspace"),
                savedSearch.category() + "|" + savedSearch.displayName());
        System.out.println("Got hits: " + searchResults.value().size());
        for (Object res : searchResults.value()) {
            System.out.println(res);
        }
/*
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
*/
    }
}
