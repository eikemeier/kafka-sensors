package de.melsicon.kafka.serde.avromapper;

import com.google.errorprone.annotations.Immutable;
import de.melsicon.kafka.model.SensorState;
import de.melsicon.kafka.model.SensorStateWithDuration;
import de.melsicon.kafka.serde.mapping.MapStructConfig;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Immutable
@Mapper(config = MapStructConfig.class, uses = DurationMapper.class)
public abstract class SpecificMapper
    implements AvroMapper<
        de.melsicon.kafka.sensors.avro.SensorState,
        de.melsicon.kafka.sensors.avro.SensorStateWithDuration> {
  public static SpecificMapper instance() {
    return new SpecificMapperImpl();
  }

  @Nullable
  @Override
  public abstract SensorState map(@Nullable de.melsicon.kafka.sensors.avro.SensorState sensorState);

  @Nullable
  @Override
  public abstract de.melsicon.kafka.sensors.avro.SensorState unmap(
      @Nullable SensorState sensorState);

  @Nullable
  @Override
  public abstract SensorStateWithDuration map2(
      @Nullable de.melsicon.kafka.sensors.avro.SensorStateWithDuration sensorState);

  @Nullable
  @Override
  @Mapping(ignore = true, target = "eventBuilder")
  public abstract de.melsicon.kafka.sensors.avro.SensorStateWithDuration unmap2(
      @Nullable SensorStateWithDuration sensorState);
}
