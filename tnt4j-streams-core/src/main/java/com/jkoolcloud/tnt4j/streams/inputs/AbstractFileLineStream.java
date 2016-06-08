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

package com.jkoolcloud.tnt4j.streams.inputs;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.jkoolcloud.tnt4j.core.OpLevel;
import com.jkoolcloud.tnt4j.sink.EventSink;
import com.jkoolcloud.tnt4j.streams.configure.StreamProperties;
import com.jkoolcloud.tnt4j.streams.configure.state.AbstractFileStreamStateHandler;
import com.jkoolcloud.tnt4j.streams.fields.ActivityInfo;
import com.jkoolcloud.tnt4j.streams.parsers.ActivityParser;
import com.jkoolcloud.tnt4j.streams.utils.StreamsResources;

/**
 * <p>
 * Base class for files lines activity stream, where each line of the file is
 * assumed to represent a single activity or event which should be recorded.
 * Stream also can read changes from defined files every "FileReadDelay"
 * property defined seconds (default is 15sec.).
 * <p>
 * This activity stream requires parsers that can support {@link String} data.
 * <p>
 * This activity stream supports the following properties:
 * <ul>
 * <li>FileName - the system-dependent file name or file name pattern defined
 * using wildcard characters '*' and '?'. (Required)</li>
 * <li>FilePolling - flag {@code true}/{@code false} indicating whether files
 * should be polled for changes or not. If not, then files are read from oldest
 * to newest sequentially one single time. Default value - {@code false}.
 * (Optional)</li>
 * <li>StartFromLatest - flag {@code true}/{@code false} indicating that
 * streaming should be performed from latest file entry line. If {@code false} -
 * then all lines from available files are streamed on startup. Actual just if
 * 'FilePolling' property is set to {@code true}. Default value - {@code true}.
 * (Optional)</li>
 * <li>FileReadDelay - delay is seconds between file reading iterations. Actual
 * just if 'FilePolling' property is set to {@code true}. Default value - 15sec.
 * (Optional)</li>
 * <li>RestoreState - flag {@code true}/{@code false} indicating whether files
 * read state should be stored and restored on stream restart. Default value -
 * {@code true}. (Optional)</li>
 * </ul>
 *
 * @version $Revision: 2 $
 *
 * @see ActivityParser#isDataClassSupported(Object)
 */
public abstract class AbstractFileLineStream<T> extends AbstractBufferedStream<AbstractFileLineStream.Line> {
	private static final long DEFAULT_DELAY_PERIOD = TimeUnit.SECONDS.toMillis(15);

	/**
	 * Stream attribute defining file name.
	 */
	protected String fileName = null;

	/**
	 * Stream attribute defining if streaming should be performed from file
	 * position found on stream initialization. If {@code false} - then
	 * streaming is performed from beginning of the file.
	 */
	protected boolean startFromLatestActivity = true;

	private long fileWatcherDelay = DEFAULT_DELAY_PERIOD;

	private FileWatcher fileWatcher;
	private boolean pollingOn = false;

	/**
	 * File read state storing-restoring manager.
	 */
	protected AbstractFileStreamStateHandler<T> stateHandler;

	/**
	 * Stream attribute defining whether file read state should be stored and
	 * restored on stream restart.
	 */
	protected boolean storeState = true;

	/**
	 * Constructs a new AbstractFileLineStream.
	 *
	 * @param logger
	 *            logger used by activity stream
	 */
	protected AbstractFileLineStream(EventSink logger) {
		super(logger, 1);
	}

	@Override
	public void setProperties(Collection<Map.Entry<String, String>> props) throws Exception {
		if (props == null) {
			return;
		}

		super.setProperties(props);

		for (Map.Entry<String, String> prop : props) {
			String name = prop.getKey();
			String value = prop.getValue();
			if (StreamProperties.PROP_FILENAME.equalsIgnoreCase(name)) {
				fileName = value;
			} else if (StreamProperties.PROP_START_FROM_LATEST.equalsIgnoreCase(name)) {
				startFromLatestActivity = Boolean.parseBoolean(value);
			} else if (StreamProperties.PROP_FILE_READ_DELAY.equalsIgnoreCase(name)) {
				fileWatcherDelay = TimeUnit.SECONDS.toMillis(Long.parseLong(value));
			} else if (StreamProperties.PROP_FILE_POLLING.equalsIgnoreCase(name)) {
				pollingOn = Boolean.parseBoolean(value);
			} else if (StreamProperties.PROP_RESTORE_STATE.equalsIgnoreCase(name)) {
				storeState = Boolean.parseBoolean(value);
			}
		}
	}

	@Override
	public Object getProperty(String name) {
		if (StreamProperties.PROP_FILENAME.equalsIgnoreCase(name)) {
			return fileName;
		}
		if (StreamProperties.PROP_START_FROM_LATEST.equalsIgnoreCase(name)) {
			return startFromLatestActivity;
		}
		if (StreamProperties.PROP_FILE_READ_DELAY.equalsIgnoreCase(name)) {
			return fileWatcherDelay;
		}
		if (StreamProperties.PROP_FILE_POLLING.equalsIgnoreCase(name)) {
			return pollingOn;
		}
		if (StreamProperties.PROP_RESTORE_STATE.equalsIgnoreCase(name)) {
			return storeState;
		}
		return super.getProperty(name);
	}

	@Override
	public void initialize() throws Exception {
		super.initialize();

		if (StringUtils.isEmpty(fileName)) {
			throw new IllegalStateException(StreamsResources.getStringFormatted(StreamsResources.RESOURCE_BUNDLE_NAME,
					"TNTInputStream.property.undefined", StreamProperties.PROP_FILENAME));
		}

		if (!pollingOn) {
			startFromLatestActivity = false;
		}

		logger.log(OpLevel.DEBUG,
				StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME, "FileLineStream.initializing.stream"),
				fileName);

		fileWatcher = createFileWatcher();
		fileWatcher.initialize();
		fileWatcher.start();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method returns total lines count in all streamed files.
	 */
	@Override
	public int getTotalActivities() {
		return fileWatcher.totalLinesCount;
	}

	@Override
	public long getTotalBytes() {
		return fileWatcher.totalBytesCount;
	}

	@Override
	protected void cleanup() {
		fileWatcher.shutdown();

		super.cleanup();
	}

	@Override
	public Line getNextItem() throws Exception {
		final Line nextItem = super.getNextItem();
		if (stateHandler != null) {
			stateHandler.saveState(nextItem, getName());
		}

		return nextItem;
	}

	/**
	 * Constructs a new file watcher instance specific for this stream.
	 *
	 * @return file watcher instance
	 */
	protected abstract FileWatcher createFileWatcher();

	@Override
	protected boolean isInputEnded() {
		return fileWatcher.isInputEnded();
	}

	@Override
	protected long getActivityItemByteSize(Line activityItem) {
		return activityItem == null || activityItem.text == null ? 0 : activityItem.text.getBytes().length;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method returns line number of the file last read.
	 */
	@Override
	public int getActivityPosition() {
		return fileWatcher == null ? 0 : fileWatcher.lineNumber;
	}

	/**
	 * Base class containing common file watcher features.
	 */
	protected abstract class FileWatcher extends InputProcessor {

		protected T fileToRead = null;

		protected T[] availableFiles;

		/**
		 * File monitor attribute storing line number marker of streamed file.
		 */
		protected int lineNumber = -1;

		/**
		 * File monitor attribute storing modification time of streamed file.
		 */
		protected long lastModifTime = -1;

		/**
		 * Total bytes count available to stream.
		 */
		protected int totalBytesCount = 0;
		/**
		 * Total lines count available to stream.
		 */
		protected int totalLinesCount = 0;

		/**
		 * Constructs a new FileWatcher.
		 *
		 * @param name
		 *            the name of file watcher thread
		 */
		FileWatcher(String name) {
			super(name);
		}

		/**
		 * Initializes file watcher thread. Picks file matching user defined
		 * file name to monitor. If user defined to start streaming from latest
		 * file line then count of lines in file is calculated to mark latest
		 * activity position.
		 *
		 * @throws Exception
		 *             indicates that stream is not configured properly and
		 *             files monitoring can't initialize and continue.
		 */
		protected abstract void initialize() throws Exception;

		/**
		 * Performs continuous file monitoring until stream thread is halted or
		 * monitoring is interrupted. File monitoring is performed with
		 * {@link #fileWatcherDelay} defined delays between iterations.
		 */
		@Override
		public void run() {
			while (!isStopping()) {
				readFileChanges();

				if (isReadingLatestFile() && !isStopping()) {
					if (!pollingOn) {
						shutdown();
					} else {
						try {
							logger.log(OpLevel.DEBUG, StreamsResources.getString(StreamsResources.RESOURCE_BUNDLE_NAME,
									"FileLineStream.waiting"), fileWatcherDelay / 1000.0);
							Thread.sleep(fileWatcherDelay);
						} catch (InterruptedException exc) {
						}
					}
				}
			}
		}

		private boolean isReadingLatestFile() {
			return fileToRead == null || ArrayUtils.isEmpty(availableFiles) ? true
					: fileToRead.equals(availableFiles[availableFiles.length - 1]);
		}

		/**
		 * Performs file changes reading.
		 */
		protected abstract void readFileChanges();

		/**
		 * Reads new file lines and adds them to changed lines buffer.
		 *
		 * @param lnr
		 *            line number reader
		 * @throws IOException
		 *             if error occurs when reading file line
		 */
		protected void readNewFileLines(LineNumberReader lnr) throws IOException {
			String line;
			while ((line = lnr.readLine()) != null && !isInputEnded()) {
				lineNumber = lnr.getLineNumber();
				if (StringUtils.isNotEmpty(line)) {
					addInputToBuffer(new Line(line, lineNumber));
				}
			}
		}

		/**
		 * Sets currently read file.
		 *
		 * @param file
		 *            file to read
		 */
		protected void setFileToRead(T file) {
			this.fileToRead = file;

			if (stateHandler != null) {
				stateHandler.setStreamedFile(file);
			}
		}

		@Override
		void close() throws Exception {
			super.close();

			if (stateHandler != null && fileToRead != null) {
				stateHandler.writeState(fileToRead instanceof File ? ((File) fileToRead).getParentFile() : null,
						AbstractFileLineStream.this.getName());
			}
		}
	}

	@Override
	protected ActivityInfo applyParsers(String[] tags, Object data) throws IllegalStateException, ParseException {
		return super.applyParsers(tags, data instanceof Line ? ((Line) data).text : data);
	}

	/**
	 * File line data package defining line text string and line number in file.
	 */
	public static class Line {
		private String text;
		private int lineNr;

		/**
		 * Creates a new Line.
		 *
		 * @param text
		 *            line text string
		 * @param lineNumber
		 *            line number in file
		 */
		public Line(String text, int lineNumber) {
			this.text = text;
			this.lineNr = lineNumber;
		}

		/**
		 * Returns file line text string.
		 *
		 * @return file line text string
		 */
		public String getText() {
			return text;
		}

		/**
		 * Returns line number in file.
		 *
		 * @return line number in file
		 */
		public int getLineNumber() {
			return lineNr;
		}

		@Override
		public String toString() {
			return text;
		}
	}
}
