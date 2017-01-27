package se.kth.integral.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.rest.credentials.ServiceClientCredentials;

import se.kth.integral.oms.AzureLogAnalytics;
import se.kth.integral.oms.implementation.AzureLogAnalyticsImpl;
import se.kth.integral.oms.models.SavedSearch;
import se.kth.integral.oms.models.SavedSearchesListResult;

public class OMS {    
    private static final String WORKSPACE = "kth-integral";
    private static final String RESOURCE_GROUP = "UF-ITA-INTEGRATION";

    public static void main(String[] args) throws FileNotFoundException, IOException {
	Properties properties = new Properties();
	properties.load(new FileInputStream(new File("azure.properties")));
	
	ServiceClientCredentials credentials = new ApplicationTokenCredentials(
		properties.getProperty("azure.clientId"),
		properties.getProperty("azure.tenantId"),
		properties.getProperty("azure.clientKey"),
		AzureEnvironment.AZURE);
	AzureLogAnalytics ala = new AzureLogAnalyticsImpl(credentials)
		.withSubscriptionId(properties.getProperty("azure.subscription"));
	//Workspaces workspaces = ala.workspaces();
	SavedSearchesListResult savedSearches = ala.savedSearches().listByWorkspace(RESOURCE_GROUP, WORKSPACE);
	for (SavedSearch savedSearch : savedSearches.value()) {
	    System.out.println(savedSearch.query());
	}
    }
}
