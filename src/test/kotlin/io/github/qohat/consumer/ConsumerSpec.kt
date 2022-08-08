package io.github.qohat.consumer

import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.TestContainerExtension
import io.kotest.extensions.testcontainers.kafka.createStringStringConsumer
import io.kotest.extensions.testcontainers.kafka.createStringStringProducer
import io.kotest.matchers.collections.shouldHaveSize
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration

class ConsumerSpec: FunSpec(){
    init {
        val kafka = install(TestContainerExtension(KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1")))) {
            withEmbeddedZookeeper()
            withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true")
        }

        test("Should consume") {
            val producer = kafka.createStringStringProducer()
            producer.send(ProducerRecord("foo", null, "bubble bobble"))
            producer.close()

            val consumer = kafka.createStringStringConsumer {
                this[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = 1
            }

            consumer.subscribe(listOf("foo"))
            val records = consumer.poll(Duration.ofSeconds(100))
            records.shouldHaveSize(1)
        }
    }
}