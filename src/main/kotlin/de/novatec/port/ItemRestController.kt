package de.novatec.port

import de.novatec.configuration.NoArgConstructor
import de.novatec.domain.ItemEntity
import de.novatec.domain.ItemService
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestResponse
import java.net.URI
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional
import javax.ws.rs.*

@Path("/items")
@ApplicationScoped
class ItemRestController(
    private val itemService: ItemService
) {

    @GET
    @Path("/")
    suspend fun getAllItems(): RestResponse<List<ItemEntity>> {
        val items = itemService.getAll()

        return RestResponse.ok(items)
    }

    @GET
    @Path("/{id}")
    suspend fun getByItem(
        @RestPath("id") id: Long
    ): RestResponse<ItemEntity> {
        val response = itemService
            .findById(id)
            ?: throw NotFoundException("Item [$id] does not exist")

        return RestResponse.ok(response)
    }

    @POST
    @Path("/{item}")
    @Transactional
    suspend fun persistNewItem(
        @RestPath("item") item: String
    ): RestResponse<ItemEntity> {
        val itemEntity = ItemEntity(
            item = item
        )
        val createdResource = itemService.create(itemEntity)

        return RestResponse.created(URI.create("/items/${createdResource.id}"))
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    suspend fun updateItem(
        @RestPath("id") id: Long,
        updateItemRequest: UpdateItemRequest
    ): RestResponse<ItemEntity> {
        println(updateItemRequest.item)
        itemService.updateItemById(id, updateItemRequest.item)

        return RestResponse.ok()
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    suspend fun deleteItem(
        @RestPath("id") id: Long,
    ): RestResponse<ItemEntity> {
        itemService.deleteById(id)

        return RestResponse.ok()
    }

    @NoArgConstructor
    data class UpdateItemRequest(val item: String)
}

