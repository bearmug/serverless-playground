package bearmug.lambda

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.micronaut.test.annotation.MicronautTest

@MicronautTest
class PingServiceTest(
        private val pingService: PingService): BehaviorSpec({

    given("the ping service") {

        `when`("ping triggered") {
            val response = pingService.pong()
            then("response is the pong") {
                response shouldBe """{ "pong" : true }"""
            }
        }
    }
})