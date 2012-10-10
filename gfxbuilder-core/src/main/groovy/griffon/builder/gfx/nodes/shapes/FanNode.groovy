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
import org.codehaus.griffon.jsilhouette.geom.Fan

import java.awt.Shape
import java.awt.geom.Rectangle2D

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class FanNode extends AbstractShapeGfxNode {
    @GfxAttribute float cx = 10f
    @GfxAttribute float cy = 10f
    @GfxAttribute Shape blade = new Rectangle2D.Float(0,0,4,8)
    @GfxAttribute int blades = 2
    @GfxAttribute(alias="a") float angle = 0f
    @GfxAttribute(alias="bcx") float bladeCx = 0.5f

    FanNode() {
        super("fan")
    }

    FanNode(Fan fan) {
        super("fan")
        cx = fan.cx
        cy = fan.cy
        shape = fan.shape
        blades = fan.blades
        bladeCx = fan.bladeCx
        angle = fan.angle
    }

    Shape calculateShape() {
       return new Fan( cx as float,
                       cy as float,
                       shape,
                       blades as int,
                       sides as float,
                       bladeCx as float )
    }
}
