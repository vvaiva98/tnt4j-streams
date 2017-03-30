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

package com.jkoolcloud.tnt4j.streams.inputs;

/**
 * Defines supported streaming process status values.
 *
 * @version $Revision: 1 $
 */
public enum StreamStatus implements StreamingStatus {
	/**
	 * Indicates streaming process status is new/initialized.
	 */
	NEW,

	/**
	 * Indicates streaming process status is started.
	 */
	STARTED,

	/**
	 * Indicates streaming process finished successfully.
	 */
	SUCCESS,

	/**
	 * Indicates streaming process finished with failure.
	 */
	FAILURE,

	/**
	 * Indicates streaming process was stopped from stream outside.
	 */
	STOP,
}
