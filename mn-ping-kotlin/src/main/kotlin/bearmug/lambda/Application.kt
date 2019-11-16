package bearmug.lambda

import io.micronaut.runtime.Micronaut

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build(*args)
                .start()
    }
}