/*
 * Copyright 2007-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

package griffon.builder.gfx.nodes.misc

import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxNode

import java.awt.RenderingHints

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class RenderingHintNode extends GfxNode {
    @GfxAttribute(alias = "k", resets = false)
    def key
    @GfxAttribute(alias = "v", resets = false)
    def value

    RenderingHintNode() {
        super("renderingHint")
    }

    void apply(GfxContext context) {
        context.g.setRenderingHint(convertKey(), convertValue())
    }

    private RenderingHints.Key convertKey() {
        def prop = ("KEY_" + key.toUpperCase()).replaceAll(" ", "_")
        return RenderingHints.@"${prop}"
    }

    private Object convertValue() {
        def prop = ("VALUE_" + value.toUpperCase()).replaceAll(" ", "_")
        return RenderingHints.@"${prop}"
    }
}
