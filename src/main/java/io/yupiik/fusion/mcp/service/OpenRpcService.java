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
package io.yupiik.fusion.mcp.service;

import io.yupiik.fusion.framework.api.scope.ApplicationScoped;
import io.yupiik.fusion.json.JsonMapper;
import io.yupiik.fusion.mcp.model.fusion.OpenRpc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

@ApplicationScoped
public class OpenRpcService {
    private final JsonMapper jsons;

    protected OpenRpcService() {
        this(null);
    }

    public OpenRpcService(final JsonMapper jsonMapper) {
        this.jsons = jsonMapper;
    }

    public OpenRpc load() {
        try (final var in = new InputStreamReader(requireNonNull(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("META-INF/fusion/jsonrpc/openrpc.json")), UTF_8)) {
            return jsons.read(OpenRpc.class, in);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Map<String, OpenRpc.JsonSchema> resolveSchemas(final OpenRpc openRpc) {
        // remove $ref for MCP - this could be optimized a bit but ok-ish
        final var resolvedSchemas = openRpc
                .schemas()
                .entrySet().stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        final var noMoreRef = new HashSet<String>();
        boolean redo = true;
        while (redo) {
            redo = false;
            for (final var schema : resolvedSchemas.entrySet()) {
                if (noMoreRef.contains(schema.getKey())) {
                    continue;
                }

                final var replacement = resolveRefs(resolvedSchemas, schema.getValue());
                if (replacement != null) {
                    resolvedSchemas.put(schema.getKey(), replacement);
                    redo = true;
                } else {
                    noMoreRef.add(schema.getKey());
                }
            }
        }

        return resolvedSchemas;
    }

    public OpenRpc.JsonSchema resolveRefs(final Map<String, OpenRpc.JsonSchema> world, final OpenRpc.JsonSchema schema) {
        if (schema == null) {
            return null;
        }

        final var ref = schema.ref();
        if (ref != null && !Objects.equals(ref, schema.id())) {
            final var jsonSchema = world.get(ref.startsWith("#/schemas/") ? ref.substring("#/schemas/".length()) : ref);
            return ofNullable(resolveRefs(world, jsonSchema)).orElse(jsonSchema);
        }

        if ("object".equals(schema.type()) && schema.properties() != null) {
            // allocate only if one nested schema resolves
            Map<String, OpenRpc.JsonSchema> newProperties = null;
            for (final var prop : schema.properties().entrySet()) {
                final var resolved = resolveRefs(world, prop.getValue());
                if (resolved != null) {
                    if (newProperties == null) {
                        newProperties = new HashMap<>(schema.properties().size());
                    }
                    newProperties.put(prop.getKey(), resolved);
                }
            }

            // if we resolved at least one prop, ensure we do not miss any
            if (newProperties != null && newProperties.size() != schema.properties().size()) {
                final var npr = newProperties;
                newProperties.putAll(schema.properties().entrySet().stream()
                        .filter(it -> !npr.containsKey(it.getKey()))
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue)));
            }

            // handle additional props (for maps mainly)
            Object additionalProps = schema.additionalProperties();
            if (schema.additionalProperties() instanceof Map<?,?>) {
                final var addPropSchema = jsons.fromString(OpenRpc.JsonSchema.class, jsons.toString(schema.additionalProperties()));
                final var additionalPropsResolved = resolveRefs(world, addPropSchema);
                additionalProps = additionalPropsResolved != null ? additionalPropsResolved : schema.additionalProperties();
            }

            if (newProperties != null || additionalProps != schema.additionalProperties()) {
                return new OpenRpc.JsonSchema(
                        null, null,
                        schema.type(), schema.nullable(), schema.description(), schema.format(), schema.pattern(),
                        newProperties == null ? schema.properties() : newProperties, additionalProps,
                        schema.items(), schema.enumeration());
            }
        } else if ("array".equals(schema.type()) && schema.items() != null) {
            final var newItems = resolveRefs(world, schema.items());
            if (newItems != null) {
                return new OpenRpc.JsonSchema(
                        null, null, schema.type(), schema.nullable(), schema.description(), schema.format(), schema.pattern(),
                        schema.properties(), schema.additionalProperties(), newItems, schema.enumeration());
            }
        }
        return null;
    }
}
