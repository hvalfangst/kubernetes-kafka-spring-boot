package hvalfangst.kafka.producer

import hvalfangst.kafka.config.EnvironmentConfiguration
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.stereotype.Component
import java.util.*

@Component
class GenericProducer(env: EnvironmentConfiguration) {
    private val producer: KafkaProducer<String, String>


    fun sendMessage(topic: String, key: String?, value: String, headers: List<Header>? = null) {
        val record = ProducerRecord(topic, null, System.currentTimeMillis(), key, value, headers)

        producer.send(record) { _, exception ->
            if (exception != null) {
                println("Error sending message: ${exception.message}")
            } else {
                println("Message sent successfully to topic: $topic")
            }
        }
    }

    fun close() {
        producer.close()
    }

    init {
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = env.bootStrapServer
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        producer = KafkaProducer(props)
    }
}
