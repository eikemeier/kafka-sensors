package de.melsicon.kafka.sensors.app;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import java.nio.file.Path;

public final class ConfigHelper {
  private ConfigHelper() {}

  public static Config config(Path configFile) {
    var configSource = ConfigSources.file(configFile).build();
    return Config.builder(configSource)
        .disableSystemPropertiesSource()
        .disableEnvironmentVariablesSource()
        .build();
  }
}
