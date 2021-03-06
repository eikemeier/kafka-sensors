package de.melsicon.kafka.serde.json;

import static org.apache.kafka.common.serialization.Serdes.serdeFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.melsicon.kafka.model.SensorState;
import de.melsicon.kafka.model.SensorStateWithDuration;
import de.melsicon.kafka.serde.Format;
import de.melsicon.kafka.serde.SensorStateSerdes;
import javax.inject.Inject;
import org.apache.kafka.common.serialization.Serde;

public final class JsonSerdes implements SensorStateSerdes {
  private static final ObjectMapper MAPPER = JsonHelper.mapper();

  @Inject
  public JsonSerdes() {}

  @Override
  public String name() {
    return "json";
  }

  @Override
  public Format format() {
    return Format.JSON;
  }

  @Override
  public Serde<SensorState> createSensorStateSerde() {
    var serializer = new JsonSerializer<>(MAPPER, SensorState.class);
    var deserializer = new JsonDeserializer<>(MAPPER, SensorState.class);

    return serdeFrom(serializer, deserializer);
  }

  @Override
  public Serde<SensorStateWithDuration> createSensorStateWithDurationSerde() {
    var serializer = new JsonSerializer<>(MAPPER, SensorStateWithDuration.class);
    var deserializer = new JsonDeserializer<>(MAPPER, SensorStateWithDuration.class);

    return serdeFrom(serializer, deserializer);
  }
}
