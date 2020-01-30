package de.melsicon.kafka.serialization.generic;

import static de.melsicon.kafka.sensors.generic.SensorStateSchema.ENUM_OFF;
import static de.melsicon.kafka.sensors.generic.SensorStateSchema.FIELD_ID;
import static de.melsicon.kafka.sensors.generic.SensorStateSchema.FIELD_STATE;
import static de.melsicon.kafka.sensors.generic.SensorStateSchema.FIELD_TIME;
import static de.melsicon.kafka.sensors.generic.SensorStateSchema.SCHEMA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.IOException;
import java.time.Instant;
import org.apache.avro.AvroMissingFieldException;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.MessageDecoder;
import org.apache.avro.message.MessageEncoder;
import org.junit.BeforeClass;
import org.junit.Test;

public final class SerializationTest {
  private static final Instant INSTANT = Instant.ofEpochSecond(443634300L);

  private static MessageEncoder<GenericRecord> encoder;
  private static MessageDecoder<GenericRecord> decoder;

  @BeforeClass
  public static void before() {
    var model = GenericData.get();
    encoder = new BinaryMessageEncoder<>(model, SCHEMA);
    decoder = new BinaryMessageDecoder<>(model, SCHEMA);
  }

  @Test
  public void canDecode() throws IOException {
    var sensorState =
        new GenericRecordBuilder(SCHEMA)
            .set(FIELD_ID, "7331")
            .set(FIELD_TIME, INSTANT.toEpochMilli())
            .set(FIELD_STATE, ENUM_OFF)
            .build();

    var encoded = encoder.encode(sensorState);

    // Check for single-record format marker
    // http://avro.apache.org/docs/1.9.1/spec.html#single_object_encoding
    assertThat(encoded.getShort(0)).isEqualTo((short) 0xc301);

    var decoded = decoder.decode(encoded);

    assertThat(decoded).isEqualTo(sensorState);
  }

  @Test
  public void stateIsRequired() throws IOException {
    assertThatExceptionOfType(AvroMissingFieldException.class)
        .isThrownBy(
            () ->
                new GenericRecordBuilder(SCHEMA)
                    .set(FIELD_ID, "7331")
                    .set(FIELD_TIME, INSTANT.toEpochMilli())
                    .build());
  }
}