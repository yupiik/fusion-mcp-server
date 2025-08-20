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
package io.yupiik.fusion.tool;

import io.yupiik.fusion.mcp.api.MCPPrompt;
import io.yupiik.fusion.mcp.api.MCPTool;
import io.yupiik.fusion.mcp.model.PromptResponse;
import io.yupiik.fusion.mcp.model.Role;
import io.yupiik.fusion.tool.model.Demo;
import io.yupiik.fusion.framework.api.scope.ApplicationScoped;
import io.yupiik.fusion.framework.build.api.jsonrpc.JsonRpc;
import io.yupiik.fusion.framework.build.api.jsonrpc.JsonRpcParam;

import java.util.List;

import static io.yupiik.fusion.mcp.model.Content.text;

@ApplicationScoped
public class DemoTools {
    @MCPTool
    @JsonRpc(value = "demo/tool", documentation = "Demo.")
    public Demo demoTool() {
        return new Demo("hello fusion!");
    }

    @MCPPrompt
    @JsonRpc(value = "demo/prompt", documentation = "Demo.")
    public PromptResponse demoPrompt(@JsonRpcParam final String code) {
        return new PromptResponse(
                null,
                "hello fusion!",
                List.of(new PromptResponse.Message(
                        Role.user,
                        text("hello sir! your code is <" + code + '>'))));
    }
}
