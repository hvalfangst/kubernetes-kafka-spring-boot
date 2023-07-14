package hvalfangst.kafka

import hvalfangst.kafka.config.DatabaseConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    DatabaseConfig.init()
    runApplication<Application>(*args)
}