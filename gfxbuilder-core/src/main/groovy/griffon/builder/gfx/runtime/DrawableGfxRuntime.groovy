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

package griffon.builder.gfx.runtime

import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxNode

import java.awt.Shape
import java.awt.geom.AffineTransform
import java.beans.PropertyChangeEvent

import static java.lang.Math.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class DrawableGfxRuntime extends AbstractGfxRuntime {
    protected Shape _localShape
    protected Shape _transformedShape
    protected AffineTransform _localTransforms
    protected double _cx = Double.NaN
    protected double _cy = Double.NaN
    protected double _x = Double.NaN
    protected double _y = Double.NaN

    DrawableGfxRuntime(GfxNode node, GfxContext context) {
        super(node, context)
    }

    void reset(PropertyChangeEvent event = null) {
        if (event == null) {
            _localShape = null
            _transformedShape = null
            _localTransforms = null
            _cx = Double.NaN
            _cy = Double.NaN
            _x = Double.NaN
            _y = Double.NaN
        }
    }

    AffineTransform getLocalTransforms() {
        if (!_localTransforms) {
            def shape = getShape()
            AffineTransform transform = new AffineTransform()
            if (shape) {
                double x = shape.bounds.x
                double y = shape.bounds.y
                double cx = x + (shape.bounds.width / 2)
                double cy = y + (shape.bounds.height / 2)
                if (!Double.isNaN(_node.ra)) {
                    transform.rotate(toRadians(_node.ra), cx, cy)
                }
                if (!Double.isNaN(_node.tx) || !Double.isNaN(_node.ty)) {
                    double tx = Double.isNaN(_node.tx) ? 0 : _node.tx
                    double ty = Double.isNaN(_node.ty) ? 0 : _node.ty
                    if (!Double.isNaN(_node.ra)) {
                        double radians = toRadians(_node.ra)
                        double rtx = (tx * cos(radians)) - (ty * sin(radians))
                        double rty = -((tx * sin(radians)) + (ty * cos(radians)))
                        tx = rtx
                        ty = rty
                    }
                    transform.translate(tx, ty)
                }
                if (!Double.isNaN(_node.sx) || !Double.isNaN(_node.sy)) {
                    double sx = Double.isNaN(_node.sx) ? 1 : _node.sx
                    double sy = Double.isNaN(_node.sy) ? 1 : _node.sy
                    double tsx = (cx - (cx * sx)) / sx
                    double tsy = (cy - (cy * sy)) / sy
                    transform.scale(sx, sy)
                    transform.translate(tsx, tsy)
                }
            }
            _localTransforms = transform
        }
        _localTransforms
    }

    Shape getShape() {
        _node.getShape()
    }

    Shape getLocalShape() {
        if (!_localShape) {
            _localShape = getShape()
            if (_localShape) {
                _localShape = getLocalTransforms().createTransformedShape(_localShape)
            }
        }
        _localShape
    }

    Shape getTransformedShape() {
        if (!_transformedShape) {
            _transformedShape = getLocalShape()
            if (_transformedShape) {
                AffineTransform affineTransform = new AffineTransform()
                affineTransform.concatenate _context.g.transform
                _node.transforms.concatenateTo(affineTransform)
                _transformedShape = affineTransform.createTransformedShape(_transformedShape)
            }
        }
        _transformedShape
    }

    Shape getBoundingShape() {
        getTransformedShape()
    }

    double getCx() {
        if (Double.isNaN(_cx)) {
            def s = getTransformedShape()
            if (s) {
                _cx = s.bounds.x + (s.bounds.width / 2)
                _cy = s.bounds.y + (s.bounds.height / 2)
            }
        }
        _cx
    }

    double getCy() {
        if (Double.isNaN(_cy)) {
            def s = getTransformedShape()
            if (s) {
                _cx = s.bounds.x + (s.bounds.width / 2)
                _cy = s.bounds.y + (s.bounds.height / 2)
            }
        }
        _cy
    }

    double getX() {
        if (Double.isNaN(_x)) {
            def s = getTransformedShape()
            if (s) {
                _x = s.bounds.x
                _y = s.bounds.y
            }
        }
        _x
    }

    double getY() {
        if (Double.isNaN(_y)) {
            def s = getTransformedShape()
            if (s) {
                _x = s.bounds.x
                _y = s.bounds.y
            }
        }
        _y
    }
}