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
package io.yupiik.fusion.mcp.model;

import io.yupiik.fusion.framework.build.api.json.JsonModel;
import io.yupiik.fusion.framework.build.api.json.JsonProperty;

import java.util.Base64;

@JsonModel
public record Content(
        @JsonProperty("_meta") Metadata metadata,
        Annotations annotations,
        Type type,
        String text,
        String data, // base64
        String mimetype,
        Resource resource) {
    @JsonModel
    public enum Type {
        text, image, audio, resource
    }

    public static Content text(final String text) {
        return new Content(null, null, Type.text, text, null, null, null);
    }

    public static Content image(final String mimeType, final byte[] content) {
        return new Content(null, null, Type.image, null, Base64.getEncoder().encodeToString(content), mimeType, null);
    }

    public static Content audio(final String mimeType, final byte[] content) {
        return new Content(null, null, Type.audio, null, Base64.getEncoder().encodeToString(content), mimeType, null);
    }

    public static Content resource(final Resource resource) {
        return new Content(null, null, Type.resource, null, null, null, resource);
    }
}