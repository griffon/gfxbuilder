/*
 * Copyright 2007-2012 the original author or authors.
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

import com.bric.awt.CalligraphyStroke
import griffon.builder.gfx.GfxAttribute

import java.awt.Stroke

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class CalligraphyStrokeNode extends AbstractStrokeNode {
    @GfxAttribute(alias="w") float width = 1f
    @GfxAttribute(alias="a") float angle = Math.toRadians(135) as float

    CalligraphyStrokeNode() {
       super("calligraphyStroke")
    }

    protected Stroke createStroke() {
        return new CalligraphyStroke(width, Math.toRadians(angle) as float)
    }
}
