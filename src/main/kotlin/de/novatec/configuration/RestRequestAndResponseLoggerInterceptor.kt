package de.novatec.configuration

import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.Provider

@Provider
class RestRequestAndResponseLoggerInterceptor(
    @ConfigProperty(name = "quarkus.http.header.include-log") private val includeHttpHeaders: List<String>
) : ContainerRequestFilter, ContainerResponseFilter {

    private val log: Logger = LoggerFactory.getLogger(RestRequestAndResponseLoggerInterceptor::class.java)

    private companion object {
        const val REQUEST_PATTERN: String = "Request => {} {} with headers {}"
        const val RESPONSE_PATTERN: String = "Response <= {} with headers {}"
    }

    /**
     * Logs every incoming Request. If the request does not contain the trackingId header it will throw
     * an exception. Pattern is [REQUEST_PATTERN]
     */
    override fun filter(requestContext: ContainerRequestContext) {
        val headers = requestContext.headers.extractHeaderForLogging()

        if (!isTrackingIdPresent(headers)) {
            throw LoggingException("[x-tracking-id] header is not set")
        }

        val method = requestContext.method
        val path = requestContext.uriInfo.path

        log.info(REQUEST_PATTERN, method, path, headers)
    }

    /**
     * Logs every finished Request. Pattern is []
     */
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {
        val status = responseContext.status
        val headers = responseContext.headers.extractHeaderForLogging()

        log.info(RESPONSE_PATTERN, status, headers)
    }

    private fun isTrackingIdPresent(headers: Map<String, String>): Boolean = headers["x-tracking-id"] != null

    private fun <T> MultivaluedMap<String, T>.extractHeaderForLogging(): Map<String, T> {
        return this
            .filter { (header, _) -> header in includeHttpHeaders }
            .mapValues { (_, values) -> values.first() }
    }

    data class LoggingException(override val message: String) : RuntimeException(message)
}
