/**
 * Code generated by Microsoft (R) AutoRest Code Generator 0.17.0.0
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package se.kth.integral.azure.opinsights;

import com.microsoft.azure.ListOperationCallback;
import com.microsoft.azure.Page;
import com.microsoft.azure.PagedList;
import com.microsoft.rest.ServiceCall;
import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceResponse;
import java.util.List;
import rx.Observable;
import se.kth.integral.azure.opinsights.models.StorageInsight;

/**
 * An instance of this class provides access to all the operations defined
 * in StorageInsights.
 */
public interface StorageInsights {
    /**
     * Create or update a storage insight.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that will contain the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     * @param parameters The parameters required to create or update a storage insight.
     * @return the StorageInsight object if successful.
     */
    StorageInsight createOrUpdate(String resourceGroupName, String workspaceName, String storageInsightName, StorageInsight parameters);

    /**
     * Create or update a storage insight.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that will contain the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     * @param parameters The parameters required to create or update a storage insight.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @return the {@link ServiceCall} object
     */
    ServiceCall<StorageInsight> createOrUpdateAsync(String resourceGroupName, String workspaceName, String storageInsightName, StorageInsight parameters, final ServiceCallback<StorageInsight> serviceCallback);

    /**
     * Create or update a storage insight.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that will contain the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     * @param parameters The parameters required to create or update a storage insight.
     * @return the observable to the StorageInsight object
     */
    Observable<StorageInsight> createOrUpdateAsync(String resourceGroupName, String workspaceName, String storageInsightName, StorageInsight parameters);

    /**
     * Create or update a storage insight.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that will contain the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     * @param parameters The parameters required to create or update a storage insight.
     * @return the observable to the StorageInsight object
     */
    Observable<ServiceResponse<StorageInsight>> createOrUpdateWithServiceResponseAsync(String resourceGroupName, String workspaceName, String storageInsightName, StorageInsight parameters);

    /**
     * Gets a storage insight instance.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that contains the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     * @return the StorageInsight object if successful.
     */
    StorageInsight get(String resourceGroupName, String workspaceName, String storageInsightName);

    /**
     * Gets a storage insight instance.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that contains the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @return the {@link ServiceCall} object
     */
    ServiceCall<StorageInsight> getAsync(String resourceGroupName, String workspaceName, String storageInsightName, final ServiceCallback<StorageInsight> serviceCallback);

    /**
     * Gets a storage insight instance.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that contains the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     * @return the observable to the StorageInsight object
     */
    Observable<StorageInsight> getAsync(String resourceGroupName, String workspaceName, String storageInsightName);

    /**
     * Gets a storage insight instance.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that contains the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     * @return the observable to the StorageInsight object
     */
    Observable<ServiceResponse<StorageInsight>> getWithServiceResponseAsync(String resourceGroupName, String workspaceName, String storageInsightName);

    /**
     * Deletes a storageInsightsConfigs resource.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that contains the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     */
    void delete(String resourceGroupName, String workspaceName, String storageInsightName);

    /**
     * Deletes a storageInsightsConfigs resource.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that contains the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @return the {@link ServiceCall} object
     */
    ServiceCall<Void> deleteAsync(String resourceGroupName, String workspaceName, String storageInsightName, final ServiceCallback<Void> serviceCallback);

    /**
     * Deletes a storageInsightsConfigs resource.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that contains the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     * @return the {@link ServiceResponse} object if successful.
     */
    Observable<Void> deleteAsync(String resourceGroupName, String workspaceName, String storageInsightName);

    /**
     * Deletes a storageInsightsConfigs resource.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that contains the storageInsightsConfigs resource
     * @param storageInsightName Name of the storageInsightsConfigs resource
     * @return the {@link ServiceResponse} object if successful.
     */
    Observable<ServiceResponse<Void>> deleteWithServiceResponseAsync(String resourceGroupName, String workspaceName, String storageInsightName);

    /**
     * Lists the storage insight instances within a workspace.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that will contain the storageInsightsConfigs resource
     * @return the PagedList&lt;StorageInsight&gt; object if successful.
     */
    PagedList<StorageInsight> listByWorkspace(final String resourceGroupName, final String workspaceName);

    /**
     * Lists the storage insight instances within a workspace.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that will contain the storageInsightsConfigs resource
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @return the {@link ServiceCall} object
     */
    ServiceCall<List<StorageInsight>> listByWorkspaceAsync(final String resourceGroupName, final String workspaceName, final ListOperationCallback<StorageInsight> serviceCallback);

    /**
     * Lists the storage insight instances within a workspace.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that will contain the storageInsightsConfigs resource
     * @return the observable to the PagedList&lt;StorageInsight&gt; object
     */
    Observable<Page<StorageInsight>> listByWorkspaceAsync(final String resourceGroupName, final String workspaceName);

    /**
     * Lists the storage insight instances within a workspace.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param workspaceName Log Analytics Workspace name that will contain the storageInsightsConfigs resource
     * @return the observable to the PagedList&lt;StorageInsight&gt; object
     */
    Observable<ServiceResponse<Page<StorageInsight>>> listByWorkspaceWithServiceResponseAsync(final String resourceGroupName, final String workspaceName);

    /**
     * Lists the storage insight instances within a workspace.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @return the PagedList&lt;StorageInsight&gt; object if successful.
     */
    PagedList<StorageInsight> listByWorkspaceNext(final String nextPageLink);

    /**
     * Lists the storage insight instances within a workspace.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceCall the ServiceCall object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @return the {@link ServiceCall} object
     */
    ServiceCall<List<StorageInsight>> listByWorkspaceNextAsync(final String nextPageLink, final ServiceCall<List<StorageInsight>> serviceCall, final ListOperationCallback<StorageInsight> serviceCallback);

    /**
     * Lists the storage insight instances within a workspace.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @return the observable to the PagedList&lt;StorageInsight&gt; object
     */
    Observable<Page<StorageInsight>> listByWorkspaceNextAsync(final String nextPageLink);

    /**
     * Lists the storage insight instances within a workspace.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @return the observable to the PagedList&lt;StorageInsight&gt; object
     */
    Observable<ServiceResponse<Page<StorageInsight>>> listByWorkspaceNextWithServiceResponseAsync(final String nextPageLink);

}
