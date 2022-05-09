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

    @Suppress("unused")
    @ServerExceptionMapper
    fun mapGeneralError(throwable: Throwable): RestResponse<ErrorMessage> {
        return RestResponse.ResponseBuilder
            .create<ErrorMessage>(RestResponse.Status.INTERNAL_SERVER_ERROR.statusCode)
            .entity(ErrorMessage(throwable.message))
            .build()
    }

    data class ErrorMessage(val message: String?)
}
