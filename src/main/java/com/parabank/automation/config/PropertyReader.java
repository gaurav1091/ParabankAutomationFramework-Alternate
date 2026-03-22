package com.parabank.automation.config;

import com.parabank.automation.exceptions.ConfigFileException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyReader {

	private PropertyReader() {
	}

	public static Properties loadProperties(String classpathResource) {
		Properties properties = new Properties();

		try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(classpathResource)) {
			if (inputStream == null) {
				throw new ConfigFileException("Unable to find properties file on classpath: " + classpathResource);
			}

			properties.load(inputStream);
		} catch (IOException exception) {
			throw new ConfigFileException("Unable to load properties file from classpath: " + classpathResource,
					exception);
		}

		return properties;
	}
}