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
import java.awt.LinearGradientPaint
import java.awt.MultipleGradientPaint.ColorSpaceType
import java.awt.MultipleGradientPaint.CycleMethod
import java.awt.Paint
import java.awt.geom.AffineTransform
import java.awt.geom.Point2D
import java.beans.PropertyChangeEvent

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class LinearGradientPaintNode extends AbstractLinearGradientPaintNode implements MultipleGradientPaintProvider {
    private List/*<GradientStop>*/ stops = []
//    private Transforms _transforms

    @GfxAttribute
    def linkTo

    LinearGradientPaintNode() {
        super("linearGradient")
//       setTransforms(new Transforms())
        cycle = getDefaultCycleValue()
    }

    LinearGradientPaintNode(LinearGradientPaint paint) {
        super("linearGradient")
//       setTransforms(new Transforms())
        x1 = paint.startPoint.x
        y1 = paint.startPoint.y
        x2 = paint.endPoint.x
        y2 = paint.endPoint.y
        cycle = paint.cycleMethod
        paint.colors.eachWithIndex { c, int i ->
            addStop(new GradientStop(offset: paint.fractions[i], color: c))
        }
    }

    protected def getDefaultCycleValue() {
        'nocycle'
    }

    LinearGradientPaintNode clone() {
        LinearGradientPaintNode node = new LinearGradientPaintNode(x1: x1,
            x2: x2,
            y1: y1,
            y2: y2,
            cycle: cycle,
            stretch: stretch,
            fit: fit)
        stops.each { stop -> node.addStop(stop.clone()) }
        return node
    }

//    void setTransforms(Transforms transforms) {
//       def oldValue = _transforms
//       if(_transforms) _transforms.removePropertyChangeListener(this)
//       _transforms = transforms
//       if(_transforms) _transforms.addPropertyChangeListener(this)
//       firePropertyChange("transforms", oldValue, transforms)
//    }
// 
//    Transforms getTransforms() {
//       _transforms
//    }
// 
//    Transforms getTxs() {
//       _transforms
//    }

    public List getStops() {
        return Collections.unmodifiableList(stops)
    }

    public void addStop(GradientStop stop) {
        if (!stop) return
        boolean replaced = false
        int size = stops.size()
        for (index in (0..<size)) {
            if (stops[index].offset == stop.offset) {
                stops[index] = stop
                replaced = true
                break
            }
        }
        if (!replaced) stops.add(stop)
        stop.addPropertyChangeListener(this)
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (/*event.source == _transforms ||*/ stops.contains(event.source)) {
            onDirty(event)
        } else {
            super.propertyChange(event)
        }
    }

    void onDirty(PropertyChangeEvent event) {
        if (event.propertyName == "linkTo" && event.newValue instanceof MultipleGradientPaintProvider) {
            even.newValue.stops.each { stop ->
                addStop(stop)
            }
        }
        super.onDirty(event)
    }

    protected Paint makePaint(x1, y1, x2, y2) {
        stops = stops.sort { a, b -> a.offset <=> b.offset }
        int n = stops.size()
        float[] fractions = new float[n]
        Color[] colors = new Color[n]
        n.times { i ->
            GradientStop stop = stops[i]
            fractions[i] = stop.offset
            colors[i] = stop.color
            if (!Float.isNaN(stop.opacity)) colors[i] = colors[i].derive(alpha: stop.opacity)
        }

        AffineTransform transform = new AffineTransform()
//       _transforms.each { t ->
//          if(t.transform) transform.concatenate t.transform
//       }

        return new LinearGradientPaint(new Point2D.Double(x1, y1),
            new Point2D.Double(x2, y2),
            fractions,
            colors,
            getCycleMethod(),
            ColorSpaceType.SRGB,
            transform)
    }

    private def getCycleMethod() {
        if (!cycle) {
            return CycleMethod.NO_CYCLE
        } else if (cycle instanceof CycleMethod) {
            return cycle
        } else if (cycle instanceof String) {
            if ("nocycle".compareToIgnoreCase(cycle) == 0 || "pad".compareToIgnoreCase(cycle) == 0) {
                return CycleMethod.NO_CYCLE
            } else if ("reflect".compareToIgnoreCase(cycle) == 0) {
                return CycleMethod.REFLECT
            } else if ("repeat".compareToIgnoreCase(cycle) == 0) {
                return CycleMethod.REPEAT
            } else {
                throw new IllegalArgumentException("'cycle=" + cycle
                    + "' is not one of [nocycle, pad, reflect, repeat]")
            }
        }
        throw new IllegalArgumentException("'cycle' value is not a String nor a CycleMethod");
    }
}
