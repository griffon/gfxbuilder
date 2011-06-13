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
import java.awt.geom.GeneralPath
import java.beans.PropertyChangeEvent
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.nodes.shapes.path.*
import org.apache.batik.ext.awt.geom.ExtendedGeneralPath

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ExtPathNode extends AbstractShapeGfxNode  {
   private ObservableList _segments = []

   @GfxAttribute(alias="wn") int winding
   @GfxAttribute(alias="cl") boolean close

   ExtPathNode() {
      super("path")
      _segments.addPropertyChangeListener(this)
   }

   void propertyChanged(PropertyChangeEvent event) {
      if(event.source == _segments || event.source instanceof ExtPathSegment) {
         onDirty(event)
      } else {
         super.propertyChanged(event)
      }
   }

   public void addPathSegment(ExtPathSegment segment ) {
      if( !segment ) return
      _segments << segment
      segment.addPropertyChangeListener(this)
   }

   Shape calculateShape()  {
      if( _segments.size() > 0 && !(_segments[0] instanceof MoveToExtPathSegment) ){
         throw new IllegalStateException("You must call 'moveTo' as the first segment of a path")
      }
      ExtendedGeneralPath path = new ExtendedGeneralPath(getWindingRule())
      _segments.each { segment ->
         segment.apply(runtime.context)
         segment.apply(path)
      }
      if(close){
         path.closePath()
      }
      return path
   }

   private int getWindingRule() {
      if( winding == null ){
         return GeneralPath.WIND_NON_ZERO
      }

      if( winding instanceof Integer ){
         return winding
      }else if( winding instanceof String ){
         if( "non_zero".compareToIgnoreCase( winding ) == 0 ||
             "nonzero".compareToIgnoreCase( winding ) == 0 ){
            return GeneralPath.WIND_NON_ZERO
         }else if( "even_odd".compareToIgnoreCase( winding ) == 0 ||
                   "evenodd".compareToIgnoreCase( winding ) == 0){
            return GeneralPath.WIND_EVEN_ODD
         }else{
            throw new IllegalStateException("'winding=$winding' is not one of [non_zero,even_odd]")
         }
      }
      throw new IllegalStateException("'winding' value is not a String nor an Integer")
   }
}