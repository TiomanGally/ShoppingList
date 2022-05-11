package de.novatec.port

import org.hibernate.HibernateException
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.server.ServerExceptionMapper
import javax.persistence.PersistenceException
import javax.ws.rs.NotFoundException

@Suppress("unused")
class RestExceptionHandler {

    @ServerExceptionMapper
    fun mapDuplicateItem(exception: HibernateException): RestResponse<ErrorMessage> {
        return RestResponse.ResponseBuilder
            .create<ErrorMessage>(RestResponse.Status.BAD_REQUEST.statusCode)
            .entity(ErrorMessage(exception.message))
            .build()
    }

    @ServerExceptionMapper
    fun mapGeneralError(throwable: Throwable): RestResponse<ErrorMessage> {
        return RestResponse.ResponseBuilder
            .create<ErrorMessage>(RestResponse.Status.INTERNAL_SERVER_ERROR.statusCode)
            .entity(ErrorMessage(throwable.message))
            .build()
    }

    @ServerExceptionMapper
    fun mapNotFoundException(notFoundException: NotFoundException): RestResponse<ErrorMessage> {
        return RestResponse.ResponseBuilder
            .create<ErrorMessage>(RestResponse.Status.NOT_FOUND.statusCode)
            .entity(ErrorMessage(notFoundException.message))
            .build()
    }

    @ServerExceptionMapper
    fun mapPgException(persistenceException: PersistenceException): RestResponse<ErrorMessage> {
        return RestResponse.ResponseBuilder
            .create<ErrorMessage>(RestResponse.Status.CONFLICT.statusCode)
            .entity(ErrorMessage(persistenceException.message))
            .build()
    }

    data class ErrorMessage(val message: String?)
}
