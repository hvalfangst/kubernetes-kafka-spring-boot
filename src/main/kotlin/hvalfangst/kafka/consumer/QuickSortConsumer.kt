package hvalfangst.kafka.consumer

import hvalfangst.kafka.config.EnvironmentConfiguration
import hvalfangst.kafka.producer.GenericProducer
import hvalfangst.kafka.state.State.HALT
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class QuickSortConsumer(private val env: EnvironmentConfiguration, private val producer: GenericProducer) {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private var halted = false

    @KafkaListener(
        topics = ["\${env.unsortedPartitionsTopic}", "\${env.systemLordTopic}"],
        groupId = "\${env.groupId}"
    )
    fun consume(record: ConsumerRecord<String, String>) {
        val key = record.key()

        if(key == HALT) {
            halted = true
        }

        if(!halted) {
            val topicName = record.topic()

            var unsortedWordsArray: List<String>
            val arrayReadTime = measureTimeMillis {
                unsortedWordsArray = record.value().split(" ")
            }

            logger.info("\n - - - - Array read for topic [${topicName}] took [$arrayReadTime ms] - - - - \n")

            var sortedWords: String
            val sortingTime = measureTimeMillis {
                sortedWords = unsortedWordsArray.sorted().joinToString(" ")
            }

            logger.info("\n - - - - List sort for topic [${topicName}] took [$sortingTime ms] - - - - \n")

            producer.sendMessage(env.sortedPartitionsTopic, topicName, sortedWords, null)
        } else {
            logger.info("\n\n - - - - - - HALTED - - - - - -  \n\n")
        }


    }
}
