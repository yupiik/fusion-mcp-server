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
package io.yupiik.fusion.mcp.protocol;

import io.yupiik.fusion.mcp.model.LoggingLevel;
import io.yupiik.fusion.http.server.api.Request;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;

import static java.util.Optional.ofNullable;

public class MCPSession implements Serializable {
    private LoggingLevel loggingLevel = LoggingLevel.info;

    // todo: ensure there is some session affinity otherwise this will fail
    private volatile SseBus sse;

    public void setLoggingLevel(final LoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    public LoggingLevel getLoggingLevel() {
        return loggingLevel;
    }

    // todo: add a session listener to auto disconnect
    public SseBus newSse() {
        if (sse != null) {
            sse.cancel();
        }
        return sse = new SseBus();
    }

    public SseBus sse() {
        return sse;
    }

    public static class Accessor {
        private Accessor() {
            // no-op
        }

        public static MCPSession get(final Request request) {
            return ofNullable(request.unwrap(HttpServletRequest.class).getSession(false))
                    .map(s -> s.getAttribute(MCPSession.class.getName()))
                    .map(MCPSession.class::cast)
                    .orElseThrow(() -> new IllegalStateException("No session"));
        }

        public static void create(final Request request) {
            request
                    .unwrap(HttpServletRequest.class)
                    .getSession(true)
                    .setAttribute(MCPSession.class.getName(), MCPSession.class);
        }
    }
}
