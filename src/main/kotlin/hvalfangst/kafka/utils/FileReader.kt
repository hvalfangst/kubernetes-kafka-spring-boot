package hvalfangst.kafka.utils

import java.io.File

class FileReader {
    companion object {
        fun readFile(inputFileName: String, numPartitions: Int): Array<String> {
            val inputFile = File(inputFileName)
            val totalWords = inputFile.readText().split(",").size
            val wordsPerPartition = totalWords / numPartitions

            val outputContentArray = mutableListOf<String>()

            var wordCount = 0
            var fileNum = 1
            var outputFileContent = StringBuilder()
            inputFile.forEachLine { line ->
                val words = line.split(",")
                for (word in words) {
                    outputFileContent.append("$word ")
                    wordCount++
                    if (wordCount >= wordsPerPartition) {
                        outputContentArray.add(outputFileContent.toString())
                        outputFileContent.clear()
                        fileNum++
                        wordCount = 0
                        if (fileNum > numPartitions) {
                            return@forEachLine  // Exit the loop if the desired number of files is reached
                        }
                    }
                }
            }

            // Add any remaining content to the last partition
            if (outputFileContent.isNotEmpty()) {
                outputContentArray.add(outputFileContent.toString())
            }

            return outputContentArray.toTypedArray()
        }
    }
}