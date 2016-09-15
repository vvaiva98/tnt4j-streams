/*
 * Copyright 2014-2016 JKOOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jkoolcloud.tnt4j.streams.inputs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.*;
import java.util.Map.Entry;

import org.I0Itec.zkclient.exception.ZkTimeoutException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Ignore;
import org.junit.Test;

import com.jkoolcloud.tnt4j.streams.configure.StreamProperties;
import com.jkoolcloud.tnt4j.streams.utils.KafkaStreamConstants;
import com.jkoolcloud.tnt4j.streams.utils.StreamsResources;

import kafka.consumer.ConsumerIterator;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * @author akausinis
 * @version 1.0
 */
public class KafkaStreamTest {
	private static final String DEFAULT_TEST_TOPIC = "TNT4JKafkaTestTopic";

	KafkaStream input;

	@Test
	public void testProperties() throws Exception {
		input = new KafkaStream();

		final Collection<Entry<String, String>> properties = InputPropertiesTestUtils
				.makeTestPropertiesSet(StreamProperties.PROP_TOPIC_NAME, DEFAULT_TEST_TOPIC);
		properties.addAll(InputPropertiesTestUtils.makeTestPropertiesSet("Topic", DEFAULT_TEST_TOPIC));
		properties.addAll(InputPropertiesTestUtils.makeTestPropertiesSet("zookeeper.connect", "127.0.0.1:2181"));
		properties.addAll(InputPropertiesTestUtils.makeTestPropertiesSet("group.id", "TEST"));
		input.setProperties(properties);
		for (Map.Entry<String, String> property : properties) {
			assertEquals("Fail for property: " + property.getKey(), property.getValue(),
					input.getProperty(property.getKey()));
		}
	}

	@Test(expected = ZkTimeoutException.class)
	public void testInitialize() throws Exception {
		testProperties();
		input.initialize();
	}

	@Test
	public void testRB() {
		String keyModule = "KafkaStream.stream.ready";
		String keyCore = "ActivityField.field.type.name.empty";

		String rbs1 = StreamsResources.getString(KafkaStreamConstants.RESOURCE_BUNDLE_NAME, keyModule);
		assertNotEquals("Kafka resource bundle entry not found", rbs1, keyModule);
		rbs1 = StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME, keyModule);
		assertEquals("Kafka resource bundle entry found in core", rbs1, keyModule);
		rbs1 = StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME, keyCore);
		assertNotEquals("Core resource bundle entry not found", rbs1, keyCore);
		rbs1 = StreamsResources.getString(KafkaStreamConstants.RESOURCE_BUNDLE_NAME, keyCore);
		assertEquals("Core resource bundle entry found in kafka", rbs1, keyCore);
	}

	@Ignore("Used to run when testing server")
	@Test
	public void produceMessages() throws InterruptedException {
		Properties props = new Properties();
		props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put("client.id", "TestProducer");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		final KafkaProducer<Integer, String> producer = new KafkaProducer<Integer, String>(props);

		Thread thred = new Thread(new Runnable() {

			@Override
			public void run() {
				int messageNo = 1;
				for (int i = 0; i <= 150; i++) {
					System.out.println("Sending message: " + i);
					String messageStr = "0:0:0:0:0:0:0:1 - - [09/Sep/2016:15:18:34 +0300] \"GET /SimpleJSF/Index.xhtml "
							+ i + " HTTP/1.1\" 200 6561";
					long startTime = System.currentTimeMillis();
					producer.send(new ProducerRecord<Integer, String>(DEFAULT_TEST_TOPIC, messageStr));
				}
			}
		});
		thred.start();
		thred.join();
		producer.close();
	}

	@Ignore("Used to run when testing server")
	@Test
	public void consumeMessages() {
		Properties props = new Properties();

		props.put("zookeeper.connect", "localhost:2181");
		props.put("group.id", "TNT4JStreams");
		props.put("zookeeper.session.timeout.ms", "4000");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		props.put("consumer.timeout.ms", "1000");

		ConsumerConnector consumer = kafka.consumer.Consumer
				.createJavaConsumerConnector(new kafka.consumer.ConsumerConfig(props));
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(DEFAULT_TEST_TOPIC, 1);

		Map<String, List<kafka.consumer.KafkaStream<byte[], byte[]>>> consumerMap = consumer
				.createMessageStreams(topicCountMap);
		kafka.consumer.KafkaStream<byte[], byte[]> stream = consumerMap.get(DEFAULT_TEST_TOPIC).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();

		while (it.hasNext()) {
			System.out.println(new String(it.next().message()));
		}

		System.err.println();
		consumer.shutdown();
	}
}
