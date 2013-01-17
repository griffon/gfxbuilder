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

package griffon.builder.gfx.nodes.paints

import griffon.builder.gfx.GfxAttribute

import java.awt.Color
import java.awt.GradientPaint
import java.awt.Paint
import java.awt.geom.Point2D

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class GradientPaintNode extends AbstractLinearGradientPaintNode {
    @GfxAttribute(alias = "c1", resets = false)
    def color1 = Color.BLACK
    @GfxAttribute(alias = "c2", resets = false)
    def color2 = Color.WHITE

    GradientPaintNode() {
        super("gradientPaint")
        cycle = getDefaultCycleValue()
    }

    GradientPaintNode(GradientPaint paint) {
        super("gradientPaint")
        cycle = getDefaultCycleValue()
        x1 = paint.point1.x
        y1 = paint.point1.y
        x2 = paint.point2.x
        y2 = paint.point2.y
        color1 = paint.color1
        color2 = paint.color2
        cycle = paint.cyclic
    }

//     void setColor1(String color) {
//         setColor1((Color) Colors.getColor(color))
//     }
// 
//     void setColor2(String color) {
//         setColor2((Color) Colors.getColor(color))
//     }

    GradientPaintNode clone() {
        new GradientPaintNode(color1: color1,
            color2: color2,
            x1: x1,
            x2: x2,
            y1: y1,
            y2: y2,
            cycle: cycle,
            stretch: stretch,
            fit: fit)
    }

    protected Paint makePaint(x1, y1, x2, y2) {
        return new GradientPaint(new Point2D.Double(x1, y1),
            color1,
            new Point2D.Double(x2, y2),
            color2,
            cycle as boolean)
    }
}
