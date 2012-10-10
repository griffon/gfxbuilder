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

import griffon.builder.gfx.GfxAttribute

import java.awt.Shape
import java.awt.geom.Rectangle2D
import java.awt.geom.RoundRectangle2D

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
final class RectangleNode extends AbstractShapeGfxNode {
   @GfxAttribute double x = 0d
   @GfxAttribute double y = 0d
   @GfxAttribute(alias="w") double width = 10d
   @GfxAttribute(alias="h") double height = 10d
   @GfxAttribute(alias="aw") double arcWidth = 0d
   @GfxAttribute(alias="ah") double arcHeight = 0d

   RectangleNode() {
       super( "rect" )
   }

   RectangleNode(Rectangle2D rectangle) {
       super( "rect" )
       x = rectangle.x
       y = rectangle.y
       width = rectangle.width
       height = rectangle.height
   }

   RectangleNode(RoundRectangle2D rectangle) {
       super( "rect" )
       x = rectangle.x
       y = rectangle.y
       width = rectangle.width
       height = rectangle.height
       arcHeight = rectangle.arcHeight
       arcWidth = rectangle.arcWidth
   }

   Shape calculateShape() {
      if(arcWidth && arcHeight){
         return new RoundRectangle2D.Double( x as double,
                                             y as double,
                                             width as double,
                                             height as double,
                                             arcWidth as double,
                                             arcHeight as double )
       }
       return new Rectangle2D.Double( x as double,
                                      y as double,
                                      width as double,
                                      height as double )
   }
}
