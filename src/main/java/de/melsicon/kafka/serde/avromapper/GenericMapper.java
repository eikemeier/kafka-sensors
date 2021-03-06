package de.melsicon.kafka.serde.avromapper;

import static de.melsicon.kafka.sensors.generic.SensorStateSchema.FIELD_ID;
import static de.melsicon.kafka.sensors.generic.SensorStateSchema.FIELD_STATE;
import static de.melsicon.kafka.sensors.generic.SensorStateSchema.FIELD_TIME;
import static de.melsicon.kafka.sensors.generic.SensorStateWithDurationSchema.FIELD_DURATION;
import static de.melsicon.kafka.sensors.generic.SensorStateWithDurationSchema.FIELD_EVENT;
import static de.melsicon.kafka.serde.avromapper.GenericMapperHelper.stateMap;
import static de.melsicon.kafka.serde.avromapper.GenericMapperHelper.stateUnmap;

import de.melsicon.kafka.model.SensorState;
import de.melsicon.kafka.model.SensorStateWithDuration;
import de.melsicon.kafka.sensors.generic.SensorStateSchema;
import de.melsicon.kafka.sensors.generic.SensorStateWithDurationSchema;
import de.melsicon.kafka.serde.SensorStateMapper;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import org.apache.avro.generic.GenericEnumSymbol;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.checkerframework.checker.nullness.qual.PolyNull;

public final class GenericMapper implements SensorStateMapper<GenericRecord, GenericRecord> {
  private GenericMapper() {}

  public static GenericMapper instance() {
    return new GenericMapper();
  }

  @Override
  public @PolyNull SensorState map(@PolyNull GenericRecord sensorState) {
    if (sensorState == null) {
      return null;
    }
    return SensorState.builder()
        .id((String) sensorState.get(FIELD_ID))
        .time((Instant) sensorState.get(FIELD_TIME))
        .state(stateMap((GenericEnumSymbol<?>) sensorState.get(FIELD_STATE)))
        .build();
  }

  @Override
  public @PolyNull GenericRecord unmap(@PolyNull SensorState sensorState) {
    if (sensorState == null) {
      return null;
    }
    return new GenericRecordBuilder(SensorStateSchema.SCHEMA)
        .set(FIELD_ID, sensorState.getId())
        .set(FIELD_TIME, sensorState.getTime())
        .set(FIELD_STATE, stateUnmap(sensorState.getState()))
        .build();
  }

  @Override
  public @PolyNull SensorStateWithDuration map2(@PolyNull GenericRecord sensorState) {
    if (sensorState == null) {
      return null;
    }
    var event = Objects.requireNonNull(map((GenericRecord) sensorState.get(FIELD_EVENT)));
    return SensorStateWithDuration.builder()
        .event(event)
        .duration((Duration) sensorState.get(FIELD_DURATION))
        .build();
  }

  @Override
  public @PolyNull GenericRecord unmap2(@PolyNull SensorStateWithDuration sensorState) {
    if (sensorState == null) {
      return null;
    }
    var event = Objects.requireNonNull(unmap(sensorState.getEvent()));
    return new GenericRecordBuilder(SensorStateWithDurationSchema.SCHEMA)
        .set(FIELD_EVENT, event)
        .set(FIELD_DURATION, sensorState.getDuration())
        .build();
  }
}
