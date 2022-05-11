package de.novatec.adapter

import de.novatec.domain.item.ItemEntity
import de.novatec.domain.item.ItemService
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ItemPostgresRepository : ItemService {

    private object Repository : PanacheRepository<ItemEntity>

    override fun getAll(): Uni<List<ItemEntity>> {
        return Repository
            .listAll(Sort.by("id"))
    }

    override fun findById(id: Long): Uni<ItemEntity?> {
        return Repository
            .findById(id)
    }

    override fun findByItems(items: List<String>): Uni<List<ItemEntity>> {
        return Repository
            .find("item in (?1)", items)
            .list()
    }

    override fun create(itemEntity: ItemEntity): Uni<ItemEntity> {
        return Repository
            .persistAndFlush(itemEntity)
    }

    override fun updateItemById(id: Long, item: String): Uni<Int> {
        return Repository
            .update("item = ?1 where id = ?2", item, id)
    }

    override fun deleteById(id: Long): Uni<Boolean> {
        return Repository
            .deleteById(id)
    }
}
