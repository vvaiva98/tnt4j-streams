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

package com.jkoolcloud.tnt4j.streams.configure;

/**
 * Lists predefined property names used by TNT4-Streams WS input streams.
 *
 * @version $Revision: 1 $
 */
public interface WsStreamProperties extends StreamProperties {
	/**
	 * Constant for name of built-in stream {@value} property.
	 */
	public static final String PROP_SECURITY_RESPONSE_PARSER_TAG = "SecurityResponseParserTag"; // NON-NLS
	/**
	 * Constant for name of built-in stream {@value} property.
	 */
	public static final String PROP_SECURITY_CACHED_TOKEN_KEY = "SecurityCachedTokenKey"; // NON-NLS
	/**
	 * Constant for name of built-in stream {@value} property.
	 */
	public static final String PROP_DISABLE_SSL = "DisableSSL"; // NON-NLS
	/**
	 * Constant for name of built-in stream {@value} property.
	 */
	public static final String PROP_SYNCHRONIZE_REQUESTS = "SynchronizeRequests"; // NON-NLS
}
