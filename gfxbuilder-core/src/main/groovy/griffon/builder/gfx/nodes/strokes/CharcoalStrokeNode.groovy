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

package griffon.builder.gfx.nodes.strokes

import java.awt.Stroke
import com.bric.awt.CharcoalStroke
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.StrokeProvider

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class CharcoalStrokeNode extends AbstractStrokeNode implements ComposableStroke {
    @GfxAttribute(alias="s")  Stroke stroke
    @GfxAttribute(alias="w")  float width = 1f
    @GfxAttribute(alias="sz") float size = 1f
    @GfxAttribute(alias="a")  float angle = (Math.PI/4) as float
    @GfxAttribute(alias="rs") int randomSeed = 0

    CharcoalStrokeNode() {
       super("charcoalStroke")
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
       if(stroke instanceof StrokeProvider) stroke.apply(context)
    }

    protected Stroke createStroke() {
       Stroke _s = stroke instanceof StrokeProvider ? stroke.getStroke() : stroke
       _s? new CharcoalStroke(_s, size as float, angle as float, randomSeed as int):
           new CharcoalStroke(width as float, angle as float, randomSeed as int)
    }
}
