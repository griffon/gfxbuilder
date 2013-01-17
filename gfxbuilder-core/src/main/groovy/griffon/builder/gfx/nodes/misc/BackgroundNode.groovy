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

import griffon.builder.gfx.Colors
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxNode

import java.awt.Color

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class BackgroundNode extends GfxNode {
    @GfxAttribute(alias = "c", resets = false)
    def color

    BackgroundNode() {
        super("background")
    }

    BackgroundNode(Color color) {
        super("background")
        this.color = color
    }

    BackgroundNode(String color) {
        super("background")
        this.color = Colors.getColor(color)
    }

    void apply(GfxContext context) {
        if (!color) return
        def clip = context.g.clipBounds
        context.g.background = color
        context.g.clearRect(clip.x as int, clip.y as int, clip.width as int, clip.height as int)
    }
}
