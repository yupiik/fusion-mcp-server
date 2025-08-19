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
package io.yupiik.fusion.mcp.mcp.model;

import io.yupiik.fusion.framework.build.api.json.JsonModel;

import java.util.Base64;

@JsonModel
public record Content(
        String type,
        String text,
        String data,
        String mimetype,
        Resource resource) {
    public static Content text(final String text) {
        return new Content("text", text, null, null, null);
    }

    public static Content image(final String mimeType, final byte[] content) {
        return new Content("image", null, Base64.getEncoder().encodeToString(content), mimeType, null);
    }

    public static Content audio(final String mimeType, final byte[] content) {
        return new Content("audio", null, Base64.getEncoder().encodeToString(content), mimeType, null);
    }

    public static Content resource(final Resource resource) {
        return new Content("resource", null, null, null, resource);
    }
}
