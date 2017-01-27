package se.kth.integral.run;

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
