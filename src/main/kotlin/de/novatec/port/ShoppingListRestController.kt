package de.novatec.port

import de.novatec.domain.AddItemsToShoppingList
import de.novatec.domain.shoppinglist.ShoppingListEntity
import de.novatec.domain.shoppinglist.ShoppingListService
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestResponse
import java.net.URI
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.PATCH
import javax.ws.rs.POST
import javax.ws.rs.Path

@Path("/shopping-list")
class ShoppingListRestController(
    private val shoppingListService: ShoppingListService,
    private val addItemsToShoppingList: AddItemsToShoppingList
) {

    @GET
    @Path("/{id}")
    fun findById(
        @RestPath("id") id: Long
    ): Uni<RestResponse<ShoppingListEntity>> {
        return shoppingListService
            .findById(id, true)
            .onItem().transform {
                if (it == null) {
                    RestResponse.notFound()
                } else {
                    RestResponse.ok(it)
                }
            }
    }

    @POST
    @ReactiveTransactional
    fun createShoppingList(): Uni<RestResponse<ShoppingListEntity>> {
        val shoppingList = ShoppingListEntity(emptyList())

        return shoppingListService
            .create(shoppingList)
            .onItem().transform { URI.create("/shopping-list/${it.id}") }
            .onItem().transform { RestResponse.created(it) }
    }

    @PATCH
    @Path("/{shoppingListId}")
    @Transactional
    suspend fun addItemsInShoppingList(
        @RestPath("shoppingListId") shoppingListId: Long,
        items: List<String>
    ): RestResponse<ShoppingListEntity> {
        addItemsToShoppingList(shoppingListId, items)

        return RestResponse.ok()
    }
}
