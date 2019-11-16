package bearmug.lambda

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

interface PingService {
    fun pong(): String
}

@Singleton
class PingServiceImpl : PingService {
    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java.simpleName)
    }

    override fun pong(): String {
        log.trace("Received a ping.")
        return """{ "pong" : true }"""
    }
}