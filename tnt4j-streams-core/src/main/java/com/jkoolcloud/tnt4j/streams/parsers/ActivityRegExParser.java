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

package com.jkoolcloud.tnt4j.streams.parsers;

import java.io.InputStream;
import java.io.Reader;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.jkoolcloud.tnt4j.core.OpLevel;
import com.jkoolcloud.tnt4j.sink.DefaultEventSinkFactory;
import com.jkoolcloud.tnt4j.sink.EventSink;
import com.jkoolcloud.tnt4j.streams.configure.ParserProperties;
import com.jkoolcloud.tnt4j.streams.fields.*;
import com.jkoolcloud.tnt4j.streams.inputs.TNTInputStream;
import com.jkoolcloud.tnt4j.streams.utils.StreamsResources;

/**
 * <p>
 * Implements an activity data parser that assumes each activity data item is a
 * string of fields as defined by the specified regular expression, with the
 * value for each field being retrieved from either of the 1-based group
 * position, or match position.
 * <p>
 * This parser supports the following properties:
 * <ul>
 * <li>Pattern - contains the regular expression pattern that each data item is
 * assumed to match. (Required)</li>
 * </ul>
 *
 * @version $Revision: 1 $
 */
public class ActivityRegExParser extends ActivityParser {
	private static final EventSink LOGGER = DefaultEventSinkFactory.defaultEventSink(ActivityRegExParser.class);

	/**
	 * Contains the regular expression pattern that each data item is assumed to
	 * match (set by {@code Pattern} property).
	 */
	protected Pattern pattern = null;

	/**
	 * Defines the mapping of activity fields to the regular expression group
	 * location(s) in the raw data from which to extract its value.
	 */
	protected final Map<ActivityField, List<ActivityFieldLocator>> groupMap = new HashMap<ActivityField, List<ActivityFieldLocator>>();

	/**
	 * Defines the mapping of activity fields to the regular expression match
	 * sequence(s) in the raw data from which to extract its value.
	 */
	protected final Map<ActivityField, List<ActivityFieldLocator>> matchMap = new HashMap<ActivityField, List<ActivityFieldLocator>>();

	/**
	 * Constructs a new ActivityRegExParser.
	 */
	public ActivityRegExParser() {
		super(LOGGER);
	}

	@Override
	public void setProperties(Collection<Map.Entry<String, String>> props) throws Exception {
		if (props == null) {
			return;
		}
		for (Map.Entry<String, String> prop : props) {
			String name = prop.getKey();
			String value = prop.getValue();
			if (ParserProperties.PROP_PATTERN.equalsIgnoreCase(name)) {
				if (!StringUtils.isEmpty(value)) {
					pattern = Pattern.compile(value);
					LOGGER.log(OpLevel.DEBUG,
							StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME, "ActivityParser.setting"),
							name, value);
				}
			}
			LOGGER.log(OpLevel.TRACE,
					StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME, "ActivityParser.ignoring"), name);
		}
	}

	@Override
	public void addField(ActivityField field) {
		List<ActivityFieldLocator> locations = field.getLocators();
		if (locations == null) {
			return;
		}
		List<ActivityFieldLocator> matchLocs = new ArrayList<ActivityFieldLocator>();
		List<ActivityFieldLocator> groupLocs = new ArrayList<ActivityFieldLocator>();
		for (ActivityFieldLocator locator : locations) {
			ActivityFieldLocatorType locType = ActivityFieldLocatorType.REGroupNum;
			try {
				locType = ActivityFieldLocatorType.valueOf(locator.getType());
			} catch (Exception e) {
			}
			LOGGER.log(OpLevel.DEBUG,
					StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME, "ActivityParser.adding.field"),
					field); // Utils.getDebugString(field));
			if (locType == ActivityFieldLocatorType.REMatchNum) {
				if (groupMap.containsKey(field)) {
					throw new IllegalArgumentException(StreamsResources.getStringFormatted(
							StreamsResources.RESOURCE_BUNDLE_NAME, "ActivityRegExParser.conflicting.mapping", field));
				}
				matchLocs.add(locator);
			} else {
				if (matchMap.containsKey(field)) {
					throw new IllegalArgumentException(StreamsResources.getStringFormatted(
							StreamsResources.RESOURCE_BUNDLE_NAME, "ActivityRegExParser.conflicting.mapping", field));
				}
				groupLocs.add(locator);
			}
		}
		if (!matchLocs.isEmpty()) {
			matchMap.put(field, matchLocs);
		}
		if (!groupLocs.isEmpty()) {
			groupMap.put(field, groupLocs);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This parser supports the following class types (and all classes
	 * extending/implementing any of these):
	 * <ul>
	 * <li>{@link java.lang.String}</li>
	 * <li>{@code byte[]}</li>
	 * <li>{@link java.io.Reader}</li>
	 * <li>{@link java.io.InputStream}</li>
	 * </ul>
	 */
	@Override
	public boolean isDataClassSupported(Object data) {
		return String.class.isInstance(data) || byte[].class.isInstance(data) || Reader.class.isInstance(data)
				|| InputStream.class.isInstance(data);
	}

	@Override
	public ActivityInfo parse(TNTInputStream<?, ?> stream, Object data) throws IllegalStateException, ParseException {
		if (pattern == null || StringUtils.isEmpty(pattern.pattern())) {
			throw new IllegalStateException(StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME,
					"ActivityRegExParser.no.regex.pattern"));
		}
		if (data == null) {
			return null;
		}
		String dataStr = getNextString(data);
		if (StringUtils.isEmpty(dataStr)) {
			return null;
		}
		LOGGER.log(OpLevel.DEBUG,
				StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME, "ActivityParser.parsing"), dataStr);
		Matcher matcher = pattern.matcher(dataStr);
		if (matcher == null || !matcher.matches()) {
			LOGGER.log(OpLevel.DEBUG,
					StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME, "ActivityParser.input.not.match"),
					getName());
			return null;
		}
		ActivityInfo ai = new ActivityInfo();
		// save entire activity string as message data
		ActivityField field = new ActivityField(StreamFieldType.Message.name());
		applyFieldValue(stream, ai, field, dataStr);
		// apply fields for parser
		try {
			if (!matchMap.isEmpty()) {
				LOGGER.log(OpLevel.DEBUG, StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME,
						"ActivityRegExParser.applying.regex"), matchMap.size());
				ArrayList<String> matches = new ArrayList<String>();
				matches.add(""); // dummy entry to index array with match
									// locations
				while (matcher.find()) {
					String matchStr = matcher.group().trim();
					matches.add(matchStr);
					LOGGER.log(OpLevel.TRACE, StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME,
							"ActivityRegExParser.match"), matches.size(), matchStr);
				}
				LOGGER.log(OpLevel.DEBUG, StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME,
						"ActivityRegExParser.found.matches"), matches.size());
				Object value;
				for (Map.Entry<ActivityField, List<ActivityFieldLocator>> fieldMapEntry : matchMap.entrySet()) {
					field = fieldMapEntry.getKey();
					List<ActivityFieldLocator> locations = fieldMapEntry.getValue();
					value = null;
					if (locations != null) {
						LOGGER.log(OpLevel.TRACE, StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME,
								"ActivityRegExParser.setting.field"), field);
						if (locations.size() == 1) {
							value = getLocatorValue(stream, locations.get(0), ActivityFieldLocatorType.REMatchNum,
									matcher, matches);
						} else {
							Object[] values = new Object[locations.size()];
							for (int li = 0; li < locations.size(); li++) {
								values[li] = getLocatorValue(stream, locations.get(li),
										ActivityFieldLocatorType.REMatchNum, matcher, matches);
							}
							value = values;
						}
					}
					applyFieldValue(stream, ai, field, value);
				}
			}
		} catch (Exception e) {
			ParseException pe = new ParseException(StreamsResources.getStringFormatted(
					StreamsResources.RESOURCE_BUNDLE_NAME, "ActivityRegExParser.failed.parsing.regex", field), 0);
			pe.initCause(e);
			throw pe;
		}
		try {
			Object value;
			for (Map.Entry<ActivityField, List<ActivityFieldLocator>> fieldMapEntry : groupMap.entrySet()) {
				field = fieldMapEntry.getKey();
				List<ActivityFieldLocator> locations = fieldMapEntry.getValue();
				value = null;
				if (locations != null) {
					LOGGER.log(OpLevel.TRACE, StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME,
							"ActivityRegExParser.setting.group.field"), field);
					if (locations.size() == 1) {
						value = getLocatorValue(stream, locations.get(0), ActivityFieldLocatorType.REGroupNum, matcher,
								null);
					} else {
						Object[] values = new Object[locations.size()];
						for (int li = 0; li < locations.size(); li++) {
							values[li] = getLocatorValue(stream, locations.get(li), ActivityFieldLocatorType.REGroupNum,
									matcher, null);
						}
						value = values;
					}
				}
				applyFieldValue(stream, ai, field, value);
			}
		} catch (Exception e) {
			ParseException pe = new ParseException(StreamsResources.getStringFormatted(
					StreamsResources.RESOURCE_BUNDLE_NAME, "ActivityRegExParser.failed.parsing.regex.group", field), 0);
			pe.initCause(e);
			throw pe;
		}
		return ai;
	}

	private static Object getLocatorValue(TNTInputStream<?, ?> stream, ActivityFieldLocator locator,
			ActivityFieldLocatorType locType, Matcher matcher, List<String> matches) throws ParseException {
		Object val = null;
		if (locator != null) {
			String locStr = locator.getLocator();
			if (!StringUtils.isEmpty(locStr)) {
				if (locator.getBuiltInType() == ActivityFieldLocatorType.StreamProp) {
					val = stream.getProperty(locStr);
				} else {
					int loc = Integer.parseInt(locStr);
					if (locType == ActivityFieldLocatorType.REMatchNum) {
						if (loc <= matches.size()) {
							val = matches.get(loc);
						}
					} else {
						if (loc <= matcher.groupCount()) {
							val = matcher.group(loc);
						}
					}
				}
			}
			val = locator.formatValue(val);
		}
		return val;
	}
}