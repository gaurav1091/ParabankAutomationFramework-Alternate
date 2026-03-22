package com.parabank.automation.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class JsonUtils {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private JsonUtils() {
	}

	public static <T> T readJsonFromFile(String classpathResource, Class<T> clazz) {
		try (InputStream inputStream = getRequiredResourceAsStream(classpathResource)) {
			return OBJECT_MAPPER.readValue(inputStream, clazz);
		} catch (IOException exception) {
			throw new RuntimeException("Failed to read JSON resource from classpath: " + classpathResource, exception);
		}
	}

	public static <T> T readJsonFromFile(String classpathResource, TypeReference<T> typeReference) {
		try (InputStream inputStream = getRequiredResourceAsStream(classpathResource)) {
			return OBJECT_MAPPER.readValue(inputStream, typeReference);
		} catch (IOException exception) {
			throw new RuntimeException("Failed to read JSON resource from classpath: " + classpathResource, exception);
		}
	}

	private static InputStream getRequiredResourceAsStream(String classpathResource) {
		InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(classpathResource);

		if (inputStream == null) {
			throw new RuntimeException("JSON resource not found on classpath: " + classpathResource);
		}

		return inputStream;
	}
}