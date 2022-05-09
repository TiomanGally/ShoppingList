package de.novatec.port

import org.hibernate.HibernateException
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

@Suppress("unused")
class RestExceptionHandler {

    @Suppress("unused")
    @ServerExceptionMapper
    fun mapDuplicateItem(exception: HibernateException): RestResponse<ErrorMessage> {
        return RestResponse.ResponseBuilder
            .create<ErrorMessage>(RestResponse.Status.BAD_REQUEST.statusCode)
            .entity(ErrorMessage(exception.message))
            .build()
    }

    data class ErrorMessage(val message: String?)
}
