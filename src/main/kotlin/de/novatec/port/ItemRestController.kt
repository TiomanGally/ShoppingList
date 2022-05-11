package de.novatec.port

import de.novatec.configuration.NoArgConstructor
import de.novatec.domain.item.ItemEntity
import de.novatec.domain.item.ItemService
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestResponse
import java.net.URI
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.*

@Path("/items")
@ApplicationScoped
class ItemRestController(
    private val itemService: ItemService
) {

    @GET
    @Path("/")
    fun getAllItems(): Uni<RestResponse<List<ItemEntity>>> {

        return itemService
            .getAll()
            .onItem().transform { RestResponse.ok(it) }
    }

    @GET
    @Path("/{id}")
    fun getByItem(
        @RestPath("id") id: Long
    ): Uni<RestResponse<ItemEntity>> {

        return itemService
            .findById(id)
            .onItem().transform {
                if (it == null)
                    RestResponse.notFound()
                else
                    RestResponse.ok(it)
            }
    }

    @POST
    @Path("/{item}")
    @ReactiveTransactional
    fun persistNewItem(
        @RestPath("item") item: String
    ): Uni<RestResponse<ItemEntity>> {
        val itemEntity = ItemEntity(item = item)

        return itemService
            .create(itemEntity)
            .onItem().transform { URI.create("/items/${it.id}") }
            .onItem().transform { RestResponse.created(it) }
    }

    @PATCH
    @Path("/{id}")
    @ReactiveTransactional
    fun updateItem(
        @RestPath("id") id: Long,
        updateItemRequest: UpdateItemRequest
    ): Uni<RestResponse<ItemEntity>> {

        return itemService
            .updateItemById(id, updateItemRequest.item)
            .onItem().transform {
                if (it == 0) {
                    RestResponse.notFound()
                } else {
                    RestResponse.ok()
                }
            }
    }

    @DELETE
    @Path("/{id}")
    @ReactiveTransactional
    fun deleteItem(
        @RestPath("id") id: Long,
    ): Uni<RestResponse<ItemEntity>> {

        return itemService
            .deleteById(id)
            .onItem().transform {
                if (it) {
                    RestResponse.noContent()
                } else {
                    RestResponse.notFound()
                }
            }
    }

    @NoArgConstructor
    data class UpdateItemRequest(val item: String)
}
