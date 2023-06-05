// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: opentelemetry/proto/trace/v1/trace.proto

package io.opentelemetry.proto.trace.v1;

public final class TraceProto {
  private TraceProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_opentelemetry_proto_trace_v1_TracesData_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_opentelemetry_proto_trace_v1_TracesData_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_opentelemetry_proto_trace_v1_ResourceSpans_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_opentelemetry_proto_trace_v1_ResourceSpans_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_opentelemetry_proto_trace_v1_ScopeSpans_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_opentelemetry_proto_trace_v1_ScopeSpans_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_opentelemetry_proto_trace_v1_Span_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_opentelemetry_proto_trace_v1_Span_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_opentelemetry_proto_trace_v1_Span_Event_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_opentelemetry_proto_trace_v1_Span_Event_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_opentelemetry_proto_trace_v1_Span_Link_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_opentelemetry_proto_trace_v1_Span_Link_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_opentelemetry_proto_trace_v1_Status_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_opentelemetry_proto_trace_v1_Status_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n(opentelemetry/proto/trace/v1/trace.pro" +
      "to\022\034opentelemetry.proto.trace.v1\032*opente" +
      "lemetry/proto/common/v1/common.proto\032.op" +
      "entelemetry/proto/resource/v1/resource.p" +
      "roto\"Q\n\nTracesData\022C\n\016resource_spans\030\001 \003" +
      "(\0132+.opentelemetry.proto.trace.v1.Resour" +
      "ceSpans\"\247\001\n\rResourceSpans\022;\n\010resource\030\001 " +
      "\001(\0132).opentelemetry.proto.resource.v1.Re" +
      "source\022=\n\013scope_spans\030\002 \003(\0132(.openteleme" +
      "try.proto.trace.v1.ScopeSpans\022\022\n\nschema_" +
      "url\030\003 \001(\tJ\006\010\350\007\020\351\007\"\227\001\n\nScopeSpans\022B\n\005scop" +
      "e\030\001 \001(\01323.opentelemetry.proto.common.v1." +
      "InstrumentationScope\0221\n\005spans\030\002 \003(\0132\".op" +
      "entelemetry.proto.trace.v1.Span\022\022\n\nschem" +
      "a_url\030\003 \001(\t\"\346\007\n\004Span\022\020\n\010trace_id\030\001 \001(\014\022\017" +
      "\n\007span_id\030\002 \001(\014\022\023\n\013trace_state\030\003 \001(\t\022\026\n\016" +
      "parent_span_id\030\004 \001(\014\022\014\n\004name\030\005 \001(\t\0229\n\004ki" +
      "nd\030\006 \001(\0162+.opentelemetry.proto.trace.v1." +
      "Span.SpanKind\022\034\n\024start_time_unix_nano\030\007 " +
      "\001(\006\022\032\n\022end_time_unix_nano\030\010 \001(\006\022;\n\nattri" +
      "butes\030\t \003(\0132\'.opentelemetry.proto.common" +
      ".v1.KeyValue\022 \n\030dropped_attributes_count" +
      "\030\n \001(\r\0228\n\006events\030\013 \003(\0132(.opentelemetry.p" +
      "roto.trace.v1.Span.Event\022\034\n\024dropped_even" +
      "ts_count\030\014 \001(\r\0226\n\005links\030\r \003(\0132\'.opentele" +
      "metry.proto.trace.v1.Span.Link\022\033\n\023droppe" +
      "d_links_count\030\016 \001(\r\0224\n\006status\030\017 \001(\0132$.op" +
      "entelemetry.proto.trace.v1.Status\032\214\001\n\005Ev" +
      "ent\022\026\n\016time_unix_nano\030\001 \001(\006\022\014\n\004name\030\002 \001(" +
      "\t\022;\n\nattributes\030\003 \003(\0132\'.opentelemetry.pr" +
      "oto.common.v1.KeyValue\022 \n\030dropped_attrib" +
      "utes_count\030\004 \001(\r\032\235\001\n\004Link\022\020\n\010trace_id\030\001 " +
      "\001(\014\022\017\n\007span_id\030\002 \001(\014\022\023\n\013trace_state\030\003 \001(" +
      "\t\022;\n\nattributes\030\004 \003(\0132\'.opentelemetry.pr" +
      "oto.common.v1.KeyValue\022 \n\030dropped_attrib" +
      "utes_count\030\005 \001(\r\"\231\001\n\010SpanKind\022\031\n\025SPAN_KI" +
      "ND_UNSPECIFIED\020\000\022\026\n\022SPAN_KIND_INTERNAL\020\001" +
      "\022\024\n\020SPAN_KIND_SERVER\020\002\022\024\n\020SPAN_KIND_CLIE" +
      "NT\020\003\022\026\n\022SPAN_KIND_PRODUCER\020\004\022\026\n\022SPAN_KIN" +
      "D_CONSUMER\020\005\"\256\001\n\006Status\022\017\n\007message\030\002 \001(\t" +
      "\022=\n\004code\030\003 \001(\0162/.opentelemetry.proto.tra" +
      "ce.v1.Status.StatusCode\"N\n\nStatusCode\022\025\n" +
      "\021STATUS_CODE_UNSET\020\000\022\022\n\016STATUS_CODE_OK\020\001" +
      "\022\025\n\021STATUS_CODE_ERROR\020\002J\004\010\001\020\002Bw\n\037io.open" +
      "telemetry.proto.trace.v1B\nTraceProtoP\001Z\'" +
      "go.opentelemetry.io/proto/otlp/trace/v1\252" +
      "\002\034OpenTelemetry.Proto.Trace.V1b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          io.opentelemetry.proto.common.v1.CommonProto.getDescriptor(),
          io.opentelemetry.proto.resource.v1.ResourceProto.getDescriptor(),
        });
    internal_static_opentelemetry_proto_trace_v1_TracesData_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_opentelemetry_proto_trace_v1_TracesData_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_opentelemetry_proto_trace_v1_TracesData_descriptor,
        new java.lang.String[] { "ResourceSpans", });
    internal_static_opentelemetry_proto_trace_v1_ResourceSpans_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_opentelemetry_proto_trace_v1_ResourceSpans_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_opentelemetry_proto_trace_v1_ResourceSpans_descriptor,
        new java.lang.String[] { "Resource", "ScopeSpans", "SchemaUrl", });
    internal_static_opentelemetry_proto_trace_v1_ScopeSpans_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_opentelemetry_proto_trace_v1_ScopeSpans_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_opentelemetry_proto_trace_v1_ScopeSpans_descriptor,
        new java.lang.String[] { "Scope", "Spans", "SchemaUrl", });
    internal_static_opentelemetry_proto_trace_v1_Span_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_opentelemetry_proto_trace_v1_Span_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_opentelemetry_proto_trace_v1_Span_descriptor,
        new java.lang.String[] { "TraceId", "SpanId", "TraceState", "ParentSpanId", "Name", "Kind", "StartTimeUnixNano", "EndTimeUnixNano", "Attributes", "DroppedAttributesCount", "Events", "DroppedEventsCount", "Links", "DroppedLinksCount", "Status", });
    internal_static_opentelemetry_proto_trace_v1_Span_Event_descriptor =
      internal_static_opentelemetry_proto_trace_v1_Span_descriptor.getNestedTypes().get(0);
    internal_static_opentelemetry_proto_trace_v1_Span_Event_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_opentelemetry_proto_trace_v1_Span_Event_descriptor,
        new java.lang.String[] { "TimeUnixNano", "Name", "Attributes", "DroppedAttributesCount", });
    internal_static_opentelemetry_proto_trace_v1_Span_Link_descriptor =
      internal_static_opentelemetry_proto_trace_v1_Span_descriptor.getNestedTypes().get(1);
    internal_static_opentelemetry_proto_trace_v1_Span_Link_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_opentelemetry_proto_trace_v1_Span_Link_descriptor,
        new java.lang.String[] { "TraceId", "SpanId", "TraceState", "Attributes", "DroppedAttributesCount", });
    internal_static_opentelemetry_proto_trace_v1_Status_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_opentelemetry_proto_trace_v1_Status_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_opentelemetry_proto_trace_v1_Status_descriptor,
        new java.lang.String[] { "Message", "Code", });
    io.opentelemetry.proto.common.v1.CommonProto.getDescriptor();
    io.opentelemetry.proto.resource.v1.ResourceProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}