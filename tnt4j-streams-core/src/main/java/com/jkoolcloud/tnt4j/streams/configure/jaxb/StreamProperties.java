/*
 * Copyright 2014-2023 JKOOL, LLC.
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

package com.jkoolcloud.tnt4j.streams.configure.jaxb;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for StreamProperties.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="StreamProperties"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DateTime"/&gt;
 *     &lt;enumeration value="FileName"/&gt;
 *     &lt;enumeration value="Host"/&gt;
 *     &lt;enumeration value="Port"/&gt;
 *     &lt;enumeration value="QueueManager"/&gt;
 *     &lt;enumeration value="Queue"/&gt;
 *     &lt;enumeration value="Topic"/&gt;
 *     &lt;enumeration value="Subscription"/&gt;
 *     &lt;enumeration value="TopicString"/&gt;
 *     &lt;enumeration value="Channel"/&gt;
 *     &lt;enumeration value="StripHeaders"/&gt;
 *     &lt;enumeration value="StartFromLatest"/&gt;
 *     &lt;enumeration value="FileReadDelay"/&gt;
 *     &lt;enumeration value="HaltIfNoParser"/&gt;
 *     &lt;enumeration value="UseExecutors"/&gt;
 *     &lt;enumeration value="ExecutorThreadsQuantity"/&gt;
 *     &lt;enumeration value="ExecutorRejectedTaskOfferTimeout"/&gt;
 *     &lt;enumeration value="ExecutorsTerminationTimeout"/&gt;
 *     &lt;enumeration value="ExecutorsBoundedModel"/&gt;
 *     &lt;enumeration value="ExecutorsImmediateShutdown"/&gt;
 *     &lt;enumeration value="Keystore"/&gt;
 *     &lt;enumeration value="KeystorePass"/&gt;
 *     &lt;enumeration value="KeyPass"/&gt;
 *     &lt;enumeration value="JNDIFactory"/&gt;
 *     &lt;enumeration value="JMSConnFactory"/&gt;
 *     &lt;enumeration value="ServerURI"/&gt;
 *     &lt;enumeration value="UserName"/&gt;
 *     &lt;enumeration value="Password"/&gt;
 *     &lt;enumeration value="UseSSL"/&gt;
 *     &lt;enumeration value="RestartOnInputClose"/&gt;
 *     &lt;enumeration value="ArchType"/&gt;
 *     &lt;enumeration value="BufferSize"/&gt;
 *     &lt;enumeration value="FullBufferAddPolicy"/&gt;
 *     &lt;enumeration value="FilePolling"/&gt;
 *     &lt;enumeration value="RestoreState"/&gt;
 *     &lt;enumeration value="StartServer"/&gt;
 *     &lt;enumeration value="InputCloseable"/&gt;
 *     &lt;enumeration value="RangeToStream"/&gt;
 *     &lt;enumeration value="StreamReconnectDelay"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "StreamProperties")
@XmlEnum
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2017-06-02T02:29:59+03:00", comments = "JAXB RI v2.2.4-2")
public enum StreamProperties {

	/**
	 * 
	 * Property value represents an initial, base, or default date, time, or date/time.
	 * 
	 * 
	 */
	@XmlEnumValue("DateTime")
	DATE_TIME("DateTime"),

	/**
	 * 
	 * Property value represents a file name.
	 * 
	 * 
	 */
	@XmlEnumValue("FileName")
	FILE_NAME("FileName"),

	/**
	 * 
	 * Property value represents a connection host name.
	 * 
	 * 
	 */
	@XmlEnumValue("Host")
	HOST("Host"),

	/**
	 * 
	 * Property value is a connection port number, interpreted based on the context in which it is used.
	 * 
	 * 
	 */
	@XmlEnumValue("Port")
	PORT("Port"),

	/**
	 * 
	 * Property value is a queue manager name.
	 * 
	 * 
	 */
	@XmlEnumValue("QueueManager")
	QUEUE_MANAGER("QueueManager"),

	/**
	 * 
	 * Property value is a queue name.
	 * 
	 * 
	 */
	@XmlEnumValue("Queue")
	QUEUE("Queue"),

	/**
	 * 
	 * Property value is a topic name.
	 * 
	 * 
	 */
	@XmlEnumValue("Topic")
	TOPIC("Topic"),

	/**
	 * 
	 * Property value is a subscription name.
	 * 
	 * 
	 */
	@XmlEnumValue("Subscription")
	SUBSCRIPTION("Subscription"),

	/**
	 * 
	 * Property value is a topic string to subscribe to.
	 * 
	 * 
	 */
	@XmlEnumValue("TopicString")
	TOPIC_STRING("TopicString"),

	/**
	 * 
	 * Property value is a channel name.
	 * 
	 * 
	 */
	@XmlEnumValue("Channel")
	CHANNEL("Channel"),

	/**
	 * 
	 * Property identifies whether stream should strip RAW activity data (e.g., WMQ message) headers.
	 * 
	 * 
	 */
	@XmlEnumValue("StripHeaders")
	STRIP_HEADERS("StripHeaders"),

	/**
	 * 
	 * Property identifies that streaming should be performed from latest log entry. If 'false' - then latest log file
	 * is streamed from beginning.
	 * 
	 * 
	 */
	@XmlEnumValue("StartFromLatest")
	START_FROM_LATEST("StartFromLatest"),

	/**
	 * 
	 * Property defines delay in seconds between file reading iterations.
	 * 
	 * 
	 */
	@XmlEnumValue("FileReadDelay")
	FILE_READ_DELAY("FileReadDelay"),

	/**
	 * 
	 * Property identifies whether stream should halt if none of the parsers can parse activity RAW data. If set to
	 * 'false' - puts log entry and continues.
	 * 
	 * 
	 */
	@XmlEnumValue("HaltIfNoParser")
	HALT_IF_NO_PARSER("HaltIfNoParser"),

	/**
	 * 
	 * Property identifies identifies whether stream should use executor service to process activities data items
	 * asynchronously or not.
	 * 
	 * 
	 */
	@XmlEnumValue("UseExecutors")
	USE_EXECUTORS("UseExecutors"),

	/**
	 * 
	 * Property defines executor service thread pool size.
	 * 
	 * 
	 */
	@XmlEnumValue("ExecutorThreadsQuantity")
	EXECUTOR_THREADS_QUANTITY("ExecutorThreadsQuantity"),

	/**
	 * 
	 * Property defines time to wait (in seconds) for a executor service to terminate.
	 * 
	 * 
	 */
	@XmlEnumValue("ExecutorRejectedTaskOfferTimeout")
	EXECUTOR_REJECTED_TASK_OFFER_TIMEOUT("ExecutorRejectedTaskOfferTimeout"),

	/**
	 * 
	 * Property defines time to wait (in seconds) for a task to be inserted into bounded queue if max. queue size is
	 * reached.
	 * 
	 * 
	 */
	@XmlEnumValue("ExecutorsTerminationTimeout")
	EXECUTORS_TERMINATION_TIMEOUT("ExecutorsTerminationTimeout"),

	/**
	 * 
	 * Property identifies whether executor service should use bounded tasks queue model.
	 * 
	 * 
	 */
	@XmlEnumValue("ExecutorsBoundedModel")
	EXECUTORS_BOUNDED_MODEL("ExecutorsBoundedModel"),

	/**
	 * 
	 * Property identifies whether executor service shutdown shall be immediate (dropping all pending tasks) or graceful
	 * (trying to complete processing of pending tasks).
	 * 
	 * 
	 */
	@XmlEnumValue("ExecutorsImmediateShutdown")
	EXECUTORS_IMMEDIATE_SHUTDOWN("ExecutorsImmediateShutdown"),

	/**
	 * 
	 * Property defines keystore path.
	 * 
	 * 
	 */
	@XmlEnumValue("Keystore")
	KEYSTORE("Keystore"),

	/**
	 * 
	 * Property defines keystore password.
	 * 
	 * 
	 */
	@XmlEnumValue("KeystorePass")
	KEYSTORE_PASS("KeystorePass"),

	/**
	 * 
	 * Property defines key password.
	 * 
	 * 
	 */
	@XmlEnumValue("KeyPass")
	KEY_PASS("KeyPass"),

	/**
	 * 
	 * Property defines JNDI context factory class name.
	 * 
	 * 
	 */
	@XmlEnumValue("JNDIFactory")
	JNDI_FACTORY("JNDIFactory"),

	/**
	 * 
	 * Property defines JMS connection factory class name.
	 * 
	 * 
	 */
	@XmlEnumValue("JMSConnFactory")
	JMS_CONN_FACTORY("JMSConnFactory"),

	/**
	 * 
	 * Property defines a connection server URI.
	 * 
	 * 
	 */
	@XmlEnumValue("ServerURI")
	SERVER_URI("ServerURI"),

	/**
	 * 
	 * Property defines a user/login name.
	 * 
	 * 
	 */
	@XmlEnumValue("UserName")
	USER_NAME("UserName"),

	/**
	 * 
	 * Property defines a user/login password.
	 * 
	 * 
	 */
	@XmlEnumValue("Password")
	PASSWORD("Password"),

	/**
	 * 
	 * Property identifies whether connection should use SSL.
	 * 
	 * 
	 */
	@XmlEnumValue("UseSSL")
	USE_SSL("UseSSL"),

	/**
	 * 
	 * Property indicates to restart stream if input socked gets closed.
	 * 
	 * 
	 */
	@XmlEnumValue("RestartOnInputClose")
	RESTART_ON_INPUT_CLOSE("RestartOnInputClose"),

	/**
	 *
	 * Property to define zipped stream processed archive type (e.g., ZIP, GZIP, JAR).
	 *
	 *
	 */
	@XmlEnumValue("ArchType")
	ARCH_TYPE("ArchType"),

	/**
	 *
	 * Property to define buffered stream buffer max. capacity.
	 *
	 *
	 */
	@XmlEnumValue("BufferSize")
	BUFFER_SIZE("BufferSize"),

	/**
	 * 
	 * Defines policy how to perform adding new RAW activity data entry, when buffer queue is full: WAIT or DROP.
	 *
	 *
	 */
	@XmlEnumValue("FullBufferAddPolicy")
	FULL_FUFFER_ADD_POLICY("FullBufferAddPolicy"),

	/**
	 *
	 * Property indicates that stream should run in file polling mode.
	 *
	 *
	 */
	@XmlEnumValue("FilePolling")
	FILE_POLLING("FilePolling"),

	/**
	 *
	 * Property indicates that stream should restore streaming state after (re)start (i.e. continue from last streamed
	 * file line).
	 *
	 *
	 */
	@XmlEnumValue("RestoreState")
	RESTORE_STATE("RestoreState"),

	/**
	 *
	 * Property indicates that stream should start as server (e.g., Kafka server) if stream supports both client and
	 * server modes.
	 *
	 *
	 */
	@XmlEnumValue("StartServer")
	START_SERVER("StartServer"),

	/**
	 *
	 * Property indicates that stream should close input after streaming is complete.
	 *
	 *
	 */
	@XmlEnumValue("InputCloseable")
	INPUT_CLOSEABLE("InputCloseable"),

	/**
	 *
	 * Property to define streamed activity data range (i.e. file lines or sheet rows) from:to.
	 * 
	 *
	 */
	@XmlEnumValue("RangeToStream")
	RANGE_TO_STREAM("RangeToStream"),

	/**
	 *
	 * Property defines delay in seconds between queue manager reconnection or failed queue GET iterations.
	 *
	 *
	 */
	@XmlEnumValue("StreamReconnectDelay")
	STREAM_RECONNECT_DELAY("StreamReconnectDelay"),

	/**
	 *
	 * Property defines charset name of streamed data. Charset name must comply Java specification to be handled
	 * properly.
	 *
	 *
	 */
	@XmlEnumValue("Charset")
	CHARSET("Charset");

	private final String value;

	StreamProperties(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static StreamProperties fromValue(String v) {
		for (StreamProperties c : StreamProperties.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
