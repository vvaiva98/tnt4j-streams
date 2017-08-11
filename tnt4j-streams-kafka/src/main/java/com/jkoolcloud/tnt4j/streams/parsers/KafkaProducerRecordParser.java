/*
 * Copyright 2014-2017 JKOOL, LLC.
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

package com.jkoolcloud.tnt4j.streams.parsers;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.jkoolcloud.tnt4j.sink.DefaultEventSinkFactory;
import com.jkoolcloud.tnt4j.sink.EventSink;
import com.jkoolcloud.tnt4j.streams.fields.ActivityFieldLocator;

/**
 * Implements an activity data parser that assumes each activity data item is an plain java {@link ProducerRecord} data
 * structure, where each field is represented by declared class field and the field name is used to map each field into
 * its corresponding activity field.
 * <p>
 * List of supported field names:
 * <ul>
 * <li>topic - topic name record is being sent to</li>
 * <li>partition - partition identifier to which the record will be sent</li>
 * <li>timestamp - record timestamp value</li>
 * <li>key - record key</li>
 * <li>value - record data</li>
 * </ul>
 * <p>
 * If {@code key} or {@code value} contains complex data, use stacked parsers to parse that data.
 *
 * @version $Revision: 1 $
 */
public class KafkaProducerRecordParser extends GenericActivityParser<ProducerRecord<?, ?>> {
	private static final EventSink LOGGER = DefaultEventSinkFactory.defaultEventSink(KafkaProducerRecordParser.class);

	/**
	 * Constructs a new KafkaProducerRecordParser.
	 */
	public KafkaProducerRecordParser() {
		super();
	}

	@Override
	protected EventSink logger() {
		return LOGGER;
	}

	/**
	 * Returns whether this parser supports the given format of the activity data. This is used by activity streams to
	 * determine if the parser can parse the data in the format that the stream has it.
	 * <p>
	 * This parser supports the following class types (and all classes extending/implementing any of these):
	 * <ul>
	 * <li>{@link org.apache.kafka.clients.producer.ProducerRecord}</li>
	 * </ul>
	 *
	 * @param data
	 *            data object whose class is to be verified
	 * @return {@code true} if this parser can process data in the specified format, {@code false} - otherwise
	 */
	@Override
	protected boolean isDataClassSupportedByParser(Object data) {
		return ProducerRecord.class.isInstance(data);
	}

	@Override
	public void setProperties(Collection<Map.Entry<String, String>> props) throws Exception {
		if (props == null) {
			return;
		}

		super.setProperties(props);

		// for (Map.Entry<String, String> prop : props) {
		// String name = prop.getKey();
		// String value = prop.getValue();
		//
		// // no any additional properties are required yet.
		// if (false) {
		// logger().log(OpLevel.DEBUG,
		// StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME, "ActivityParser.setting"),
		// name, value);
		// }
		// }
	}

	@Override
	protected Object resolveLocatorValue(ActivityFieldLocator locator, ActivityContext cData,
			AtomicBoolean formattingNeeded) throws ParseException {
		Object val = null;
		String locStr = locator.getLocator();
		ProducerRecord<?, ?> cRecord = cData.getData();

		if (StringUtils.isNotEmpty(locStr)) {
			if (locStr.equalsIgnoreCase("topic")) { // NON-NLS
				val = cRecord.topic();
			} else if (locStr.equalsIgnoreCase("partition")) { // NON-NLS
				val = cRecord.partition();
			} else if (locStr.equalsIgnoreCase("timestamp")) { // NON-NLS
				val = cRecord.timestamp();
			} else if (locStr.equalsIgnoreCase("key")) { // NON-NLS
				val = cRecord.key();
			} else if (locStr.equalsIgnoreCase("value")) { // NON-NLS
				val = cRecord.value();
			}
		}

		return val;
	}
}
