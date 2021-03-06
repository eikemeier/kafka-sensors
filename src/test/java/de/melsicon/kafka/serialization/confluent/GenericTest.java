package de.melsicon.kafka.serialization.confluent;

import static com.google.common.truth.Truth.assertThat;
import static de.melsicon.kafka.sensors.generic.SensorStateSchema.ENUM_OFF;
import static de.melsicon.kafka.sensors.generic.SensorStateSchema.FIELD_ID;
import static de.melsicon.kafka.sensors.generic.SensorStateSchema.FIELD_STATE;
import static de.melsicon.kafka.sensors.generic.SensorStateSchema.FIELD_TIME;
import static de.melsicon.kafka.sensors.generic.SensorStateSchema.SCHEMA;
import static de.melsicon.kafka.serialization.confluent.TestHelper.INSTANT;
import static de.melsicon.kafka.serialization.confluent.TestHelper.KAFKA_TOPIC;
import static de.melsicon.kafka.serialization.confluent.TestHelper.REGISTRY_SCOPE;

import de.melsicon.kafka.sensors.generic.SensorStateSchema;
import de.melsicon.kafka.serde.confluent.GenericAvroDeserializer;
import de.melsicon.kafka.testutil.SchemaRegistryRule;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public final class GenericTest {
  @ClassRule
  public static final SchemaRegistryRule registryTestResource =
      new SchemaRegistryRule(REGISTRY_SCOPE);

  private static @MonotonicNonNull Serializer<GenericRecord> encoder;
  private static @MonotonicNonNull Deserializer<GenericRecord> decoder;

  @BeforeClass
  @EnsuresNonNull({"encoder", "decoder"})
  public static void before() {
    encoder = new GenericAvroSerializer();
    decoder = new GenericAvroDeserializer(SensorStateSchema.SCHEMA);
    registerRegistry();
  }

  @RequiresNonNull({"encoder", "decoder"})
  private static void registerRegistry() {
    registryTestResource.configureSerializer(encoder);
    registryTestResource.configureDeserializer(decoder);
  }

  @AfterClass
  @RequiresNonNull({"encoder", "decoder"})
  public static void after() {
    encoder.close();
    decoder.close();
  }

  private static GenericRecord createSensorState() {
    return new GenericRecordBuilder(SCHEMA)
        .set(FIELD_ID, "7331")
        .set(FIELD_TIME, INSTANT.toEpochMilli())
        .set(FIELD_STATE, ENUM_OFF)
        .build();
  }

  @Test
  @RequiresNonNull({"encoder", "decoder"})
  public void canDecode() {
    var sensorState = createSensorState();

    var encoded = encoder.serialize(KAFKA_TOPIC, sensorState);

    // Check for “Magic Byte”
    // https://docs.confluent.io/current/schema-registry/serializer-formatter.html#wire-format
    assertThat(encoded[0]).isEqualTo((byte) 0);

    var decoded = decoder.deserialize(KAFKA_TOPIC, encoded);

    assertThat(decoded).isEqualTo(sensorState);
  }
}
