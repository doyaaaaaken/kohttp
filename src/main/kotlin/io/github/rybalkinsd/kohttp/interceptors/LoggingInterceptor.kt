package io.github.rybalkinsd.kohttp.interceptors

import io.github.rybalkinsd.kohttp.interceptors.logging.HttpLoggingStrategy
import io.github.rybalkinsd.kohttp.interceptors.logging.LoggingStrategy
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Request Logging Interceptor
 *
 * Logs HTTP requests.
 *
 * @param strategy logging option (format type...etc).
 *   HttpLoggingStrategy: http request format strategy
 *   CurlLoggingStrategy: curl command format strategy
 * @param log function to consume log message
 *
 * Sample Output: [2019-01-28T04:17:42.885Z] GET 200 - 1743ms https://postman-echo.com/get
 *
 * @since 0.8.0
 * @author gokul
 */
class LoggingInterceptor(
        private val strategy: LoggingStrategy = HttpLoggingStrategy(),
        private val log: (String) -> Unit = ::println
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        return chain.proceed(request).also { response ->
            log("${request.method()} ${response.code()} - ${System.currentTimeMillis() - startTime}ms ${request.url()}")
            strategy.log(request, log)
        }
    }
}
