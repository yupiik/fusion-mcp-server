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
package io.yupiik.fusion.mcp.demo.model.fusion;

import io.yupiik.fusion.framework.build.api.json.JsonModel;
import io.yupiik.fusion.framework.build.api.json.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonModel
public record OpenRpc(
        Map<String, JsonSchema> schemas,
        Map<String, JsonRpcMethod> methods
) {
    // note that result is also available but we do not map it until we do need it
    @JsonModel
    public record JsonRpcMethod(
            String name,
            String description,
            List<Parameter> params,
            Result result
    ) {
        @JsonModel
        public record Parameter(
                String name,
                JsonSchema schema,
                Boolean required
        ) {}
    }

    @JsonModel
    public record Result(JsonSchema schema) {}

    @JsonModel
    public record JsonSchema(
            @JsonProperty("$ref") String ref,
            @JsonProperty("$id") String id,
            String type,
            Boolean nullable,
            String description,
            String format,
            String pattern,
            Map<String, JsonSchema> properties,
            Object additionalProperties,
            JsonSchema items,
            @JsonProperty("enum") List<String> enumeration
    ) {
    }
}
