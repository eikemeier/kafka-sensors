package de.melsicon.kafka.serialization.reflect;

import static com.google.common.truth.Truth.assertThat;
import static de.melsicon.kafka.sensors.reflect.SensorStateWithDuration.MODEL;
import static de.melsicon.kafka.sensors.reflect.SensorStateWithDuration.SCHEMA;
import static org.junit.Assert.assertThrows;

import de.melsicon.kafka.sensors.reflect.SensorState;
import de.melsicon.kafka.sensors.reflect.SensorStateWithDuration;
import de.melsicon.kafka.sensors.reflect.State;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import org.apache.avro.AvroTypeException;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.MessageDecoder;
import org.apache.avro.message.MessageEncoder;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.junit.BeforeClass;
import org.junit.Test;

public final class SerializationTest {
  private static final Instant INSTANT = Instant.ofEpochSecond(443634300L);

  private static @MonotonicNonNull MessageEncoder<SensorStateWithDuration> encoder;
  private static @MonotonicNonNull MessageDecoder<SensorStateWithDuration> decoder;

  @BeforeClass
  @EnsuresNonNull({"encoder", "decoder"})
  public static void before() {
    encoder = new BinaryMessageEncoder<>(MODEL, SCHEMA);
    decoder = new BinaryMessageDecoder<>(MODEL, SCHEMA);
  }

  @Test
  @RequiresNonNull({"encoder", "decoder"})
  public void canDecode() throws IOException {
    var event = new SensorState();
    event.id = "7331";
    event.time = INSTANT;
    event.state = State.OFF;

    var sensorState = new SensorStateWithDuration();
    sensorState.event = event;
    sensorState.duration = Duration.ofSeconds(15);

    var encoded = encoder.encode(sensorState);

    // Check for single-record format marker
    // https://avro.apache.org/docs/current/spec.html#single_object_encoding
    assertThat(encoded.getShort(0)).isEqualTo((short) 0xc301);

    var decoded = decoder.decode(encoded);

    assertThat(decoded).isEqualTo(sensorState);
  }

  @Test
  @RequiresNonNull("encoder")
  public void stateIsRequired() {
    var event = new SensorState();
    event.id = "7331";
    event.time = INSTANT;

    var sensorState = new SensorStateWithDuration();
    sensorState.event = event;
    sensorState.duration = Duration.ofSeconds(15);

    var enc = encoder; // https://github.com/typetools/checker-framework/issues/1248
    assertThrows(AvroTypeException.class, () -> enc.encode(sensorState));
  }
}
