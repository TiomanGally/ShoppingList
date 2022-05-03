package de.novatec.adapter

import de.novatec.domain.ItemEntity
import de.novatec.domain.ItemService
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ItemPostgresRepository : ItemService {

    override suspend fun persist(itemEntity: ItemEntity): ItemEntity {
        return Repository.persistAndFlush(itemEntity).awaitSuspending()
    }

    override suspend fun getAll(): List<ItemEntity> {
        return Repository.listAll(Sort.by("item")).awaitSuspending()
    }

    override suspend fun getByItem(item: String): ItemEntity {
        return Repository.find("", item).firstResult<ItemEntity>().awaitSuspending()
    }

    override suspend fun update(itemEntity: ItemEntity): Int {
        return Repository.update("", itemEntity).awaitSuspending()
    }

    override suspend fun delete(itemEntity: ItemEntity) {
        Repository.delete(itemEntity).awaitSuspending()
    }

    private object Repository : PanacheRepository<ItemEntity>
}
