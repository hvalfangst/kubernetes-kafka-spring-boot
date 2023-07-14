package hvalfangst.kafka.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "env")
class EnvironmentConfiguration {

    // - - - - - - MISC - - - - -
    var bootStrapServer = "nil"

    // - - - TOPICS - - - -
    var usersTopic = "nil"
    var sortedPartitionsTopic = "nil"
    var systemLordTopic = "nil"
}