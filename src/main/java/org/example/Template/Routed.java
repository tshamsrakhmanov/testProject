package org.example.Template;

import com.fasterxml.jackson.databind.JsonNode;

public record Routed(String templateName, JsonNode payload) {
}
