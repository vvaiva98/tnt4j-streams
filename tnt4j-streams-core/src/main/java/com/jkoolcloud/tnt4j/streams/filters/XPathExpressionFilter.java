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

package com.jkoolcloud.tnt4j.streams.filters;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathVariableResolver;

import org.apache.commons.collections4.CollectionUtils;

import com.jkoolcloud.tnt4j.core.Property;
import com.jkoolcloud.tnt4j.sink.EventSink;
import com.jkoolcloud.tnt4j.streams.fields.AbstractFieldEntity;
import com.jkoolcloud.tnt4j.streams.fields.ActivityInfo;
import com.jkoolcloud.tnt4j.streams.utils.*;

/**
 * Data value filtering based on XPath function expressions.
 * 
 * @version $Revision: 1 $
 *
 * @see com.jkoolcloud.tnt4j.streams.utils.StreamsXMLUtils#getStreamsXPath()
 * @see XPathVariableResolver
 * @see javax.xml.xpath.XPathFunctionResolver
 * @see javax.xml.namespace.NamespaceContext
 * @see javax.xml.xpath.XPathFunction
 * @see XPath#evaluate(String, Object)
 */
public class XPathExpressionFilter extends AbstractExpressionFilter<Object> {
	private static final EventSink LOGGER = LoggerUtils.getLoggerSink(XPathExpressionFilter.class);

	private static final String OWN_FIELD_VALUE_KEY = "<!TNT4J_XPATH_FLTR_FLD_VALUE!>"; // NON-NLS;
	private static final String OWN_FIELD_NAME_KEY = "<!TNT4J_XPATH_FLTR_FLD_NAME!>"; // NON-NLS;

	/**
	 * Constructs a new XPathExpressionFilter. Handle type is set to
	 * {@link com.jkoolcloud.tnt4j.streams.filters.HandleType#INCLUDE}.
	 *
	 * @param filterExpression
	 *            filter expression string
	 */
	public XPathExpressionFilter(String filterExpression) {
		super(filterExpression);

		initFilter();
	}

	/**
	 * Constructs a new XPathExpressionFilter.
	 * 
	 * @param handleType
	 *            filter {@link com.jkoolcloud.tnt4j.streams.filters.HandleType} name
	 * @param filterExpression
	 *            filter expression string
	 */
	public XPathExpressionFilter(String handleType, String filterExpression) {
		super(handleType, filterExpression);

		initFilter();
	}

	@Override
	protected EventSink getLogger() {
		return LOGGER;
	}

	@Override
	protected String getHandledLanguage() {
		return StreamsScriptingUtils.XPATH_SCRIPT_LANG;
	}

	@Override
	public boolean doFilter(Object value, Map<String, ?> context) throws FilterException {
		Map<String, Object> valuesMap = new HashMap<>();
		valuesMap.put(OWN_FIELD_VALUE_KEY, value);
		AbstractFieldEntity field = context == null ? null
				: (AbstractFieldEntity) context.get(StreamsConstants.CTX_FIELD_KEY);
		valuesMap.put(OWN_FIELD_NAME_KEY, field == null ? null : field.getName());

		ActivityInfo ai = context == null ? null : (ActivityInfo) context.get(StreamsConstants.CTX_ACTIVITY_DATA_KEY);

		if (ai != null && CollectionUtils.isNotEmpty(exprVars)) {
			for (String eVar : exprVars) {
				Property eKV = resolveFieldKeyAndValue(eVar, ai);

				valuesMap.put(eKV.getKey(), eKV.getValue());
			}
		}

		return evaluate(valuesMap);
	}

	@Override
	public boolean doFilter(Map<String, ?> valBindings) throws FilterException {
		Map<String, Object> valuesMap = new HashMap<>();

		if (valBindings != null && CollectionUtils.isNotEmpty(exprVars)) {
			for (String eVar : exprVars) {
				Property eKV = resolveFieldKeyAndValue(eVar, valBindings);

				valuesMap.put(eKV.getKey(), eKV.getValue());
			}
		}

		return evaluate(valuesMap);
	}

	private boolean evaluate(Map<String, ?> valuesMap) throws FilterException {
		XPath xPath = StreamsXMLUtils.getStreamsXPath();
		xPath.setXPathVariableResolver(new StreamsVariableResolver(valuesMap));

		try {
			boolean match = "true".equals(xPath.evaluate(getExpression(), (Object) null)); // NON-NLS

			logEvaluationResult(valuesMap, match);

			return isFilteredOut(getHandleType(), match);
		} catch (Exception exc) {
			throw new FilterException(StreamsResources.getStringFormatted(StreamsResources.RESOURCE_BUNDLE_NAME,
					"ExpressionFilter.filtering.failed", filterExpression), exc);
		}
	}

	private static class StreamsVariableResolver implements XPathVariableResolver {
		private Map<String, ?> valuesMap;

		private StreamsVariableResolver(Map<String, ?> valuesMap) {
			this.valuesMap = valuesMap;
		}

		@Override
		public Object resolveVariable(QName variableName) {
			Object varValue;
			if (variableName.equals(new QName(StreamsScriptingUtils.FIELD_VALUE_VARIABLE_NAME))) {
				varValue = valuesMap.get(OWN_FIELD_VALUE_KEY);
			} else if (variableName.equals(new QName(StreamsScriptingUtils.FIELD_NAME_VARIABLE_NAME))) {
				varValue = valuesMap.get(OWN_FIELD_NAME_KEY);
			} else {
				String varNameStr = "$" + variableName.toString(); // NON-NLS

				varValue = valuesMap.get(varNameStr);
			}

			return varValue == null ? "" : varValue;
		}
	}
}
