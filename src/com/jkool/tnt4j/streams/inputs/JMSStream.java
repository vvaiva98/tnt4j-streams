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

package com.jkool.tnt4j.streams.inputs;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.jkool.tnt4j.streams.configure.StreamsConfig;
import com.nastel.jkool.tnt4j.core.OpLevel;
import com.nastel.jkool.tnt4j.sink.DefaultEventSinkFactory;
import com.nastel.jkool.tnt4j.sink.EventSink;

/**
 * <p>
 * Implements a JMS message transported activity stream, where each JMS message
 * carries single {@code TextMessage} activity object data or line of the file
 * is assumed to represent a single activity or event which should be recorded.
 * TODO
 *
 * </p>
 * <p>
 * This activity stream requires parsers that can support {@code String} data.
 * </p>
 * This activity stream supports the following properties:
 * <ul>
 * <li>ServerURI - JMS server URL</li>
 * <li>Queue - queue name</li>
 * <li>JNDIFactory - JNDI factory name</li>
 * <li>JMSFactory - JMS factory name</li>
 * </ul>
 *
 * @version $Revision: 1$
 *
 * @see com.jkool.tnt4j.streams.parsers.ActivityParser#isDataClassSupported(Object)
 */
public class JMSStream extends AbstractBufferedStream<Message> {
	private static final EventSink LOGGER = DefaultEventSinkFactory.defaultEventSink(JMSStream.class);

	// Stream properties
	private String serverURL = null;
	private String queueName = null;
	private String jndiFactory = null;
	private String jmsFactory = null;

	private JMSDataReceiver jmsDataReceiver;

	/**
	 * Construct empty JMSStream. Requires configuration settings to set input
	 * stream source.
	 */
	public JMSStream() {
		super(LOGGER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getProperty(String name) {
		if (StreamsConfig.PROP_SERVER_URI.equalsIgnoreCase(name)) {
			return serverURL;
		}
		if (StreamsConfig.PROP_QUEUE_NAME.equalsIgnoreCase(name)) {
			return queueName;
		}
		if (StreamsConfig.PROP_JNDI_FACTORY.equalsIgnoreCase(name)) {
			return jndiFactory;
		}
		if (StreamsConfig.PROP_JMS_FACTORY.equalsIgnoreCase(name)) {
			return jmsFactory;
		}

		return super.getProperty(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProperties(Collection<Map.Entry<String, String>> props) throws Throwable {
		if (props == null) {
			return;
		}

		super.setProperties(props);

		for (Map.Entry<String, String> prop : props) {
			String name = prop.getKey();
			String value = prop.getValue();
			if (StreamsConfig.PROP_SERVER_URI.equalsIgnoreCase(name)) {
				serverURL = value;
			} else if (StreamsConfig.PROP_QUEUE_NAME.equalsIgnoreCase(name)) {
				queueName = value;
			} else if (StreamsConfig.PROP_JNDI_FACTORY.equalsIgnoreCase(name)) {
				jndiFactory = value;
			} else if (StreamsConfig.PROP_JMS_FACTORY.equalsIgnoreCase(name)) {
				jmsFactory = value;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialize() throws Throwable {
		super.initialize();

		jmsDataReceiver = new JMSDataReceiver();
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, jndiFactory);
		env.put(Context.PROVIDER_URL, serverURL);

		Context ic = new InitialContext(env);

		jmsDataReceiver.initialize(ic, queueName, jmsFactory);

		LOGGER.log(OpLevel.DEBUG, "TNT4J-Streams JMS stream ready to receive messages");

		jmsDataReceiver.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void cleanup() {
		jmsDataReceiver.shutdown();

		super.cleanup();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isInputEnded() {
		return jmsDataReceiver.isInputEnded();
	}

	private class JMSDataReceiver extends InputProcessor implements MessageListener {

		private QueueConnectionFactory qConFactory;
		private QueueConnection qCon;
		private QueueSession qSession;
		private QueueReceiver qReceiver;
		private Queue queue;

		private JMSDataReceiver() {

			super("JMSStream.JMSDataReceiver"); // NON-NLS
		}

		private void initialize(Context ctx, String queueName, String jmsFactory) throws NamingException, JMSException {
			qConFactory = (QueueConnectionFactory) ctx.lookup(jmsFactory);
			qCon = qConFactory.createQueueConnection();
			qSession = qCon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = (Queue) ctx.lookup(queueName);
			qReceiver = qSession.createReceiver(queue);
			qReceiver.setMessageListener(this);
			qCon.start();
		}

		/**
		 * Message listener interface. TODO
		 *
		 * @param msg
		 *            message
		 */
		@Override
		public void onMessage(Message msg) {
			if (msg == null) {
				return;
			}

			addInputToBuffer(msg);
		}

		/**
		 * Closes JMS objects.
		 *
		 * @throws JMSException
		 *             if JMS fails to close objects due to internal error
		 */
		void close() throws Exception {
			qReceiver.close();
			qSession.close();
			qCon.close();

			super.close();
		}
	}
}
