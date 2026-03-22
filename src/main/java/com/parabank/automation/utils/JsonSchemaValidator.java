package com.parabank.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class JsonSchemaValidator {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private JsonSchemaValidator() {
	}

	public static List<String> validate(Object payload, String schemaClasspathResource) {
		try (InputStream inputStream = JsonSchemaValidator.class.getClassLoader()
				.getResourceAsStream(schemaClasspathResource)) {

			if (inputStream == null) {
				throw new RuntimeException("Schema resource not found on classpath: " + schemaClasspathResource);
			}

			JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
			JsonSchema schema = factory.getSchema(inputStream);

			JsonNode payloadNode = OBJECT_MAPPER.valueToTree(payload);
			Set<ValidationMessage> validationMessages = schema.validate(payloadNode);

			List<String> violations = new ArrayList<>();
			for (ValidationMessage validationMessage : validationMessages) {
				violations.add(validationMessage.getMessage());
			}

			return violations;
		} catch (Exception exception) {
			throw new RuntimeException("Failed to validate payload against schema: " + schemaClasspathResource,
					exception);
		}
	}
}