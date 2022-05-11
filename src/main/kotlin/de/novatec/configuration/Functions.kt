package de.novatec.configuration

import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending

suspend inline fun <T, R> T.async(block: T.() -> Uni<R>): R {
    return block().awaitSuspending()
}
