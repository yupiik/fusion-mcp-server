/*
 * Copyright (c) 2025 - present - Yupiik SAS - https://www.yupiik.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.yupiik.fusion.mcp.demo.mcp.model;

import io.yupiik.fusion.framework.build.api.json.JsonModel;
import io.yupiik.fusion.framework.build.api.json.JsonProperty;

import java.util.List;
import java.util.Map;

// simplified JSON-Schema model for MCP - should be enhanced if set in a lib
@JsonModel
public record JsonSchema(
        String type,
        Boolean nullable,
        String title,
        String description,
        String format,
        String pattern,
        Map<String, JsonSchema> properties,
        Object additionalProperties,
        JsonSchema items,
        @JsonProperty("enum") List<String> enumeration,
        List<String> required,
        @JsonProperty("default") Object defaultValue) {
    // generic primitive
    public JsonSchema(final String type, final Boolean nullable, final String description) {
        this(type, nullable, null, description, null, null, null, null, null, null, null, null);
    }

    // string
    public JsonSchema(final Boolean nullable, final String description,
                      final String format, final String pattern) {
        this("string", nullable, null, description, format, pattern, null, null, null, null, null, null);
    }

    // enumeration
    public JsonSchema(final Boolean nullable, final String description,
                      final String format, final String pattern, final List<String> enumeration) {
        this("string", nullable, null, description, format, pattern, null, null, null, enumeration, null, null);
    }

    // object
    public JsonSchema(final Boolean nullable, final String description,
                      final Map<String, JsonSchema> properties, final Object additionalProperties,
                      final List<String> required) {
        this("object", nullable, null, description, null, null, properties, additionalProperties, null, null, required, null);
    }

    public JsonSchema(final Boolean nullable, final String description, final Map<String, JsonSchema> properties, final List<String> required) {
        this("object", nullable, null, description, null, null, properties, null, null, null, required, null);
    }

    // array
    public JsonSchema(final Boolean nullable, final String description, final JsonSchema items) {
        this("array", nullable, null, description, null, null, null, null, items, null, null, null);
    }
}
