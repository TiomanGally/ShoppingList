package de.novatec.adapter

import de.novatec.domain.recipe.Recipe
import de.novatec.domain.recipe.RecipeEntity
import de.novatec.domain.recipe.RecipeService
import io.quarkus.hibernate.reactive.panache.PanacheEntity_.id
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Tuple
import io.vertx.pgclient.PgException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import org.hibernate.reactive.mutiny.Mutiny
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.RequestScoped
import javax.ws.rs.NotFoundException
import kotlinx.coroutines.async as coAsync

@RequestScoped
class RecipePostgresRepository(
    @Suppress("CdiInjectionPointsInspection") private val pgPool: PgPool
) : RecipeService {

    private val coScope = CoroutineScope(Dispatchers.IO)

    private val log: Logger = LoggerFactory.getLogger(RecipePostgresRepository::class.java)

    private object Repository : PanacheRepository<RecipeEntity>

    override fun getAll(): Uni<List<Recipe>> {
        return Repository.listAll()
            .onItem()
            .transform { allRecipes ->
                allRecipes.map { Recipe(it.id, it.title) }
            }
    }

    override fun findById(id: Long, fetchItems: Boolean): Uni<RecipeEntity> {
        return Repository
            .findById(id)
            .onItem().ifNull().failWith { NotFoundException("Recipe with id [$id] does not exist") }
            .onItem().ifNotNull().call { it ->
                if (fetchItems) {
                    Mutiny.fetch(it.items)
                } else {
                    Uni.createFrom().item(it)
                }
            }
    }

    override fun create(title: String): Uni<RecipeEntity> {
        return Repository
            .persistAndFlush(RecipeEntity(title = title))
    }

    override suspend fun update(id: Long, title: String?, items: List<Long>?) {
        val recipeEntity = Repository
            .findById(id)
            .onItem().ifNull().failWith { NotFoundException("Recipe with id [$id] does not exist") }
            .awaitSuspending()

        if (items?.isNotEmpty() == true) {
            updateItems(recipeEntity.id, items)
        }

        if (title != null) {
            Repository.update("set title = ?1", title).awaitSuspending()
        }
    }

    private suspend fun updateItems(recipeId: Long, items: List<Long>) {
        items.map { itemId ->
            coScope.coAsync {
                log.info("Adding $itemId to recipe $id")

                pgPool
                    .preparedQuery("insert into recipe_items (recipe_id, item_id) values ($1, $2)")
                    .execute(Tuple.of(recipeId, itemId))
                    .onItemOrFailure().transform { _, failure ->
                        when (failure) {
                            is PgException -> log.warn("Item {} was already added to shoppingList: {}", itemId, recipeId)
                        }
                    }.awaitSuspending()
            }
        }.awaitAll()
    }
}
