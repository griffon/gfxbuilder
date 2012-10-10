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

package griffon.builder.gfx.nodes.shapes

import griffon.builder.gfx.GfxAttribute
import org.codehaus.griffon.jsilhouette.geom.RegularPolygon

import java.awt.Shape

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class RegularPolygonNode extends AbstractShapeGfxNode {
    @GfxAttribute float cx = 5f
    @GfxAttribute float cy = 5f
    @GfxAttribute(alias="r") float radius = 5f
    @GfxAttribute int sides = 3i
    @GfxAttribute(alias="a") float angle = 0f

    RegularPolygonNode() {
        super("regularPolygon")
    }

    RegularPolygonNode(RegularPolygon regularPolygon) {
        super("regularPolygon")
        cx = regularPolygon.cx
        cy = regularPolygon.cy
        radius = regularPolygon.radius
        sides = regularPolygon.sides
        angle = regularPolygon.angle
    }

    Shape calculateShape() {
       return new RegularPolygon( cx as float,
                                  cy as float,
                                  radius as float,
                                  sides as int,
                                  angle as float )
    }
}
