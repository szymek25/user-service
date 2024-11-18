package pl.szymanski.user.service.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	private static final String SCHEMA_REGISTRY_URL_KEY = "schema.registry.url";
	@Value("${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value("${kafka.groupId}")
	private String groupId;

	@Value("${kafka.schemaRegistryUrl}")
	private String schemaRegistryUrlKey;

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(
				ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				bootstrapAddress);
		props.put(
				ConsumerConfig.GROUP_ID_CONFIG,
				groupId);
		props.put(
				ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class);
		props.put(
				ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String>
	kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	private ConsumerFactory<String, UpdateUserEvent> getAvroConsumerConfig() {
		final Map<String, Object> properties = new HashMap<>();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		properties.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, "5000");
		properties.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, "5000");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
		properties.put(SCHEMA_REGISTRY_URL_KEY, schemaRegistryUrlKey);
		properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

		return new DefaultKafkaConsumerFactory<>(properties);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, UpdateUserEvent>
	userUpdatesContainerFactor() {
		ConcurrentKafkaListenerContainerFactory<String, UpdateUserEvent> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(getAvroConsumerConfig());
		return factory;
	}
}

