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

package griffon.builder.gfx.nodes.shapes

import griffon.builder.gfx.GfxAttribute
import org.codehaus.griffon.jsilhouette.geom.Donut

import java.awt.Shape

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class DonutNode extends AbstractShapeGfxNode {
    @GfxAttribute float cx = 5f
    @GfxAttribute float cy = 5f
    @GfxAttribute float or = 5f
    @GfxAttribute float ir = 2f
    @GfxAttribute int sides = 0i
    @GfxAttribute(alias = "a") float angle = 0f

    DonutNode() {
        super("donut")
    }

    DonutNode(Donut donut) {
        super("donut")
        cx = donut.cx
        cy = donut.cy
        or = donut.or
        ir = donut.ir
        sides = donut.sides
        angle = donut.angle
    }

    Shape calculateShape() {
        return new Donut(cx as float,
            cy as float,
            or as float,
            ir as float,
            sides as int,
            angle as float)
    }
}
