package com.nitnelave.CreeperHeal.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.logging.Logger;

abstract class ConfigValue<T> {

  protected final Logger log = Logger.getLogger("Minecraft");
  final YamlConfiguration config;
  private final String key;
  private final T defaultValue;
  T value;

  ConfigValue(T value, YamlConfiguration config, String key) {
    defaultValue = value;
    this.config = config;
    this.key = key;
    this.value = value;
  }

  T getValue() {
    return value;
  }

  void setValue(T value) {
    this.value = value;
  }

  T getDefaultValue() {
    return defaultValue;
  }

  String getKey() {
    return key;
  }

  protected abstract void load();

  void write() {
    config.set(key, value);
  }
}
