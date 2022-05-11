package de.novatec.adapter

import de.novatec.configuration.async
import de.novatec.domain.AddItemsToShoppingList
import de.novatec.domain.item.ItemService
import de.novatec.domain.shoppinglist.ShoppingListService
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Tuple
import io.vertx.pgclient.PgException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.RequestScoped
import javax.ws.rs.NotFoundException
import kotlinx.coroutines.async as coAsync

@RequestScoped
class AddItemsToShoppingListPostgresAction(
    private val itemService: ItemService,
    private val shoppingListService: ShoppingListService,
    @Suppress("CdiInjectionPointsInspection") private val pgPool: PgPool,
) : AddItemsToShoppingList {

    private val log: Logger = LoggerFactory.getLogger(AddItemsToShoppingListPostgresAction::class.java)
    private val coScope = CoroutineScope(Dispatchers.IO)

    override suspend fun invoke(shoppingListId: Long, items: List<String>) {
        shoppingListService
            .async { findById(shoppingListId) }
            ?.also { log.info("ShoppingList with id {} does exist", it.id) }
            ?: throw NotFoundException("ShoppingList with id [$shoppingListId] does not exist")

        val foundItems = itemService.async { findByItems(items) }
        val missingItems = items - foundItems.map { it.item }.toSet()

        if (missingItems.isNotEmpty()) {
            log.info("Following items are not persisted in the database: {}", missingItems)
            throw NotFoundException("While adding to ShoppingList following items are missing: $missingItems")
        }

        // Question: How can I persist a one-to-many relation in a cleaner way?
        foundItems
            .map {
                coScope.coAsync {
                    log.info("Handling ${it.item}")
                    pgPool
                        .preparedQuery("insert into shopping_list_items (shopping_list_id, item_id) values ($1, $2)")
                        .execute(Tuple.of(shoppingListId, it.id))
                        .onItemOrFailure().transform { _, u ->
                            when (u) {
                                is PgException -> {
                                    log.warn("Item {} was already linked with shoppingList: {}", it.item, u.message)
                                }
                            }
                        }.awaitSuspending()
                }
            }.awaitAll()
    }
}
