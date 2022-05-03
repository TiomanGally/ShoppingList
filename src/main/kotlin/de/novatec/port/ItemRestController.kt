package de.novatec.port

import de.novatec.domain.ItemEntity
import de.novatec.domain.ItemService
import org.hibernate.HibernateException
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.server.ServerExceptionMapper
import java.net.URI
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path

@Path("/items")
@ApplicationScoped
class ItemRestController(
    private val itemService: ItemService
) {

    @GET
    @Path("/")
    suspend fun getAllItems(): RestResponse<ItemsResponse> {
        val response = itemService
            .getAll()
            .let { ItemsResponse.from(it) }

        return RestResponse.ok(response)
    }

    @POST
    @Path("/{item}")
    suspend fun persistNewItem(
        @RestPath("item") item: String
    ): RestResponse<ItemEntity> {
        itemService.persist(ItemEntity(item))

        return RestResponse.created(URI.create("/items"))
    }

    @Suppress("unused")
    @ServerExceptionMapper
    fun mapDuplicateItem(exception: HibernateException): RestResponse<ErrorMessage> {
        return RestResponse.ResponseBuilder
            .create<ErrorMessage>(RestResponse.Status.BAD_REQUEST.statusCode)
            .entity(ErrorMessage(exception.message!!))
            .build()
    }

    data class ErrorMessage(val message: String)

    data class ItemsResponse(
        val items: List<String>
    ) {
        companion object {
            fun from(itemEntities: List<ItemEntity>) = ItemsResponse(itemEntities.map { it.item })
        }
    }
}
