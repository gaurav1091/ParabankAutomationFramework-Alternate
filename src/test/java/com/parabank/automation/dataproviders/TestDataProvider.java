package com.parabank.automation.dataproviders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.parabank.automation.utils.JsonUtils;

import java.util.List;
import java.util.function.Function;

public final class TestDataProvider {

	private TestDataProvider() {
	}

	public static <T> T getTestDataByKey(String classpathResource, TypeReference<List<T>> typeReference,
			Function<T, String> keyExtractor, String key) {
		List<T> dataList = readList(classpathResource, typeReference);

		return dataList.stream().filter(data -> {
			String extractedKey = keyExtractor.apply(data);
			return extractedKey != null && extractedKey.equalsIgnoreCase(key);
		}).findFirst().orElseThrow(() -> new RuntimeException(
				"No test data found for key: " + key + " in classpath resource: " + classpathResource));
	}

	public static <T> List<T> readList(String classpathResource, TypeReference<List<T>> typeReference) {
		try {
			return JsonUtils.readJsonFromFile(classpathResource, typeReference);
		} catch (Exception exception) {
			throw new RuntimeException("Failed to read test data classpath resource: " + classpathResource, exception);
		}
	}
}