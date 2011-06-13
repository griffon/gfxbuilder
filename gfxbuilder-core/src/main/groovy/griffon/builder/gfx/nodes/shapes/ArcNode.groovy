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
import java.awt.geom.Arc2D
import griffon.builder.gfx.GfxAttribute

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ArcNode extends AbstractShapeGfxNode {
   @GfxAttribute double x = 0d
   @GfxAttribute double y = 0d
   @GfxAttribute(alias="w") double width = 10d
   @GfxAttribute(alias="h") double height = 10d
   @GfxAttribute double start = 0d
   @GfxAttribute double extent = 90d
   @GfxAttribute def close

   ArcNode() {
      super("arc")
   }

   ArcNode(Arc2D arc) {
      super("arc")
      x = arc.x
      y = arc.y
      width = arc.width
      height = arc.height
      start = arc.angleStart
      extent = arc.angleExtent
      close = arc.arcType
   }

    Shape calculateShape() {
        return new Arc2D.Double( x as double,
                                 y as double,
                                 width as double,
                                 height as double,
                                 start as double,
                                 extent as double,
                                 getCloseValue() )
    }

    private def getCloseValue() {
        if( !close ){
            return Arc2D.OPEN
        }
        if( close instanceof Number ){
          return close as int
        }else if( close instanceof String ){
           if( close.compareToIgnoreCase("OPEN") == 0 ){
               return Arc2D.OPEN
           }else if( close.compareToIgnoreCase("CHORD") == 0 ){
               return Arc2D.CHORD
           }else if( close.compareToIgnoreCase("PIE") == 0 ){
               return Arc2D.PIE
           }
        }
       return Arc2D.OPEN
    }
}
