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

package griffon.builder.gfx.nodes.paints

import java.awt.Paint
import java.awt.Shape
import java.awt.geom.Rectangle2D

import java.beans.PropertyChangeEvent

import griffon.builder.gfx.GfxNode
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.PaintProvider
import griffon.builder.gfx.MultiPaintProvider

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
final class MultiPaintNode extends GfxNode implements MultiPaintProvider {
   private ObservableList/*<PaintProvider>*/ _paints = new ObservableList()

   MultiPaintNode() {
      super("multiPaint")
      _paints.addPropertyChangeListener(this)
   }

   public void addPaint(PaintProvider paint) {
      if(!paint || _paints.contains(paint)) return
      _paints << paint
      paint.addPropertyChangeListener( this )
      firePropertyChange("size", _paints.size()-1, _paints.size())
   }

   MultiPaintNode leftShift(PaintProvider paint) {
      addPaint(paint)
      this
   }

   public void removePaint(PaintProvider paint) {
       if(!paint) return
       _paints.remove( paint )
       paint.removePropertyChangeListener( this )
       firePropertyChange( "size", _paints.size()+1, _paints.size() )
   }

   public void propertyChange( PropertyChangeEvent event ){
      if( _paints.contains(event.source) ){
         onDirty(event)
      }else{
         super.propertyChange( event )
      }
   }

   void apply(GfxContext context) {}

   public void apply(GfxContext context, Shape shape){
      if(!shape) return
      def  p = context.g.paint
      _paints.each { paint ->
         if(!paint.enabled) return
         switch(paint) {
            case MultiPaintProvider:
               paint.apply(context, shape)
               break
            case PaintProvider:
               context.g.paint = paint.getPaint(shape.bounds2D)
               context.g.fill(shape)
               break
         }
      }
      context.g.paint = p
   }

   public List getPaints(){
      Collections.unmodifiableCollection(_paints)
   }

   public boolean isEmpty() {
      return _paints.isEmpty()
   }

   public void clear() {
      if( _paints.isEmpty() ) return
      int actualSize = _paints.size()
      _paints.clear()
      firePropertyChange("size", actualSize, 0)
   }

   public Iterator iterator() {
      return _paints.iterator()
   }

   public int getSize() {
      return _paints.size()
   }

   public PaintProvider getAt(int index) {
      return _paints[index]
   }

   public PaintProvider getAt(String name) {
      return _paints.find{ it?.name == name }
   }
}
