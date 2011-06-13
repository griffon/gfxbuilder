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

import java.awt.Color
import java.awt.Paint
import java.awt.GradientPaint
import java.awt.geom.Rectangle2D
import griffon.builder.gfx.Colors
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
final class ColorPaintNode extends AbstractPaintNode {
    @GfxAttribute(alias="c", resets=false) def/*Color|String*/ color = Color.BLACK

    ColorPaintNode(){
       super("colorPaint")
    }

    ColorPaintNode clone() {
       new ColorPaintNode(color: color)
    }

    Paint getPaint(Rectangle2D bounds) {
       if( color instanceof String ){
          return Colors.getColor( color )
       }else if(color instanceof Color){
          return color
       }
    }
}
