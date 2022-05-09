package de.novatec.port

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
    suspend fun getAllItems(): RestResponse<ItemsResponse> {
        val items = itemService
            .getAll()
            .let { ItemsResponse.from(it) }

        return RestResponse.ok(items)
    }

    @GET
    @Path("/{item}")
    suspend fun getByItem(
        @RestPath("item") item: String
    ): RestResponse<ItemEntity> {
        val response = itemService
            .findItem(item)
            ?: throw NotFoundException("Item [$item] does not exist")

        return RestResponse.ok(response)
    }

    @POST
    @Path("/{item}")
    @Transactional
    suspend fun persistNewItem(
        @RestPath("item") item: String
    ): RestResponse<ItemEntity> {
        itemService.persist(ItemEntity(item))

        return RestResponse.created(URI.create("/items/$item"))
    }

    @PATCH
    @Path("/{item}")
    @Transactional
    suspend fun updateItem(
        @RestPath("item") item: String,
        itemEntity: ItemEntity
    ): RestResponse<ItemEntity> {
        itemService.updateItem(item, itemEntity.item)

        return RestResponse.ok()
    }

    @DELETE
    @Path("/{item}")
    @Transactional
    suspend fun deleteItem(
        @RestPath("item") item: String,
    ): RestResponse<ItemEntity> {
        itemService.delete(item)

        return RestResponse.ok()
    }

    data class ItemsResponse(
        val items: List<String>
    ) {
        companion object {
            fun from(itemEntities: List<ItemEntity>) = ItemsResponse(itemEntities.map { it.item })
        }
    }
}
