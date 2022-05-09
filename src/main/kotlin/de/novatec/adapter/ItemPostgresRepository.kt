package de.novatec.adapter

import de.novatec.domain.ItemEntity
import de.novatec.domain.ItemService
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ItemPostgresRepository : ItemService {

    private val log: Logger = LoggerFactory.getLogger(ItemPostgresRepository::class.java)

    override suspend fun persist(itemEntity: ItemEntity): ItemEntity {
        return Repository.persistAndFlush(itemEntity)
            .awaitSuspending()
    }

    override suspend fun getAll(): List<ItemEntity> {
        return Repository
            .listAll(Sort.by("item"))
            .awaitSuspending()
    }

    override suspend fun findItem(item: String): ItemEntity? {
        return Repository.find("item", item)
            .firstResult<ItemEntity>()
            .awaitSuspending()
    }

    override suspend fun updateItem(oldItem: String, newItem: String) {
        val currentItemEntity: ItemEntity = Repository.find("item", oldItem)
            .firstResult<ItemEntity>()
            .awaitSuspending()
            ?: return

        Repository
            .delete(currentItemEntity)
            .call { _ -> Repository.persist(ItemEntity(newItem)) }
            .call { _ -> Repository.flush() }
            .awaitSuspending()

    }

    override suspend fun delete(item: String) {
        log.info("Searching for {}...", item)
        val itemEntity: ItemEntity? = Repository.find("item", item)
            .firstResult<ItemEntity>()
            .awaitSuspending()

        log.info("Found {}", itemEntity?.item)

        if (itemEntity != null) {
            log.info("Deleting {}", itemEntity.item)
            Repository.delete(itemEntity).awaitSuspending()
            Repository.flush().awaitSuspending()
        }
    }

    private object Repository : PanacheRepository<ItemEntity>
}
