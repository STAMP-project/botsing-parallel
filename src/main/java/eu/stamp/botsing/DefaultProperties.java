package eu.stamp.botsing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultProperties {

	private final Logger LOG = LoggerFactory.getLogger(DefaultProperties.class);
	private final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";

	private static Properties properties = null;
	private static DefaultProperties instance = null;

	protected DefaultProperties() {
		loadConfig();
	}

	private void loadConfig() {
		try {
			InputStream inputstream = getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILE_NAME);
			properties = new Properties();
			properties.load(inputstream);

		} catch (FileNotFoundException eta) {
			LOG.error("Default config.properties file not found in the resources of the jar file!");
			throw new IllegalStateException(
					"Default config.properties file not found in the resources of the jar file!");
		} catch (IOException e) {
			LOG.error("Exception while reading default config.properties from the resources of the jar file!");
			throw new IllegalStateException(
					"Exception while reading default config.properties from the resources of the jar file!");
		}
	}

	public static DefaultProperties getInstance() {
		if (instance == null) {
			instance = new DefaultProperties();
		}
		return instance;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public Set<String> getAllPropertyNames() {
		return properties.stringPropertyNames();
	}

	public boolean containsKey(String key) {
		return properties.containsKey(key);
	}

	public void removeProperty(String key) {
		if (containsKey(key))
			properties.remove(key);
	}

	public Properties getProperties() {
		return properties;
	}
}
