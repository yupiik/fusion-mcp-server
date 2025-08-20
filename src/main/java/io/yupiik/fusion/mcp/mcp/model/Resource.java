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

import java.util.Base64;

@JsonModel
public record Resource(
        @JsonProperty("_meta") Metadata metadata,
        String uri, String mimeType,
        String text,
        String blob // base64, required
) {
    public static Resource text(final Metadata metadata, final String uri, final String mimeType, final String text) {
        return new Resource(metadata, uri, mimeType, text, null);
    }

    public static Resource blob(final Metadata metadata, final String uri, final String mimeType, final byte[] content) {
        return new Resource(metadata, uri, mimeType, null, Base64.getEncoder().encodeToString(content));
    }
}
