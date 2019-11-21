package de.melsicon.kafka.serde.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import de.melsicon.annotation.Nullable;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public final class ProtoDeserializer<T extends MessageLite> implements Deserializer<T> {
  private final Parser<T> parser;

  public ProtoDeserializer(Parser<T> parser) {
    this.parser = parser;
  }

  @Override
  public @Nullable T deserialize(String topic, @Nullable byte[] data) {
    if (data == null) {
      return null;
    }
    try {
      return parser.parseFrom(data);
    } catch (InvalidProtocolBufferException e) {
      throw new SerializationException("Error while parsing message from topic " + topic, e);
    }
  }
}