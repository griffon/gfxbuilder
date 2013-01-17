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

package griffon.builder.gfx.nodes.strokes

import com.jhlabs.awt.ZigzagStroke
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.StrokeProvider

import java.awt.Stroke

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class ZigzagStrokeNode extends AbstractStrokeNode implements ComposableStroke {
    @GfxAttribute(alias = "s") Stroke stroke
    @GfxAttribute(alias = "a") float amplitude = 10f
    @GfxAttribute(alias = "w") float wavelength = 10f

    ZigzagStrokeNode() {
        super("zigzagStroke")
    }

    public void addStroke(Stroke stroke) {
        setStroke(stroke)
    }

    public void addStroke(StrokeProvider stroke) {
        setStroke(stroke)
    }

    ComposableStroke leftShift(Stroke stroke) {
        addStroke(stroke)
    }

    ComposableStroke leftShift(StrokeProvider stroke) {
        addStroke(stroke)
    }

    void apply(GfxContext context) {
        if (stroke instanceof StrokeProvider) stroke.apply(context)
    }

    protected Stroke createStroke() {
        if (!stroke) {
            throw new IllegalArgumentException("${this}.stroke is null.")
        }

        def _s = stroke instanceof StrokeProvider ? stroke.getStroke() : stroke
        return new ZigzagStroke(_s, amplitude as float, wavelength as float)
    }
}
