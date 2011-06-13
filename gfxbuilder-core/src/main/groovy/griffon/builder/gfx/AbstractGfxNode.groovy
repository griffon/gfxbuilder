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

package griffon.builder.gfx

import java.awt.AlphaComposite
import java.awt.Composite
import java.awt.Shape
import java.awt.Color
import java.awt.Paint
import java.awt.geom.Rectangle2D
import java.beans.PropertyChangeEvent

import griffon.builder.gfx.Colors
import griffon.builder.gfx.runtime.GfxRuntime
import griffon.builder.gfx.runtime.VisualGfxRuntime
import griffon.builder.gfx.nodes.transforms.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 * @author Alexander Klein <info@aklein.org>
 */
abstract class AbstractGfxNode extends AbstractDrawableContainerNode {
   //private ObservableMap _drag = new ObservableMap()
   private Shape _shape

   @GfxAttribute(alias="s", resets=false)  boolean asShape = false
   @GfxAttribute(alias="bc", resets=false) def/*Color*/ borderColor
   @GfxAttribute(alias="bw") double borderWidth = 1d
   @GfxAttribute(alias="f", resets=false)  def/*Color*/ fill
   //@GfxAttribute(alias="ad") boolean autoDrag = false
   //@GfxAttribute(alias="p", resets=false)  def/*Paint*/ paint = null
   //@GfxAttribute(alias="st") def/*Stroke*/ stroke = null

   AbstractGfxNode(String name) {
      super(name)
   }

   Shape getShape() {
      if(!_shape) {
         _shape = calculateShape()
      }
      _shape
   }

   GfxRuntime createRuntime(GfxContext context) {
      new VisualGfxRuntime(this, context)
   }

   abstract Shape calculateShape()

   protected boolean triggersReset(PropertyChangeEvent event) {
      switch(event.propertyName) {
         case "borderColor":
         case "borderWidth":
         case "fill":
            runtime?.reset(event)
      }
      return super.triggersReset(event)
   }

   protected void reset(PropertyChangeEvent event) {
      _shape = null
      runtime?.reset()
   }

//    void setBorderColor(String color) {
//       setBorderColor((Color) Colors.getColor(color))
//    }
// 
//    void setFill(String color) {
//       setFill((Color) Colors.getColor(color))
//    }

   protected void applyThisNode(GfxContext context) {
      if(shouldSkip(context)) return
      Shape shape = _runtime.getLocalShape()
      if(shape) {
         context.bounds = _runtime.getBoundingShape()?.bounds ?: context.bounds
         fill(context, shape)
         draw(context, shape)
      }
   }

   protected void applyNestedNode(GfxNode node, GfxContext context) {
      // node.apply(context)
   }

   protected void fill(GfxContext context, Shape shape){
       def __f = _runtime.fill
       def g = context.g

       switch( __f ){
          case Color:
             def color = g.color
             g.color = __f
             applyFill(context, shape)
             g.color = color
             break
          case Paint:
             def paint = g.paint
             g.paint = __f
             applyFill(context, shape)
             g.paint = paint
             break
          case PaintProvider:
             if(!__f.enabled) break
             def paint = g.paint
             g.paint = __f.getPaint(shape.bounds)
             applyFill(context, shape)
             g.paint = paint
             break
          case MultiPaintProvider:
             if(!__f.enabled) break
             __f.apply(context, shape)
             break
          default:
             // no fill
             break
       }
   }

   protected void applyFill(GfxContext context, Shape shape) {
        context.g.fill(shape)
   }

   protected void draw(GfxContext context, Shape shape) {
       def g = context.g
       def __bc = _runtime.borderColor
       def __bw = _runtime.borderWidth
       def __st = _runtime.stroke
       def __bp = findLast{ it instanceof BorderPaintProvider }

       if(__bp && __bp.paint) {
          if(!__bp.paint.enabled) return
          def __ss = __st.createStrokedShape(shape)
          if(__bp.paint instanceof MultiPaintProvider) {
             __bp.apply(context,__ss)
          } else {
             def __p = g.paint
             g.paint = __bp.getPaint(__ss.bounds2D)
             g.fill(__ss)
             g.paint = __p
          }
       } else if(__bc && __bw > 0) {
          def __pc = g.color
          def __ps = g.stroke
          g.color = __bc
          if(__st) g.stroke = __st
          g.draw(shape)
          g.color = __pc
          if(__st) g.stroke = __ps
       } else {
          // don't draw the shape
       }
   }
}
