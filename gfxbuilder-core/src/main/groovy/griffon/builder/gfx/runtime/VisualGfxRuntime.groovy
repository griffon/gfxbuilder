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

package griffon.builder.gfx.runtime

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Paint
import java.awt.Shape
import java.awt.geom.Area
import java.beans.PropertyChangeEvent

import griffon.builder.gfx.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class VisualGfxRuntime extends DrawableGfxRuntime {
   protected def _fill
   protected def _paint
   protected def _borderColor
   protected def _borderWidth
   protected def _stroke
   protected Shape _boundingShape

   VisualGfxRuntime(GfxNode node, GfxContext context){
      super(node, context)
   }

   void reset(PropertyChangeEvent event = null) {
      super.reset(event)
      if(event == null) {
         _fill = null
         _paint = null
         _borderColor = null
         _borderWidth = null
         _stroke = null
         _boundingShape = null
         return
      }
      switch(event.propertyName) {
         case "fill":
            _fill = null
            break
         case "borderColor":
            _borderColor = null
            break
         case "borderWidth":
            _borderWidth = null
            _stroke = null
            break
      }
      switch(event.source) {
         case BorderPaintProvider: break;
         case PaintProvider:
         case MultiPaintProvider:
            _fill = null
            _paint = null
            break
         case StrokeProvider:
            _stroke = null;
      }
   }

   /**
    * Returns the borderColor taking into account inherited value from group.<p>
    *
    * @return a java.awt.Paint
    */
   public def getBorderColor(){
      if( !_borderColor ){
         _borderColor = _context.g.paint
         if( _context.groupSettings.borderColor != null ){
            _borderColor = _context.groupSettings.borderColor
         }
         if( _node.borderColor != null ){
            _borderColor = _node.borderColor
         }
         if( _borderColor instanceof Boolean && !_borderColor ){
            _borderColor = null
         }else{
            if( _borderColor instanceof String ){
               _borderColor = Colors.getColor(_borderColor)
            }
            if( !_borderColor ){
               _borderColor = _context.g.paint
            }
         }
      }
      _borderColor
   }

   /**
    * Returns the borderWidth taking into account inherited value from group.<p>
    *
    * @return an int
    */
   public def getBorderWidth(){
      if( _borderWidth == null ){
         _borderWidth = 1
         if( _context.groupSettings.borderWidth != null ){
            _borderWidth = _context.groupSettings.borderWidth
         }
         if( _node.borderWidth != null ){
            _borderWidth = _node.borderWidth
         }
      }
      _borderWidth
   }

   /**
    * Returns the fill to be used.<p>
    * Fill will be determined with the following order<ol>
    * <li>fill property (inherited from group too) if set to false</li>
    * <li>nested paint node</li>
    * <li>fill property (inherited from group too) if set to non null, non false</li>
    * </ol>
    *
    * @return either <code>false</code>/<code>null</code> (no fill), java.awt.Color, java.awt.Paint or MultiPaintProvider
    */
   public def getFill() {
      if( _fill == null ){
         if( _context.groupSettings.fill != null ){
            _fill = _context.groupSettings.fill
         }
         if( _node.fill != null ){
            _fill = _node.fill
         }

         def pp = getPaint()
         if( _fill instanceof Boolean && !_fill ){
            _fill = null
         }else if( pp != null ){
            //if( pp instanceof PaintProvider ){
            //   _fill = pp
            //}else if( pp instanceof MultiPaintProvider ){
               // let the caller handle it
               _fill = pp
            //}
         }else if( _fill ){
            switch( _fill ){
               case String:
                  _fill = Colors.getColor(_fill)
                  break
               case PaintProvider:
                  //_fill = _fill.runtime(context).paint
                  break
               case true:
                  _fill = _context?.g.paint
                  break
               case Color:
               case Paint:
                  /* do nothing */
                  break
            }
         }
      }
      _fill
   }

   /**
    * Returns a nested paint() node if any.<p>
    *
    * @return either a PaintProvider, MultipaintProvider or null if no nested paint() node is found
    */
   public def getPaint() {
      if( !_paint ){
          _node.nodes.each { n ->
              switch(n) {
                  case BorderPaintProvider: break;
                  case PaintProvider:
                  case MultiPaintProvider:
                      if(n.enabled) _paint = n
              }
          }
      }
      _paint
   }

   /**
    * Returns a java.awt.Stroke.<p>
    * Stroke will be determined with the following order<ol>
    * <li>nested stroke operation</li>
    * <li>borderWidth property (inherited from group too)</li>
    * <li>default stroke on Graphics object</li>
    * </ol>
    *
    * @return a java.awt.Stroke
    */
   public def getStroke() {
      if( _stroke == null ){
         def s = _node.findLast { it instanceof StrokeProvider }
         def bw = getBorderWidth()
         if(s && s.enabled){
            s.apply(_context)
            _stroke = s.getStroke()
         }else if( bw ){
            def ps = _context.g.stroke
            if( ps instanceof BasicStroke ){
               _stroke = ps.derive(width:bw)
            }else{
               _stroke = new BasicStroke( bw as float )
            }
         }else{
            _stroke = _context.g.stroke
         }
      }
      _stroke
   }

   /**
    * Returns the bounding shape including stroked border.<p>
    *
    * @return a java.awt.Shape
    */
   public Shape getBoundingShape() {
      if( !_boundingShape ){
         def s = getTransformedShape()
         if(s) {
            def st = getStroke()
            _boundingShape = new Area(s)
            _boundingShape.add(new Area(st.createStrokedShape(_boundingShape)))
         }
      }
      _boundingShape
   }
}