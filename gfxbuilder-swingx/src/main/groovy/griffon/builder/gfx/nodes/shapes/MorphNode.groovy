/*
 * Copyright 2007-2010 the original author or authors.
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

import java.awt.Shape
import java.awt.geom.AffineTransform
import java.beans.PropertyChangeEvent
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.ShapeProvider

import org.jdesktop.swingx.geom.Morphing2D

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class MorphNode extends AbstractShapeGfxNode {
   @GfxAttribute(alias="s1") def/*<Shape|ShapeProvider>*/ shape1
   @GfxAttribute(alias="s2") def/*<Shape|ShapeProvider>*/ shape2
   @GfxAttribute(alias="m") double morph = 0d

   MorphNode() {
      super("morph")
   }

   void propertyChanged(PropertyChangeEvent event) {
      if(event.source == s1 || event.source == s2) {
         onDirty(event)
      } else {
         super.propertyChanged(event)
      }
   }

   void addShape(Shape shape){
      if(!shape1){
         shape1 = shape
      } else if(!shape2){
         shape2 = shape
      }
   }

   void addShape(ShapeProvider shape){
      if(!shape1){
         shape1 = shape
      } else if(!shape2){
         shape2 = shape
      }
   }

   MorphNode leftShift(Shape shape) {
      addShape(shape)
      this
   }

   MorphNode leftShift(ShapeProvider shape) {
      addShape(shape)
      this
   }

   Shape calculateShape() {
      def _s1 = s1
      def _s2 = s2

      if(s1 instanceof ShapeProvider) {
         _s1 = s1.getRuntime(runtime.context).localShape
      }
      if(s2 instanceof ShapeProvider) {
         _s2 = s2.getRuntime(runtime.context).localShape
      }

      Morphing2D morphedShape = new Morphing2D(_s1, _s2)
      morphedShape.morphing = morph as double
      new AffineTransform().createTransformedShape(morphedShape)
   }
}