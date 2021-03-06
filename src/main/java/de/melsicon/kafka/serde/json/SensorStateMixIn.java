package de.melsicon.kafka.serde.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import de.melsicon.kafka.model.ImmutableSensorState;

@JsonDeserialize(builder = ImmutableSensorState.Builder.class)
public abstract class SensorStateMixIn {
  private SensorStateMixIn() {}

  @JsonPOJOBuilder(withPrefix = "")
  public abstract static class BuilderMixIn {
    private BuilderMixIn() {}

    @JsonCreator
    public static ImmutableSensorState.Builder builder() {
      throw new UnsupportedOperationException();
    }
  }
}
