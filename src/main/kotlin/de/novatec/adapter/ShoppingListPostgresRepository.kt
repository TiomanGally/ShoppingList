package de.novatec.adapter

import de.novatec.domain.shoppinglist.ShoppingListEntity
import de.novatec.domain.shoppinglist.ShoppingListService
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.smallrye.mutiny.Uni
import org.hibernate.reactive.mutiny.Mutiny
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.NotFoundException


@ApplicationScoped
class ShoppingListPostgresRepository : ShoppingListService {

    private object Repository : PanacheRepository<ShoppingListEntity>

    override fun create(shoppingList: ShoppingListEntity): Uni<ShoppingListEntity> {
        return Repository
            .persistAndFlush(shoppingList)
    }

    override fun findById(id: Long, fetchItems: Boolean): Uni<ShoppingListEntity?> {
        return Repository
            .findById(id)
            .onItem().ifNull().failWith { NotFoundException("Shopping List with id [$id] does not exist") }
            .onItem().ifNotNull().call { it ->
                if (fetchItems) {
                    Mutiny.fetch(it.items)
                } else {
                    Uni.createFrom().item(it)
                }
            }
    }

    override fun deleteById(id: Long): Uni<Boolean> {
        return Repository
            .deleteById(id)
    }
}
