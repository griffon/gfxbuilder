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

package griffon.builder.gfx.nodes.strokes

import com.jhlabs.awt.ShapeStroke
import griffon.builder.gfx.DrawableNode
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext

import java.awt.Shape
import java.awt.Stroke
import java.beans.PropertyChangeEvent

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ShapeStrokeNode extends AbstractStrokeNode {
    @GfxAttribute List shapes = []
    @GfxAttribute(alias = "ad") float advance = 10f

    ShapeStrokeNode() {
        super("shapeStroke")
    }

    void addShape(Shape shape) {
        if (shape) shapes << shape
    }

    void addShape(DrawableNode node) {
        if (node) {
            node.addPropertyChangeListener(this)
            shapes << node
        }
    }

    ShapeStrokeNode leftShift(Shape shape) {
        addShape(shape)
    }

    ShapeStrokeNode leftShift(DrawableNode node) {
        addShape(node)
    }

    void propertyChanged(PropertyChangeEvent event) {
        if (shapes.contains(event.source)) {
            onDirty(event)
        } else {
            super.propertyChanged(event)
        }
    }

    void apply(GfxContext context) {
        shapes.each { shape ->
            if (shape instanceof DrawableNode) {
                shape.getRuntime(context)
            }
        }
    }

    protected Stroke createStroke() {
        if (!shapes) throw new IllegalArgumentException("shapeStroke requires at least one shape.")
        def s = []
        shapes.each { shape ->
            if (shape instanceof Shape) s << shape
            if (shape instanceof DrawableNode && shape.enabled) {
                def _s = shape.runtime.localShape
                if (_s) s << _s
            }
        }

        return new ShapeStroke(s as Shape[], advance as float)
    }
}
