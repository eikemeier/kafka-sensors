package de.melsicon.kafka.model;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import de.melsicon.kafka.model.SensorState.State;
import java.time.Duration;
import java.time.Instant;
import org.junit.Test;

public final class SensorStateTest {
  @Test
  public void creation() {
    var instant = Instant.ofEpochSecond(443634300L);

    var sensorState =
        SensorState.builder().setId("7331").setTime(instant).setState(State.OFF).build();

    assertThat(sensorState.getId()).isEqualTo("7331");
    assertThat(sensorState.getTime()).isEqualTo(instant);
    assertThat(sensorState.getState()).isEqualTo(State.OFF);
  }

  @Test
  public void required() {
    assertThrows(IllegalStateException.class, () -> SensorState.builder().build());
  }

  @Test
  @SuppressWarnings("nullness:argument.type.incompatible")
  public void notNull() {
    assertThrows(NullPointerException.class, () -> SensorState.builder().setId(null).build());
  }

  @Test
  public void durationShouldNotBeNegative() {
    var instant = Instant.ofEpochSecond(443634300L);

    var sensorState =
        SensorState.builder().setId("7331").setTime(instant).setState(State.OFF).build();

    assertThrows(
        IllegalStateException.class,
        () ->
            SensorStateWithDuration.builder()
                .setEvent(sensorState)
                .setDuration(Duration.ofMillis(-1))
                .build());
  }
}
