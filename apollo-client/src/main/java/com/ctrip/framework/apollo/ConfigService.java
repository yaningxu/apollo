package com.ctrip.framework.apollo;

import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.internals.ConfigManager;
import com.ctrip.framework.apollo.spi.ConfigFactory;
import com.ctrip.framework.apollo.spi.ConfigRegistry;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for client config use
 *
 * @author Jason Song(song_s@ctrip.com)
 */
public class ConfigService {
  private static final ConfigService s_instance = new ConfigService();

  private volatile ConfigManager m_configManager;
  private volatile ConfigRegistry m_configRegistry;

  private static List<Config> configs = new ArrayList<>();

  private ConfigManager getManager() {
    if (m_configManager == null) {
      synchronized (this) {
        if (m_configManager == null) {
          m_configManager = ApolloInjector.getInstance(ConfigManager.class);
        }
      }
    }

    return m_configManager;
  }

  private ConfigRegistry getRegistry() {
    if (m_configRegistry == null) {
      synchronized (this) {
        if (m_configRegistry == null) {
          m_configRegistry = ApolloInjector.getInstance(ConfigRegistry.class);
        }
      }
    }

    return m_configRegistry;
  }

  /**
   * Get Application's config instance.
   *
   * @return config instance
   */
  public static Config getAppConfig() {
    return getConfig(ConfigConsts.NAMESPACE_APPLICATION);
  }

  /**
   * Get the config instance for the namespace.
   * add namespace to list in order
   *
   * @param namespace the namespace of the config
   * @return config instance
   */
  public static Config getConfig2(String namespace) {

    Config config = s_instance.getManager().getConfig(namespace);
    if(config != null){
      configs.add(config);
    }
    return config;
  }

  /**
   * Get the config instance for the key.
   *
   * @param key the key of the property
   * @return config instance
   */
  public static Config getConfigByKey(String key){
    int size = configs.size();
    for(int i = 0; i < size; i++){
      Config config = configs.get(i);
      if(config.getPropertyNames().contains(key)){
        return config;
      }
    }
    return null;
  }

  /**
   * Get the config instance for the namespace.
   *
   * @param namespace the namespace of the config
   * @return config instance
   */
  public static Config getConfig(String namespace) {
    return s_instance.getManager().getConfig(namespace);
  }

  public static ConfigFile getConfigFile(String namespace, ConfigFileFormat configFileFormat) {
    return s_instance.getManager().getConfigFile(namespace, configFileFormat);
  }

  static void setConfig(Config config) {
    setConfig(ConfigConsts.NAMESPACE_APPLICATION, config);
  }

  /**
   * Manually set the config for the namespace specified, use with caution.
   *
   * @param namespace the namespace
   * @param config    the config instance
   */
  static void setConfig(String namespace, final Config config) {
    s_instance.getRegistry().register(namespace, new ConfigFactory() {
      @Override
      public Config create(String namespace) {
        return config;
      }

      @Override
      public ConfigFile createConfigFile(String namespace, ConfigFileFormat configFileFormat) {
        return null;
      }

    });
  }

  static void setConfigFactory(ConfigFactory factory) {
    setConfigFactory(ConfigConsts.NAMESPACE_APPLICATION, factory);
  }

  /**
   * Manually set the config factory for the namespace specified, use with caution.
   *
   * @param namespace the namespace
   * @param factory   the factory instance
   */
  static void setConfigFactory(String namespace, ConfigFactory factory) {
    s_instance.getRegistry().register(namespace, factory);
  }

  // for test only
  static void reset() {
    synchronized (s_instance) {
      s_instance.m_configManager = null;
      s_instance.m_configRegistry = null;
    }
  }
}
