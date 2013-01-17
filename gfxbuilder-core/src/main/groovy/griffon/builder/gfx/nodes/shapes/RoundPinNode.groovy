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
import org.codehaus.griffon.jsilhouette.geom.RoundPin

import java.awt.Shape

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class RoundPinNode extends AbstractShapeGfxNode {
    @GfxAttribute float cx = 5f
    @GfxAttribute float cy = 5f
    @GfxAttribute(alias = "r") float radius = 5f
    @GfxAttribute(alias = "h") float height = 5f
    @GfxAttribute(alias = "a") float angle = 0f

    RoundPinNode() {
        super("pin")
    }

    RoundPinNode(RoundPin pin) {
        super("pin")
        cx = pin.cx
        cy = pin.cy
        radius = pin.radius
        height = pin.height
        angle = pin.angle
    }

    Shape calculateShape() {
        return new RoundPin(cx as float,
            cy as float,
            radius as float,
            height as float,
            angle as float)
    }
}
