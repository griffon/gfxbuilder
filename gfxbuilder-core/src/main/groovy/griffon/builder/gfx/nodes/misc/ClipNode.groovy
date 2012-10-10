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

package griffon.builder.gfx.nodes.misc

import griffon.builder.gfx.DrawableNode
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxNode

import java.awt.Shape
import java.awt.geom.AffineTransform
import java.beans.PropertyChangeEvent

import static java.lang.Math.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ClipNode extends GfxNode {
   @GfxAttribute(alias="s") def shape
   @GfxAttribute(alias="tx") double translateX = Double.NaN
   @GfxAttribute(alias="ty") double translateY = Double.NaN
   @GfxAttribute(alias="ra") double rotateAngle = Double.NaN
   @GfxAttribute(alias="sx") double scaleX = Double.NaN
   @GfxAttribute(alias="sy") double scaleY = Double.NaN

   ClipNode() {
      super("clip")
   }

   ClipNode(Shape shape) {
      super("clip")
      this.shape = shape
   }

   ClipNode(DrawableNode shape) {
      super("clip")
      this.shape = shape
   }

   void propertyChanged(PropertyChangeEvent event) {
      if(event.source == shape) {
         onDirty(event)
      } else {
         super.propertyChanged(event)
      }
   }

   void apply(GfxContext context) {
      if(!shape || !enabled) return
      def _shape = null
      if(shape instanceof DrawableNode && shape.enabled) {
         def rtm = shape.runtime ?: shape.createRuntime(context)
         _shape = rtm.localShape
      } else if(shape instanceof Shape) {
         _shape = shape
      }
      if(!_shape) return
      context.g.clip = getTransformedShape(_shape)
   }

   private Shape getTransformedShape(Shape shape) {
      AffineTransform transform = new AffineTransform()
      double x = shape.bounds.x
      double y = shape.bounds.y
      double cx = x + (shape.bounds.width/2)
      double cy = y + (shape.bounds.height/2)
      if(!Double.isNaN(ra)) {
         transform.rotate(toRadians(ra),cx, cy)
      }
      if(!Double.isNaN(tx) || !Double.isNaN(ty)) {
         double _tx = Double.isNaN(tx) ? 0 : tx
         double _ty = Double.isNaN(ty) ? 0 : ty
         if(!Double.isNaN(ra)) {
            double radians = toRadians(ra)
            double rtx =   (_tx * cos(radians)) - (_ty * sin(radians))
            double rty = -((_tx * sin(radians)) + (_ty * cos(radians)))
            _tx = rtx
            _ty = rty
         }
         transform.translate(_tx, _ty)
      }
      if(!Double.isNaN(sx) || !Double.isNaN(sy)) {
         double _sx = Double.isNaN(sx) ? 1 : sx
         double _sy = Double.isNaN(sy) ? 1 : sy
         double tsx = (cx - (cx*_sx)) / _sx
         double tsy = (cy - (cy*_sy)) / _sy
         transform.scale(_sx, _sy)
         transform.translate(tsx, tsy)
      }
      transform.createTransformedShape(shape)
   }
}
