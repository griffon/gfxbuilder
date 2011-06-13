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

import java.beans.PropertyChangeEvent

import java.awt.Paint
import java.awt.GradientPaint
import java.awt.geom.Rectangle2D
import griffon.builder.gfx.Colors
import griffon.builder.gfx.PaintProvider
import griffon.builder.gfx.MultiPaintProvider
import griffon.builder.gfx.BorderPaintProvider
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
final class BorderPaintNode extends AbstractPaintNode implements BorderPaintProvider {
    @GfxAttribute(alias="p", resets=false) def/*Paint|PaintProvider*/ paint

    BorderPaintNode() {
        super("borderPaint")
    }

    void setProperty(String property, Object value) {
       if(property == "paint" || property == "p"){
          value = value instanceof PaintProvider || value instanceof MultiPaintProvider ? value.clone() : value
          super.setProperty(property, value)
       } else if( this.@paint != null ){
          this.@paint.setProperty(property, value)
       } else {
          throw new MissingPropertyException(property, BorderPaintNode)
       }
    }

    Object getProperty( String property ) {
       if(property == "paint" || property == "p"){
          return this.@paint
       }else if(this.@paint != null){
          return this.@paint.getProperty(property)
       }
       throw new MissingPropertyException(property, BorderPaintNode)
    }

    public void propertyChange(PropertyChangeEvent event) {
       if(event.source == this.@paint){
          onDirty(event)
       }else{
          super.propertyChange(event)
       }
    }

    Paint getPaint(Rectangle2D bounds) {
       if(!paint ) throw new IllegalStateException("borderPaint.paint is null!")
       if(paint instanceof MultiPaintProvider) return null
       if(!(paint instanceof PaintProvider)){
          throw new IllegalStateException("borderPaint.paint is not a PaintProvider!")
       }
       return paint.getPaint(bounds)
    }

    void apply(GfxContext context) {}
}
