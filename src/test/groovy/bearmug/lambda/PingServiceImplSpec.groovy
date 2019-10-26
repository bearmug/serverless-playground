package bearmug.lambda

import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class PingServiceImplSpec extends Specification {

    @Inject
    PingServiceImpl pingService

    def "should respond 'pong'"() {
        when:
        def response = pingService.pong()

        then:
        response == '{"pong":true}'
    }
}
