/*
 * Copyright 2014-2018 JKOOL, LLC.
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

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.jkoolcloud.tnt4j.core.OpLevel;
import com.jkoolcloud.tnt4j.sink.DefaultEventSinkFactory;
import com.jkoolcloud.tnt4j.sink.EventSink;
import com.jkoolcloud.tnt4j.streams.configure.ParserProperties;
import com.jkoolcloud.tnt4j.streams.fields.ActivityFieldLocator;
import com.jkoolcloud.tnt4j.streams.fields.ActivityFieldLocatorType;
import com.jkoolcloud.tnt4j.streams.fields.ActivityInfo;
import com.jkoolcloud.tnt4j.streams.inputs.TNTInputStream;
import com.jkoolcloud.tnt4j.streams.utils.StreamsResources;

/**
 * Implements an activity data parser that assumes each activity data item is a string of fields as defined by the
 * specified regular expression, with the value for each field being retrieved from either of the 0-based group
 * position, match position, or group name.
 * <p>
 * This parser supports the following configuration properties (in addition to those supported by
 * {@link GenericActivityParser}):
 * <ul>
 * <li>Pattern - contains the regular expression pattern that each data item is assumed to match. (Required)</li>
 * <li>MatchStrategy - defines <tt>pattern</tt> created <tt>matcher</tt> comparison strategy used against input data
 * string. Value can be one of: {@code "MATCH"} - pattern should match complete input string, or {@code "FIND"} -
 * pattern has to match sub-sequence within input string. Default value - '{@code MATCH}'. (Optional)</li>
 * </ul>
 * <p>
 * This activity parser supports those activity field locator types:
 * <ul>
 * <li>{@link com.jkoolcloud.tnt4j.streams.fields.ActivityFieldLocatorType#Index}</li>
 * <li>{@link com.jkoolcloud.tnt4j.streams.fields.ActivityFieldLocatorType#Label}</li>
 * <li>{@link com.jkoolcloud.tnt4j.streams.fields.ActivityFieldLocatorType#REMatchId}</li>
 * <li>{@link com.jkoolcloud.tnt4j.streams.fields.ActivityFieldLocatorType#StreamProp}</li>
 * <li>{@link com.jkoolcloud.tnt4j.streams.fields.ActivityFieldLocatorType#Cache}</li>
 * <li>{@link com.jkoolcloud.tnt4j.streams.fields.ActivityFieldLocatorType#Activity}</li>
 * </ul>
 *
 * @version $Revision: 2 $
 */
public class ActivityRegExParser extends GenericActivityParser<Matcher> {
	private static final EventSink LOGGER = DefaultEventSinkFactory.defaultEventSink(ActivityRegExParser.class);

	private static final String MATCHES_KEY = "MATCHES_DATA"; // NON-NLS

	/**
	 * Contains the regular expression pattern that each data item is assumed to match (set by {@code Pattern}
	 * property).
	 */
	protected Pattern pattern = null;
	/**
	 * Defines strategy used to verify if {@link #pattern} created {@link Matcher} matches input data string.
	 */
	protected Strategy matchStrategy = Strategy.MATCH;

	/**
	 * Constructs a new ActivityRegExParser.
	 */
	public ActivityRegExParser() {
		super();
	}

	@Override
	protected EventSink logger() {
		return LOGGER;
	}

	@Override
	public void setProperties(Collection<Map.Entry<String, String>> props) {
		super.setProperties(props);

		if (CollectionUtils.isNotEmpty(props)) {
			for (Map.Entry<String, String> prop : props) {
				String name = prop.getKey();
				String value = prop.getValue();

				if (ParserProperties.PROP_PATTERN.equalsIgnoreCase(name)) {
					if (StringUtils.isNotEmpty(value)) {
						pattern = Pattern.compile(value);
						logger().log(OpLevel.DEBUG, StreamsResources.getBundle(StreamsResources.RESOURCE_BUNDLE_NAME),
								"ActivityParser.setting", name, value);
					}
				} else if (ParserProperties.PROP_MATCH_STRATEGY.equalsIgnoreCase(name)) {
					if (StringUtils.isNotEmpty(value)) {
						matchStrategy = Strategy.valueOf(value.toUpperCase());
						logger().log(OpLevel.DEBUG, StreamsResources.getBundle(StreamsResources.RESOURCE_BUNDLE_NAME),
								"ActivityParser.setting", name, value);
					}
				}
			}
		}
	}

	@Override
	public Object getProperty(String name) {
		if (ParserProperties.PROP_PATTERN.equalsIgnoreCase(name)) {
			return pattern;
		}
		if (ParserProperties.PROP_MATCH_STRATEGY.equalsIgnoreCase(name)) {
			return matchStrategy;
		}

		return super.getProperty(name);
	}

	@Override
	public ActivityInfo parse(TNTInputStream<?, ?> stream, Object data) throws IllegalStateException, ParseException {
		if (pattern == null || StringUtils.isEmpty(pattern.pattern())) {
			throw new IllegalStateException(StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME,
					"ActivityRegExParser.no.regex.pattern"));
		}

		return super.parse(stream, data);
	}

	@Override
	protected ActivityContext prepareItem(TNTInputStream<?, ?> stream, Object data) throws ParseException {
		String dataStr = getNextActivityString(data);
		if (StringUtils.isEmpty(dataStr)) {
			return null;
		}
		Matcher matcher = pattern.matcher(dataStr);
		if (matcher == null || !isMatching(matcher)) {
			logger().log(OpLevel.DEBUG, StreamsResources.getBundle(StreamsResources.RESOURCE_BUNDLE_NAME),
					"ActivityParser.input.not.match", getName(), pattern.pattern());
			return null;
		}

		ActivityContext cData = new ActivityContext(stream, data, matcher);
		cData.setMessage(dataStr);

		if (matchStrategy == Strategy.FIND) {
			Map<String, String> matches = findMatches(matcher);
			cData.put(MATCHES_KEY, matches);
		}

		return cData;
	}

	/**
	 * Checks if <tt>matcher</tt> matches input data string using parser defined matching strategy.
	 * 
	 * @param matcher
	 *            matcher instance to use
	 * @return {@code true} if matcher matches input data string using parser defined matching strategy
	 *
	 * @see java.util.regex.Matcher#find()
	 * @see java.util.regex.Matcher#matches()
	 */
	protected boolean isMatching(Matcher matcher) {
		boolean match;

		if (matchStrategy == Strategy.FIND) {
			match = matcher.find();
			matcher.reset();
		} else {
			match = matcher.matches();
		}

		return match;
	}

	/**
	 * Resolves all available RegEx matches found in activity data.
	 * <p>
	 * Found matches map key is match group index or name, value is matched activity data substring.
	 * 
	 * @param matcher
	 *            regex matcher to be used to find matches
	 * @return map of found matches
	 * @throws ParseException
	 *             if exception occurs while finding RegEx matches
	 *
	 * @see java.util.regex.Matcher#group(int)
	 */
	protected Map<String, String> findMatches(Matcher matcher) throws ParseException {
		try {
			Map<String, Integer> namedGroupsMap = getNamedGroups(pattern);
			Map<String, String> matches = new HashMap<>(10);

			matcher.reset();
			while (matcher.find()) {
				int gc = matcher.groupCount();
				for (int gi = 0; gi <= gc; gi++) {
					String matchStr = matcher.group(gi);
					if (matchStr != null) {
						addMatchEntry(matches, String.valueOf(gi), matchStr);

						if (MapUtils.isNotEmpty(namedGroupsMap)) {
							for (Map.Entry<String, Integer> namedGroup : namedGroupsMap.entrySet()) {
								if (gi == namedGroup.getValue()) {
									addMatchEntry(matches, namedGroup.getKey(), matchStr);
								}
							}
						}
					}
				}
			}
			logger().log(OpLevel.DEBUG, StreamsResources.getBundle(StreamsResources.RESOURCE_BUNDLE_NAME),
					"ActivityRegExParser.found.matches", matches.size());

			return matches;
		} catch (Exception e) {
			ParseException pe = new ParseException(StreamsResources.getStringFormatted(
					StreamsResources.RESOURCE_BUNDLE_NAME, "ActivityRegExParser.failed.parsing.matches"), 0);
			pe.initCause(e);
			throw pe;
		}
	}

	private void addMatchEntry(Map<String, String> matches, String matchKey, String matchStr) {
		matches.put(matchKey, matchStr);
		logger().log(OpLevel.TRACE, StreamsResources.getBundle(StreamsResources.RESOURCE_BUNDLE_NAME),
				"ActivityRegExParser.match", matchKey, matchStr);
	}

	/**
	 * Gets field raw data value resolved by locator.
	 *
	 * @param locator
	 *            activity field locator
	 * @param cData
	 *            RegEx data package - {@link Matcher} or {@link ArrayList} of matches
	 * @param formattingNeeded
	 *            flag to set if value formatting is not needed
	 * @return raw value resolved by locator, or {@code null} if value is not resolved
	 *
	 * @see java.util.regex.Matcher#group(int)
	 * @see java.util.regex.Matcher#group(String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected Object resolveLocatorValue(ActivityFieldLocator locator, ActivityContext cData,
			AtomicBoolean formattingNeeded) {
		Object val = null;
		String locStr = locator.getLocator();

		if (StringUtils.isNotEmpty(locStr)) {
			if (cData.containsKey(MATCHES_KEY)) {
				Map<String, String> matches = (Map<String, String>) cData.get(MATCHES_KEY);
				val = matches.get(locStr);
			} else {
				Matcher matcher = cData.getData();
				ActivityFieldLocatorType locType = locator.getBuiltInType();

				if (locType != null && locType.getDataType() == Integer.class) {
					val = groupIndex(locStr, matcher);
				} else if (locType != null && locType.getDataType() == String.class) {
					val = groupName(locStr, matcher);
				} else {
					try {
						val = groupIndex(locStr, matcher);
					} catch (NumberFormatException exc) {
						val = groupName(locStr, matcher);
					}
				}
			}
		}

		return val;
	}

	private static String groupIndex(String locStr, Matcher matcher) {
		int loc = Integer.parseInt(locStr);
		try {
			return matcher.group(loc);
		} catch (IndexOutOfBoundsException exc) {
			return null;
		}
	}

	private static String groupName(String locStr, Matcher matcher) {
		try {
			return matcher.group(locStr);
		} catch (NullPointerException | IllegalArgumentException exc) {
			return null;
		}
	}

	/**
	 * Retrieves named groups map from <tt>regexPattern</tt>.
	 * <p>
	 * Named groups map key is group name, value is group index.
	 * 
	 * @param regexPattern
	 *            pattern to get group names
	 * @return map of named groups, or {@code null} if no named groups defined
	 * @throws java.lang.Exception
	 *             if error occurs while retrieving named groups map from pattern
	 */
	@SuppressWarnings("unchecked")
	protected static Map<String, Integer> getNamedGroups(Pattern regexPattern) throws Exception {
		Method namedGroupsMethod = Pattern.class.getDeclaredMethod("namedGroups");
		namedGroupsMethod.setAccessible(true);

		Map<String, Integer> namedGroups = (Map<String, Integer>) namedGroupsMethod.invoke(regexPattern);

		return namedGroups == null ? null : Collections.unmodifiableMap(namedGroups);
	}

	private static final EnumSet<ActivityFieldLocatorType> UNSUPPORTED_LOCATOR_TYPES = EnumSet
			.of(ActivityFieldLocatorType.Range);

	/**
	 * {@inheritDoc}
	 * <p>
	 * Unsupported activity locator types are:
	 * <ul>
	 * <li>{@link com.jkoolcloud.tnt4j.streams.fields.ActivityFieldLocatorType#Range}</li>
	 * </ul>
	 */
	@Override
	protected EnumSet<ActivityFieldLocatorType> getUnsupportedLocatorTypes() {
		return UNSUPPORTED_LOCATOR_TYPES;
	}

	/**
	 * Strategies used to verify if pattern matches input data string.
	 */
	protected enum Strategy {
		/**
		 * Complete input data string matches defined pattern.
		 */
		MATCH,

		/**
		 * Part of input string matches defined pattern.
		 */
		FIND
	}
}
