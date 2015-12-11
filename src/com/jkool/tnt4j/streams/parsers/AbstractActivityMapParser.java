/*
 * Copyright (c) 2015 jKool, LLC. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * jKool, LLC. ("Confidential Information").  You shall not disclose
 * such Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with jKool, LLC.
 *
 * JKOOL MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JKOOL SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * CopyrightVersion 1.0
 *
 */

package com.jkool.tnt4j.streams.parsers;

import java.io.InputStream;
import java.io.Reader;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.jkool.tnt4j.streams.fields.ActivityField;
import com.jkool.tnt4j.streams.fields.ActivityFieldLocator;
import com.jkool.tnt4j.streams.fields.ActivityFieldLocatorType;
import com.jkool.tnt4j.streams.fields.ActivityInfo;
import com.jkool.tnt4j.streams.inputs.TNTInputStream;
import com.jkool.tnt4j.streams.utils.StreamsResources;
import com.nastel.jkool.tnt4j.core.OpLevel;
import com.nastel.jkool.tnt4j.sink.EventSink;

/**
 * <p>
 * Implements an activity data parser that assumes each activity data item is an
 * Map data structure, where each field is represented by a key/value pair and
 * the name is used to map each field onto its corresponding activity field.
 * </p>
 *
 * @version $Revision: 1 $
 */
public abstract class AbstractActivityMapParser extends ActivityParser {

	/**
	 * Constructs an AbstractActivityMapParser.
	 */
	protected AbstractActivityMapParser(EventSink logger) {
		super(logger);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProperties(Collection<Map.Entry<String, String>> props) throws Throwable {
		if (props == null) {
			return;
		}
		for (Map.Entry<String, String> prop : props) {
			String name = prop.getKey();
			String value = prop.getValue();

			// TODO:
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This parser supports the following class types (and all classes
	 * extending/implementing any of these):
	 * </p>
	 * <ul>
	 * <li>{@code java.lang.String}</li>
	 * <li>{@code byte[]}</li>
	 * <li>{@code java.io.Reader}</li>
	 * <li>{@code java.io.InputStream}</li>
	 * </ul>
	 */
	@Override
	public boolean isDataClassSupported(Object data) {
		return String.class.isInstance(data) || byte[].class.isInstance(data) || Reader.class.isInstance(data)
				|| InputStream.class.isInstance(data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActivityInfo parse(TNTInputStream stream, Object data) throws IllegalStateException, ParseException {
		if (data == null) {
			return null;
		}
		logger.log(OpLevel.DEBUG, StreamsResources.getStringFormatted("ActivityParser.parsing", data));

		ActivityInfo ai = new ActivityInfo();
		ActivityField field = null;
		try {
			Map<String, ?> dataMap = getDataMap(data);
			if (dataMap == null || dataMap.isEmpty()) {
				logger.log(OpLevel.DEBUG, StreamsResources.getString("ActivityParser.not.find"));
				return null;
			}

			// save entire activity string as message data
			// field = new ActivityField(StreamFieldType.Message.name());
			// applyFieldValue(ai, field, dataStr); //TODO

			// apply fields for parser
			Object value;
			for (Map.Entry<ActivityField, List<ActivityFieldLocator>> fieldEntry : fieldMap.entrySet()) {
				value = null;
				field = fieldEntry.getKey();
				List<ActivityFieldLocator> locations = fieldEntry.getValue();
				if (locations != null) {
					if (locations.size() == 1) {
						// field value is based on single raw data location, get
						// the value of this location
						value = getLocatorValue(stream, locations.get(0), dataMap);
					} else {
						// field value is based on concatenation of several raw
						// data locations, build array to hold data from each
						// location
						Object[] values = new Object[locations.size()];
						for (int li = 0; li < locations.size(); li++) {
							values[li] = getLocatorValue(stream, locations.get(li), dataMap);
						}
						value = values;
					}
				}
				applyFieldValue(stream, ai, field, value);
			}
		} catch (Exception e) {
			ParseException pe = new ParseException(
					StreamsResources.getStringFormatted("ActivityParser.parsing.failed", field), 0);
			pe.initCause(e);
			throw pe;
		}

		return ai;
	}

	/**
	 * Makes map data package containing data of specified activity object.
	 * 
	 * @param data
	 *            activity object data
	 *
	 * @return activity object data map
	 */
	protected abstract Map<String, ?> getDataMap(Object data);

	/**
	 * Gets field value from raw data location and formats it according locator
	 * definition.
	 * 
	 * @param stream
	 *            parent stream
	 * @param locator
	 *            activity field locator
	 * @param dataMap
	 *            activity object data map
	 *
	 * @return value formatted based on locator definition or {@code null} if
	 *         locator is not defined
	 *
	 * @throws ParseException
	 *             if error applying locator format properties to specified
	 *             value
	 *
	 * @see ActivityFieldLocator#formatValue(Object)
	 */
	protected Object getLocatorValue(TNTInputStream stream, ActivityFieldLocator locator, Map<String, ?> dataMap)
			throws ParseException {
		Object val = null;
		if (locator != null) {
			String locStr = locator.getLocator();
			if (!StringUtils.isEmpty(locStr)) {
				if (locator.getBuiltInType() == ActivityFieldLocatorType.StreamProp) {
					val = stream.getProperty(locStr);
				} else {
					String[] path = getNodePath(locStr);
					val = getNode(path, dataMap, 0);
				}
			}
			val = locator.formatValue(val);
		}

		return val;
	}

	private static String[] getNodePath(String locStr) {
		if (StringUtils.isNotEmpty(locStr)) {
			return locStr.split("\\."); // NON-NLS
		}

		return null;
	}

	private static Object getNode(String[] path, Map<String, ?> dataMap, int i) {
		if (ArrayUtils.isEmpty(path) || dataMap == null) {
			return null;
		}

		Object val = dataMap.get(path[i]);

		if (i < path.length - 1 && val instanceof Map) {
			val = getNode(path, (Map<String, ?>) val, ++i);
		}

		return val;
	}
}
