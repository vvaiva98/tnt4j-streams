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

package com.jkoolcloud.tnt4j.streams.outputs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jkoolcloud.tnt4j.core.Snapshot;
import com.jkoolcloud.tnt4j.core.Trackable;
import com.jkoolcloud.tnt4j.sink.DefaultEventSinkFactory;
import com.jkoolcloud.tnt4j.sink.EventSink;
import com.jkoolcloud.tnt4j.source.DefaultSourceFactory;
import com.jkoolcloud.tnt4j.source.Source;
import com.jkoolcloud.tnt4j.source.SourceFactory;
import com.jkoolcloud.tnt4j.streams.configure.OutputProperties;
import com.jkoolcloud.tnt4j.streams.fields.ActivityInfo;
import com.jkoolcloud.tnt4j.tracker.Tracker;
import com.jkoolcloud.tnt4j.tracker.TrackingActivity;
import com.jkoolcloud.tnt4j.tracker.TrackingEvent;

/**
 * Implements TNT4J-Streams output logger for activities provided as {@link ActivityInfo} entities to be recorded to
 * JKool Cloud over TNT4J and JESL APIs.
 * <p>
 * This output supports the following configuration properties (in addition to those supported by
 * {@link com.jkoolcloud.tnt4j.streams.outputs.AbstractJKCloudOutput}):
 * <ul>
 * <li>ResolveServerFromDNS - flag indicating whether to resolve activity entity host name/IP from DNS server. Default
 * value - {@code false}. (Optional)</li>
 * <li>TurnOutActivityChildren - flag indicating whether to send activity entity child entities independently merging
 * data from both parent and child entity fields. Default value - {@code false}. (Optional)</li>
 * <li>BuildSourceFQNFromStreamedData - flag indicating whether to set streamed activity entity {@link Source} FQN build
 * from activity fields data instead of default on configured in 'tnt4j.properties'. Default value - {@code true}.
 * (Optional)</li>
 * <li>SourceFQN - {@link Source} FQN pattern to be used when building it from streamed activity entity fields values.
 * Format is: SourceType1=${FieldName1}#SourceType2=${FieldName2}#SourceType3=${FieldName3}... . Default value -
 * 'APPL=${ApplName}#USER=${UserName}#SERVER=${ServerName}#NETADDR=${ServerIp}#GEOADDR=${Location}'. (Optional)</li>
 * </ul>
 *
 * @version $Revision: 1 $
 *
 * @see ActivityInfo#buildTrackable(com.jkoolcloud.tnt4j.tracker.Tracker, java.util.Collection)
 */
public class JKCloudActivityOutput extends AbstractJKCloudOutput<ActivityInfo, Trackable> {
	private static final EventSink LOGGER = DefaultEventSinkFactory.defaultEventSink(JKCloudActivityOutput.class);
	private static final String DEFAULT_SOURCE_FQN = "APPL=${ApplName}#SERVER=${ServerName}#NETADDR=${ServerIp}#GEOADDR=${Location}";

	private boolean resolveServer = false;
	private boolean turnOutActivityChildren = false;
	private boolean buildFQNFromData = true;
	private String sourceFQN = null;

	/**
	 * Constructs a new JKCloudActivityOutput.
	 */
	public JKCloudActivityOutput() {
		super();
	}

	@Override
	protected EventSink logger() {
		return LOGGER;
	}

	@Override
	public void setProperty(String name, Object value) {
		super.setProperty(name, value);

		if (OutputProperties.PROP_RESOLVE_SERVER.equalsIgnoreCase(name)) {
			resolveServer = Boolean.parseBoolean((String) value);
		} else if (OutputProperties.PROP_TURN_OUT_CHILDREN.equalsIgnoreCase(name)) {
			turnOutActivityChildren = Boolean.parseBoolean((String) value);
		} else if (OutputProperties.PROP_BUILD_FQN_FROM_DATA.equalsIgnoreCase(name)) {
			buildFQNFromData = Boolean.parseBoolean((String) value);
		} else if (OutputProperties.PROP_SOURCE_FQN.equalsIgnoreCase(name)) {
			sourceFQN = (String) value;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * @see ActivityInfo#buildTrackable(com.jkoolcloud.tnt4j.tracker.Tracker, java.util.Collection)
	 */
	@Override
	public void logItem(ActivityInfo ai) throws Exception {
		Tracker tracker = getTracker();
		ai.resolveServer(resolveServer);
		String aiFQN = buildFQNFromData ? StringUtils.isEmpty(sourceFQN) ? DEFAULT_SOURCE_FQN : sourceFQN : null;

		if (turnOutActivityChildren && ai.hasChildren()) {
			for (ActivityInfo cai : ai.getChildren()) {
				cai.merge(ai);
				Trackable t = cai.buildTrackable(tracker);
				alterTrackableSource(tracker, t, cai, aiFQN);
				recordActivity(tracker, CONN_RETRY_INTERVAL, t);
			}
		} else {
			List<Trackable> chTrackables = new ArrayList<>();
			Trackable t = ai.buildTrackable(tracker, chTrackables);
			alterTrackableSource(tracker, t, ai, aiFQN);
			recordActivity(tracker, CONN_RETRY_INTERVAL, t);

			for (int i = 0; i < chTrackables.size(); i++) {
				Trackable chT = chTrackables.get(i);
				ActivityInfo cai = ai.getChildren().get(i);
				alterTrackableSource(tracker, chT, cai, aiFQN);
				recordActivity(tracker, CONN_RETRY_INTERVAL, chT);
			}
		}
	}

	private static void alterTrackableSource(Tracker tracker, Trackable t, ActivityInfo ai, String fqn) {
		if (StringUtils.isNotEmpty(fqn)) {
			t.setSource(buildSource(tracker, ai.getSourceFQN(fqn)));
		}
	}

	private static Source buildSource(Tracker tracker, String sourceFQN) {
		if (StringUtils.isEmpty(sourceFQN)) {
			return null;
		}
		SourceFactory sf = tracker == null ? DefaultSourceFactory.getInstance()
				: tracker.getConfiguration().getSourceFactory();
		Source source = sf.newFromFQN(sourceFQN);
		source.setSSN(sf.getSSN());

		return source;

	}

	@Override
	protected void logJKCActivity(Tracker tracker, Trackable trackable) {
		if (trackable instanceof TrackingActivity) {
			tracker.tnt((TrackingActivity) trackable);
		} else if (trackable instanceof Snapshot) {
			tracker.tnt((Snapshot) trackable);
		} else {
			tracker.tnt((TrackingEvent) trackable);
		}
	}

	@Override
	public Trackable formatStreamStatusMessage(TrackingEvent statusMessage) {
		return statusMessage;
	}
}
