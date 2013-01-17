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

import java.awt.Shape
import java.awt.geom.Ellipse2D

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class CircleNode extends AbstractShapeGfxNode {
    @GfxAttribute double x = Double.NaN
    @GfxAttribute double y = Double.NaN
    @GfxAttribute double cx = 5d
    @GfxAttribute double cy = 5d
    @GfxAttribute(alias = "r") double radius = 5d

    CircleNode() {
        super("circle")
    }

    CircleNode(Ellipse2D circle) {
        super("circle")
        radius = circle.width / 2
        x = circle.x
        y = circle.y
        cx = circle.x + radius
        cy = circle.y + radius
    }

    Shape calculateShape() {
        if (Double.isNaN(x) && Double.isNaN(y)) {
            return new Ellipse2D.Double((cx - radius) as double,
                (cy - radius) as double,
                (radius * 2) as double,
                (radius * 2) as double)
        }
        return new Ellipse2D.Double(x as double,
            y as double,
            (radius * 2) as double,
            (radius * 2) as double)
    }
}
