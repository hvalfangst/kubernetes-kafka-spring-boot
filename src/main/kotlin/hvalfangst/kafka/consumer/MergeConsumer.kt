package hvalfangst.kafka.consumer

import hvalfangst.kafka.config.EnvironmentConfiguration
import hvalfangst.kafka.producer.GenericProducer
import hvalfangst.kafka.state.State.HALT
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.io.File
import kotlin.system.measureTimeMillis

@Component
class MergeConsumer(private val env: EnvironmentConfiguration, private val producer: GenericProducer) {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val uniqueKeys: HashSet<String> = hashSetOf()
    private val sortedPartitions: MutableMap<String, String> = mutableMapOf()

    @KafkaListener(
        topics = ["\${env.sortedPartitionsTopic}"],
        groupId = "\${env.groupId}"
    )
    fun consume(record: ConsumerRecord<String, String>) {
        val key = record.key()

        if (!uniqueKeys.contains(key)) {
            uniqueKeys.add(key)
            val sortedWordPartition = record.value()
            sortedPartitions[key] = sortedWordPartition
            logger.info("Received Message - Key: ${key}, characters: ${record.value().length}")
        }

        if (uniqueKeys.size == 1) {
            var sortedResult: List<String>
            val mergeSortTime = measureTimeMillis {
                 sortedResult = mergeSort(sortedPartitions.values.toList())
            }

            logger.info("\n - - - - Merge sort for topic [${record.topic()}] took [$mergeSortTime ms] - - - - \n")

            writeMergedMessagesToFile(sortedResult)
            producer.sendMessage(env.systemLordTopic, HALT, "\n ALGORITHM CONCLUDED \n", null)
        }
    }

    private fun mergeSort(list: List<String>): List<String> {
        if (list.size <= 1) return list

        val mid = list.size / 2
        val left = mergeSort(list.subList(0, mid))
        val right = mergeSort(list.subList(mid, list.size))

        return merge(left, right)
    }

    private fun merge(left: List<String>, right: List<String>): List<String> {
        val result = mutableListOf<String>()
        var i = 0
        var j = 0

        while (i < left.size && j < right.size) {
            if (left[i] < right[j]) {
                result.add(left[i])
                i++
            } else {
                result.add(right[j])
                j++
            }
        }

        while (i < left.size) {
            result.add(left[i])
            i++
        }

        while (j < right.size) {
            result.add(right[j])
            j++
        }

        return result
    }

    private fun writeMergedMessagesToFile(mergedMessages: List<String>) {
        val outputFile = File("one_million_sorted_words.txt")
        outputFile.writeText(mergedMessages.toString())
        logger.info("Merged messages written to ${outputFile.absolutePath}")
    }
}
