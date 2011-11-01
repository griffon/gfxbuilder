/*
 * Copyright 2007-2010 the original author or authors.
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
import com.jhlabs.composite.MiscComposite
import com.jhlabs.composite.ContourComposite
import java.awt.Composite

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class CompositeNode extends GfxNode {
    @GfxAttribute(alias = "r", resets = false) def rule
    @GfxAttribute(alias = "a", resets = false) def alpha
    @GfxAttribute(alias = "o", resets = false) def offset

    CompositeNode() {
        super("composite")
    }

    void apply(GfxContext context) {
        context.g.setComposite(createComposite())
    }

    private Composite createComposite() {
        if (rule instanceof Number) {
            if (alpha != null) {
                return MiscComposite.getInstance(rule as int, alpha as float)
            } else {
                return MiscComposite.getInstance(rule as int, 1.0f)
            }
        } else if (rule instanceof String) {
            if (rule.toUpperCase() == 'CONTOUR') {
                return new ContourComposite(offset as int)
            }
            def r = rule.toUpperCase().replaceAll(" ", "_")
            if (alpha != null) {
                return MiscComposite.getInstance(MiscComposite."$r", alpha as float)
            } else {
                return MiscComposite.getInstance(MiscComposite."$r", 1.0f)
            }
        }
    }
}
