// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: zipkin/proto3/zipkin.proto

package zipkin2.proto3;

public interface SpanOrBuilder extends
    // @@protoc_insertion_point(interface_extends:zipkin.proto3.Span)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Randomly generated, unique identifier for a trace, set on all spans within
   * it.
   * This field is required and encoded as 8 or 16 bytes, in big endian byte
   * order.
   * </pre>
   *
   * <code>bytes trace_id = 1;</code>
   * @return The traceId.
   */
  com.google.protobuf.ByteString getTraceId();

  /**
   * <pre>
   * The parent span ID or absent if this the root span in a trace.
   * </pre>
   *
   * <code>bytes parent_id = 2;</code>
   * @return The parentId.
   */
  com.google.protobuf.ByteString getParentId();

  /**
   * <pre>
   * Unique identifier for this operation within the trace.
   * This field is required and encoded as 8 opaque bytes.
   * </pre>
   *
   * <code>bytes id = 3;</code>
   * @return The id.
   */
  com.google.protobuf.ByteString getId();

  /**
   * <pre>
   * When present, used to interpret remote_endpoint
   * </pre>
   *
   * <code>.zipkin.proto3.Span.Kind kind = 4;</code>
   * @return The enum numeric value on the wire for kind.
   */
  int getKindValue();
  /**
   * <pre>
   * When present, used to interpret remote_endpoint
   * </pre>
   *
   * <code>.zipkin.proto3.Span.Kind kind = 4;</code>
   * @return The kind.
   */
  zipkin2.proto3.Span.Kind getKind();

  /**
   * <pre>
   * The logical operation this span represents in lowercase (e.g. rpc method).
   * Leave absent if unknown.
   * As these are lookup labels, take care to ensure names are low cardinality.
   * For example, do not embed variables into the name.
   * </pre>
   *
   * <code>string name = 5;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <pre>
   * The logical operation this span represents in lowercase (e.g. rpc method).
   * Leave absent if unknown.
   * As these are lookup labels, take care to ensure names are low cardinality.
   * For example, do not embed variables into the name.
   * </pre>
   *
   * <code>string name = 5;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <pre>
   * Epoch microseconds of the start of this span, possibly absent if
   * incomplete.
   * For example, 1502787600000000 corresponds to 2017-08-15 09:00 UTC
   * This value should be set directly by instrumentation, using the most
   * precise value possible. For example, gettimeofday or multiplying epoch
   * millis by 1000.
   * There are three known edge-cases where this could be reported absent.
   * - A span was allocated but never started (ex not yet received a timestamp)
   * - The span's start event was lost
   * - Data about a completed span (ex tags) were sent after the fact
   * </pre>
   *
   * <code>fixed64 timestamp = 6;</code>
   * @return The timestamp.
   */
  long getTimestamp();

  /**
   * <pre>
   * Duration in microseconds of the critical path, if known. Durations of less
   * than one are rounded up. Duration of children can be longer than their
   * parents due to asynchronous operations.
   * For example 150 milliseconds is 150000 microseconds.
   * </pre>
   *
   * <code>uint64 duration = 7;</code>
   * @return The duration.
   */
  long getDuration();

  /**
   * <pre>
   * The host that recorded this span, primarily for query by service name.
   * Instrumentation should always record this. Usually, absent implies late
   * data. The IP address corresponding to this is usually the site local or
   * advertised service address. When present, the port indicates the listen
   * port.
   * </pre>
   *
   * <code>.zipkin.proto3.Endpoint local_endpoint = 8;</code>
   * @return Whether the localEndpoint field is set.
   */
  boolean hasLocalEndpoint();
  /**
   * <pre>
   * The host that recorded this span, primarily for query by service name.
   * Instrumentation should always record this. Usually, absent implies late
   * data. The IP address corresponding to this is usually the site local or
   * advertised service address. When present, the port indicates the listen
   * port.
   * </pre>
   *
   * <code>.zipkin.proto3.Endpoint local_endpoint = 8;</code>
   * @return The localEndpoint.
   */
  zipkin2.proto3.Endpoint getLocalEndpoint();
  /**
   * <pre>
   * The host that recorded this span, primarily for query by service name.
   * Instrumentation should always record this. Usually, absent implies late
   * data. The IP address corresponding to this is usually the site local or
   * advertised service address. When present, the port indicates the listen
   * port.
   * </pre>
   *
   * <code>.zipkin.proto3.Endpoint local_endpoint = 8;</code>
   */
  zipkin2.proto3.EndpointOrBuilder getLocalEndpointOrBuilder();

  /**
   * <pre>
   * When an RPC (or messaging) span, indicates the other side of the
   * connection.
   * By recording the remote endpoint, your trace will contain network context
   * even if the peer is not tracing. For example, you can record the IP from
   * the "X-Forwarded-For" header or the service name and socket of a remote
   * peer.
   * </pre>
   *
   * <code>.zipkin.proto3.Endpoint remote_endpoint = 9;</code>
   * @return Whether the remoteEndpoint field is set.
   */
  boolean hasRemoteEndpoint();
  /**
   * <pre>
   * When an RPC (or messaging) span, indicates the other side of the
   * connection.
   * By recording the remote endpoint, your trace will contain network context
   * even if the peer is not tracing. For example, you can record the IP from
   * the "X-Forwarded-For" header or the service name and socket of a remote
   * peer.
   * </pre>
   *
   * <code>.zipkin.proto3.Endpoint remote_endpoint = 9;</code>
   * @return The remoteEndpoint.
   */
  zipkin2.proto3.Endpoint getRemoteEndpoint();
  /**
   * <pre>
   * When an RPC (or messaging) span, indicates the other side of the
   * connection.
   * By recording the remote endpoint, your trace will contain network context
   * even if the peer is not tracing. For example, you can record the IP from
   * the "X-Forwarded-For" header or the service name and socket of a remote
   * peer.
   * </pre>
   *
   * <code>.zipkin.proto3.Endpoint remote_endpoint = 9;</code>
   */
  zipkin2.proto3.EndpointOrBuilder getRemoteEndpointOrBuilder();

  /**
   * <pre>
   * Associates events that explain latency with the time they happened.
   * </pre>
   *
   * <code>repeated .zipkin.proto3.Annotation annotations = 10;</code>
   */
  java.util.List<zipkin2.proto3.Annotation> 
      getAnnotationsList();
  /**
   * <pre>
   * Associates events that explain latency with the time they happened.
   * </pre>
   *
   * <code>repeated .zipkin.proto3.Annotation annotations = 10;</code>
   */
  zipkin2.proto3.Annotation getAnnotations(int index);
  /**
   * <pre>
   * Associates events that explain latency with the time they happened.
   * </pre>
   *
   * <code>repeated .zipkin.proto3.Annotation annotations = 10;</code>
   */
  int getAnnotationsCount();
  /**
   * <pre>
   * Associates events that explain latency with the time they happened.
   * </pre>
   *
   * <code>repeated .zipkin.proto3.Annotation annotations = 10;</code>
   */
  java.util.List<? extends zipkin2.proto3.AnnotationOrBuilder> 
      getAnnotationsOrBuilderList();
  /**
   * <pre>
   * Associates events that explain latency with the time they happened.
   * </pre>
   *
   * <code>repeated .zipkin.proto3.Annotation annotations = 10;</code>
   */
  zipkin2.proto3.AnnotationOrBuilder getAnnotationsOrBuilder(
      int index);

  /**
   * <pre>
   * Tags give your span context for search, viewing and analysis.
   * For example, a key "your_app.version" would let you lookup traces by
   * version. A tag "sql.query" isn't searchable, but it can help in debugging
   * when viewing a trace.
   * </pre>
   *
   * <code>map&lt;string, string&gt; tags = 11;</code>
   */
  int getTagsCount();
  /**
   * <pre>
   * Tags give your span context for search, viewing and analysis.
   * For example, a key "your_app.version" would let you lookup traces by
   * version. A tag "sql.query" isn't searchable, but it can help in debugging
   * when viewing a trace.
   * </pre>
   *
   * <code>map&lt;string, string&gt; tags = 11;</code>
   */
  boolean containsTags(
      java.lang.String key);
  /**
   * Use {@link #getTagsMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.String>
  getTags();
  /**
   * <pre>
   * Tags give your span context for search, viewing and analysis.
   * For example, a key "your_app.version" would let you lookup traces by
   * version. A tag "sql.query" isn't searchable, but it can help in debugging
   * when viewing a trace.
   * </pre>
   *
   * <code>map&lt;string, string&gt; tags = 11;</code>
   */
  java.util.Map<java.lang.String, java.lang.String>
  getTagsMap();
  /**
   * <pre>
   * Tags give your span context for search, viewing and analysis.
   * For example, a key "your_app.version" would let you lookup traces by
   * version. A tag "sql.query" isn't searchable, but it can help in debugging
   * when viewing a trace.
   * </pre>
   *
   * <code>map&lt;string, string&gt; tags = 11;</code>
   */

  /* nullable */
java.lang.String getTagsOrDefault(
      java.lang.String key,
      /* nullable */
java.lang.String defaultValue);
  /**
   * <pre>
   * Tags give your span context for search, viewing and analysis.
   * For example, a key "your_app.version" would let you lookup traces by
   * version. A tag "sql.query" isn't searchable, but it can help in debugging
   * when viewing a trace.
   * </pre>
   *
   * <code>map&lt;string, string&gt; tags = 11;</code>
   */

  java.lang.String getTagsOrThrow(
      java.lang.String key);

  /**
   * <pre>
   * True is a request to store this span even if it overrides sampling policy.
   * This is true when the "X-B3-Flags" header has a value of 1.
   * </pre>
   *
   * <code>bool debug = 12;</code>
   * @return The debug.
   */
  boolean getDebug();

  /**
   * <pre>
   * True if we are contributing to a span started by another tracer (ex on a
   * different host).
   * </pre>
   *
   * <code>bool shared = 13;</code>
   * @return The shared.
   */
  boolean getShared();
}
