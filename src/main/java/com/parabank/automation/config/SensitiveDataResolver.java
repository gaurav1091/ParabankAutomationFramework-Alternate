package com.parabank.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SensitiveDataResolver {

	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)}");
	private static final String LOCAL_SECRETS_FILE_PATH = "config/local-secrets.properties";
	private static final String USERNAME_SYSTEM_PROPERTY = "app.username";
	private static final String PASSWORD_SYSTEM_PROPERTY = "app.password";
	private static final String USERNAME_SECRET_KEY = "PARABANK_USERNAME";
	private static final String PASSWORD_SECRET_KEY = "PARABANK_PASSWORD";

	private static volatile Properties localSecretsProperties;

	private SensitiveDataResolver() {
	}

	public static String resolveCredentialValue(String rawValue, String contextDescription) {
		String resolvedValue = resolvePlaceholders(rawValue);
		if (resolvedValue == null || resolvedValue.trim().isEmpty() || containsPlaceholder(resolvedValue)) {
			throw new IllegalStateException("Sensitive value could not be resolved for: " + contextDescription
					+ ". Provide it using Maven system properties (-Dapp.username / -Dapp.password), environment variables (PARABANK_USERNAME / PARABANK_PASSWORD), or config/local-secrets.properties.");
		}
		return resolvedValue.trim();
	}

	public static String resolvePlaceholders(String rawValue) {
		if (rawValue == null || rawValue.trim().isEmpty()) {
			return rawValue;
		}

		Matcher matcher = PLACEHOLDER_PATTERN.matcher(rawValue);
		StringBuffer resolvedBuffer = new StringBuffer();

		while (matcher.find()) {
			String placeholderKey = matcher.group(1).trim();
			String replacement = lookupValue(placeholderKey);
			if (replacement == null) {
				replacement = matcher.group(0);
			}
			matcher.appendReplacement(resolvedBuffer, Matcher.quoteReplacement(replacement));
		}

		matcher.appendTail(resolvedBuffer);
		return resolvedBuffer.toString();
	}

	private static boolean containsPlaceholder(String value) {
		return value != null && PLACEHOLDER_PATTERN.matcher(value).find();
	}

	private static String lookupValue(String key) {
		String normalizedKey = normalizeKey(key);

		String systemValue = System.getProperty(normalizedKey);
		if (systemValue != null && !systemValue.trim().isEmpty()) {
			return systemValue.trim();
		}

		String envValue = System.getenv(normalizedKey);
		if (envValue != null && !envValue.trim().isEmpty()) {
			return envValue.trim();
		}

		Properties secretsProperties = getLocalSecretsProperties();
		String localValue = secretsProperties.getProperty(normalizedKey);
		if (localValue != null && !localValue.trim().isEmpty()) {
			return localValue.trim();
		}

		return null;
	}

	private static String normalizeKey(String key) {
		if (USERNAME_SYSTEM_PROPERTY.equalsIgnoreCase(key) || USERNAME_SECRET_KEY.equalsIgnoreCase(key)
				|| "username".equalsIgnoreCase(key)) {
			return USERNAME_SECRET_KEY;
		}

		if (PASSWORD_SYSTEM_PROPERTY.equalsIgnoreCase(key) || PASSWORD_SECRET_KEY.equalsIgnoreCase(key)
				|| "password".equalsIgnoreCase(key)) {
			return PASSWORD_SECRET_KEY;
		}

		return key;
	}

	private static Properties getLocalSecretsProperties() {
		if (localSecretsProperties == null) {
			synchronized (SensitiveDataResolver.class) {
				if (localSecretsProperties == null) {
					localSecretsProperties = loadLocalSecretsProperties();
				}
			}
		}
		return localSecretsProperties;
	}

	private static Properties loadLocalSecretsProperties() {
		Properties properties = new Properties();
		Path localSecretsPath = Paths.get(LOCAL_SECRETS_FILE_PATH);
		if (!Files.exists(localSecretsPath)) {
			return properties;
		}

		try (InputStream inputStream = Files.newInputStream(localSecretsPath)) {
			properties.load(inputStream);
		} catch (IOException exception) {
			throw new IllegalStateException("Failed to load local secrets file: " + LOCAL_SECRETS_FILE_PATH, exception);
		}

		return properties;
	}
}