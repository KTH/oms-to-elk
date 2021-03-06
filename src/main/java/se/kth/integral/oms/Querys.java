/**
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package se.kth.integral.oms;

import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceFuture;
import com.microsoft.rest.ServiceResponse;
import java.io.IOException;
import org.joda.time.Period;
import rx.Observable;
import se.kth.integral.oms.models.ErrorResponseException;
import se.kth.integral.oms.models.QueryBody;
import se.kth.integral.oms.models.QueryResults;

/**
 * An instance of this class provides access to all the operations defined
 * in Querys.
 */
public interface Querys {
    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data.
     *
     * @param query The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws ErrorResponseException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the QueryResults object if successful.
     */
    QueryResults get(String query);

    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data.
     *
     * @param query The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<QueryResults> getAsync(String query, final ServiceCallback<QueryResults> serviceCallback);

    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data.
     *
     * @param query The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the QueryResults object
     */
    Observable<QueryResults> getAsync(String query);

    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data.
     *
     * @param query The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the QueryResults object
     */
    Observable<ServiceResponse<QueryResults>> getWithServiceResponseAsync(String query);
    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data.
     *
     * @param query The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @param timespan Optional. The timespan over which to query data. This is an ISO8601 time period value.  This timespan is applied in addition to any that are specified in the query expression.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws ErrorResponseException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the QueryResults object if successful.
     */
    QueryResults get(String query, Period timespan);

    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data.
     *
     * @param query The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @param timespan Optional. The timespan over which to query data. This is an ISO8601 time period value.  This timespan is applied in addition to any that are specified in the query expression.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<QueryResults> getAsync(String query, Period timespan, final ServiceCallback<QueryResults> serviceCallback);

    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data.
     *
     * @param query The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @param timespan Optional. The timespan over which to query data. This is an ISO8601 time period value.  This timespan is applied in addition to any that are specified in the query expression.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the QueryResults object
     */
    Observable<QueryResults> getAsync(String query, Period timespan);

    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data.
     *
     * @param query The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @param timespan Optional. The timespan over which to query data. This is an ISO8601 time period value.  This timespan is applied in addition to any that are specified in the query expression.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the QueryResults object
     */
    Observable<ServiceResponse<QueryResults>> getWithServiceResponseAsync(String query, Period timespan);

    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data. [Here](/documentation/2-Using-the-API/Query) is an example for using POST with an Analytics query.
     *
     * @param body The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws ErrorResponseException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the QueryResults object if successful.
     */
    QueryResults post(QueryBody body);

    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data. [Here](/documentation/2-Using-the-API/Query) is an example for using POST with an Analytics query.
     *
     * @param body The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<QueryResults> postAsync(QueryBody body, final ServiceCallback<QueryResults> serviceCallback);

    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data. [Here](/documentation/2-Using-the-API/Query) is an example for using POST with an Analytics query.
     *
     * @param body The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the QueryResults object
     */
    Observable<QueryResults> postAsync(QueryBody body);

    /**
     * Execute an Analytics query.
     * Executes an Analytics query for data. [Here](/documentation/2-Using-the-API/Query) is an example for using POST with an Analytics query.
     *
     * @param body The Analytics query. Learn more about the [Analytics query syntax](https://azure.microsoft.com/documentation/articles/app-insights-analytics-reference/)
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the QueryResults object
     */
    Observable<ServiceResponse<QueryResults>> postWithServiceResponseAsync(QueryBody body);

}
