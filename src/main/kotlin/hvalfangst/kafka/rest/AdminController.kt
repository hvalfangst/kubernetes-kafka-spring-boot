package hvalfangst.kafka.rest

import hvalfangst.kafka.producer.GenericProducer
import hvalfangst.kafka.utils.FileReader.Companion.readFile
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.system.measureTimeMillis

@RestController
class AdminController(private val producer: GenericProducer) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/api/kafka/startQuicksort")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun distributePartitions() {
        val inputFileName = "one_million_words.txt"
        val numPartitions = 10
        val partitionedFileContent: Array<String>

        val fileSplitTime = measureTimeMillis {
            partitionedFileContent = readFile(inputFileName, numPartitions)
        }

        logger.info("\n - - - - File Split Time: [$fileSplitTime ms] - - - - \n")
        publishFilePartitions(partitionedFileContent)
    }

    private fun publishFilePartitions(partitionedFileContent: Array<String>) {
        for (i in partitionedFileContent.indices) {
            val message = partitionedFileContent[i]
            val topic = "usp-${i+1}"

            producer.sendMessage(topic, topic, message, null)
        }
    }
}