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

package griffon.builder.gfx.nodes.paints

import griffon.builder.gfx.GfxAttribute

import java.awt.Paint
import java.awt.geom.AffineTransform
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D

import static java.lang.Math.abs

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 * @author Alexander Klein <info@aklein.org>
 */
abstract class AbstractLinearGradientPaintNode extends AbstractPaintNode {
    @GfxAttribute double x1 = Double.NaN
    @GfxAttribute double x2 = Double.NaN
    @GfxAttribute double y1 = Double.NaN
    @GfxAttribute double y2 = Double.NaN
    @GfxAttribute(alias = "c", resets = false) def cycle
    @GfxAttribute(alias = "s") boolean stretch = false
    @GfxAttribute(alias = "f") boolean fit = true

    AbstractLinearGradientPaintNode(String name) {
        super(name)
    }

    protected def getDefaultCycleValue() {
        false
    }

    public void setCycle(value) {
        this.@cycle = value
        if (value) {
            this.@stretch = false
            this.@fit = false
        }
    }

    public void setStretch(value) {
        this.@stretch = value
        if (value) {
            this.@fit = false
            this.@cycle = getDefaultCycleValue()
        }
    }

    public void setFit(value) {
        this.@fit = value
        if (value) {
            this.@stretch = false
            this.@cycle = getDefaultCycleValue()
        }
    }

    Paint getPaint(Rectangle2D bounds) {
        x1 = Double.isNaN(x1) ? bounds.x : x1
        y1 = Double.isNaN(y1) ? bounds.y : y1
        x2 = Double.isNaN(x2) ? bounds.x + bounds.width - 1 : x2
        y2 = Double.isNaN(y2) ? bounds.y : y2

        // TODO cache paint somehow
        if (x1 == x2) {
            // vertical
            def dy = stretch || fit ? bounds.height : abs(y2 - y1)
            if (y2 > y1) {
                return makePaint(bounds.x, bounds.y, bounds.x, bounds.y + dy)
            } else {
                return makePaint(bounds.x, bounds.y + bounds.height, bounds.x, bounds.y + bounds.height - dy)
            }
        }
        if (y1 == y2) {
            // horizontal
            def dx = stretch || fit ? bounds.width : abs(x2 - x1)
            if (x2 > x1) {
                return makePaint(bounds.x, bounds.y, bounds.x + dx, bounds.y)
            } else {
                return makePaint(bounds.x + bounds.width, bounds.y, bounds.x + bounds.width - dx, bounds.y)
            }
        }

        def corner = 1
        def scale = null
        def translate = null

        if (x2 > x1) {
            // left-right
            if (y2 > y1) {
                // up-down
                if (bounds.x != x1 || bounds.y != y1)
                translate = AffineTransform.getTranslateInstance(bounds.x, bounds.y)
            } else {
                // down-up
                corner = 2
                def y = bounds.y + bounds.height - (y1 - y2)
                if (bounds.x != x1 || y != y1)
                translate = AffineTransform.getTranslateInstance(bounds.x, y)
            }
        } else {
            // right-left
            if (y2 > y1) {
                // up-down
                corner = 3
                def x = bounds.x + bounds.width - (x1 - x2)
                if (bounds.x + bounds.width != x1 || bounds.y != y1)
                translate = AffineTransform.getTranslateInstance(x, bounds.y)
            } else {
                // down-up
                corner = 4
                def x = bounds.x + bounds.width - (x1 - x2)
                def y = bounds.y + bounds.height - (y1 - y2)
                if (bounds.x + bounds.width != x1 || y != y1)
                translate = AffineTransform.getTranslateInstance(x, y)
            }
        }

        if (fit) {
            scale = AffineTransform.getScaleInstance(bounds.height / abs(x2 - x1), bounds.width / abs(y2 - y1))
        }
        if (stretch) {
            scale = AffineTransform.getScaleInstance(bounds.width / abs(x2 - x1), bounds.height / abs(y2 - y1))
        }

        def line = new Line2D.Double(x1 as double, y1 as double, x2 as double, y2 as double)
        if (scale) line = scale.createTransformedShape(line)
        if (translate) line = translate.createTransformedShape(line)
        def b = line.bounds2D

        switch (corner) {
            case 1: return makePaint(bounds.x, bounds.y,
                    bounds.x + b.width, bounds.y + b.height)
            case 2: return makePaint(bounds.x, bounds.y + bounds.height,
                    bounds.x + b.width, bounds.y + bounds.height - b.height)
            case 3: return makePaint(bounds.x + bounds.width, bounds.y,
                    bounds.x + bounds.width - b.width, bounds.y + b.height)
            case 4: return makePaint(bounds.x + bounds.width, bounds.y + bounds.height,
                    bounds.x + bounds.width - b.width, bounds.y + bounds.height - b.height)
        }
    }

    protected abstract Paint makePaint(x1, y1, x2, y2)
}
