package bearmug.lambda


import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject

import static io.micronaut.http.HttpMethod.GET
import static io.micronaut.http.HttpRequest.create

@MicronautTest
class PingControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    @Unroll
    def "GET /ping/#flavor responds 200 OK"() {
        when:
        def response = client
                .toBlocking()
                .retrieve(create(GET, "/ping/$flavor"), String.class)

        then:
        response == '{ "pong" : true }'

        where:
        flavor << [
                'java',
                'graal',
                'rjava',
                'rgraal'
        ]
    }
}
