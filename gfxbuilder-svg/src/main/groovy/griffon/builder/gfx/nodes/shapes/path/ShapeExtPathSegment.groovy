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

package griffon.builder.gfx.nodes.shapes.path

import java.awt.Shape
import org.apache.batik.ext.awt.geom.ExtendedGeneralPath
import java.beans.PropertyChangeEvent
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.DrawableNode

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ShapeExtPathSegment extends AbstractExtPathSegment {
    @GfxAttribute def shape
    @GfxAttribute boolean connect = false

    ShapeExtPathSegment(){
       super("xshapeTo")
    }

    public void setShape(Shape shape){
       shapes = shape
    }

    public void setShape(DrawableNode node){
       if(shape instanceof DrawableNode) {
          shape.removePropertyChangeListener(this)
       }
       node.addPropertyChangeListener(this)
       shape = node
    }

   void propertyChanged(PropertyChangeEvent event) {
      if(event.source == shape) {
         onDirty(event)
      } else {
         super.propertyChanged(event)
      }
   }

   void apply(GfxContext context) {
      if(shape instanceof DrawableNode) {
         shape.getRuntime(context)
      }
   }

   void apply(ExtendedGeneralPath path) {
      if(shape instanceof Shape) {
         path.append(shape, connect as boolean)
      } else {
         path.append(shape.runtime.localShape, connect as boolean)
      }
   }
}