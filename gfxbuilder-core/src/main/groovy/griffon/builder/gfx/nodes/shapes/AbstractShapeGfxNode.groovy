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

import griffon.builder.gfx.AbstractGfxNode
import griffon.builder.gfx.ShapeProvider

import java.awt.Shape
import java.awt.geom.Area

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
abstract class AbstractShapeGfxNode extends AbstractGfxNode implements ShapeProvider {
   public AbstractShapeGfxNode(String name) {
      super( name )
   }
    /* ===== OPERATOR OVERLOADING ===== */

    public Shape plus(ShapeProvider shape) {
       return plus(shape.getLocalShape())
    }
    public Shape plus(Shape shape) {
       def area = new Area(getLocalShape())
       area.add(shape instanceof Area ? shape :new Area(shape))
       return area
    }

    public Shape minus(ShapeProvider shape) {
       return minus(shape.getLocalShape())
    }
    public Shape minus(Shape shape) {
       def area = new Area(getLocalShape())
       area.subtract(shape instanceof Area ? shape :new Area(shape))
       return area
    }

    public Shape and(ShapeProvider shape) {
       return and(shape.getLocalShape())
    }
    public Shape and(Shape shape) {
       def area = new Area(getLocalShape())
       area.intersect(shape instanceof Area ? shape :new Area(shape))
       return area
    }

    public Shape xor(ShapeProvider shape) {
       return xor(shape.getLocalShape())
    }
    public Shape xor(Shape shape) {
       def area = new Area(getLocalShape())
       area.exclusiveOr(shape instanceof Area ? shape :new Area(shape))
       return area
    }
}
