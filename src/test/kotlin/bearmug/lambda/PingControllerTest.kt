package bearmug.lambda

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import javax.inject.Inject

@MicronautTest
class PingControllerTest(
        @Inject
        @Client("/")
        private val httpClient: HttpClient) : StringSpec({
    "ping endpoints valled" {
        forall(
                row("graal"),
                row("java")
        ) { path ->
            val response = httpClient
                    .toBlocking()
                    .retrieve(HttpRequest.GET<Any>("/ping/$path"))
            response shouldBe """{ "pong" : true }"""
        }
    }
})