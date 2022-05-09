package de.novatec.adapter

import de.novatec.domain.ItemEntity
import de.novatec.domain.ItemService
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ItemPostgresRepository : ItemService {

    private object Repository : PanacheRepository<ItemEntity>

    override suspend fun getAll(): List<ItemEntity> {
        return Repository
            .listAll(Sort.by("id"))
            .awaitSuspending()
    }

    override suspend fun findById(id: Long): ItemEntity? {
        return Repository
            .findById(id)
            .awaitSuspending()
    }

    override suspend fun create(itemEntity: ItemEntity): ItemEntity {
        return Repository
            .persistAndFlush(itemEntity)
            .awaitSuspending()
    }

    override suspend fun updateItemById(id: Long, item: String) {
        Repository
            .update("item = ?1 where id = ?2", item, id)
            .awaitSuspending()
    }

    override suspend fun deleteById(id: Long) {
        Repository.deleteById(id).awaitSuspending()
        Repository.flush().awaitSuspending()
    }
}
