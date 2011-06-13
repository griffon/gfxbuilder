/*
 * Copyright 2008-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.builder.gfx.factory

import griffon.builder.gfx.nodes.misc.FontNode
import griffon.builder.gfx.nodes.misc.GroupNode
import griffon.builder.gfx.nodes.shapes.TextNode
import griffon.builder.gfx.nodes.strokes.TextStrokeNode

import java.awt.Font

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class FontFactory extends AbstractGfxFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value,
            Map properties ) throws InstantiationException, IllegalAccessException {
        FontNode node = new FontNode()
        if( value != null && Font.class.isAssignableFrom( value.class ) ){
            node.font = value
            return node
        }

        def face = properties.remove( "face" )
        face = face != null ? face : "Default"
        def style = properties.remove( "style" )
        style = style != null ? getStyle(style): Font.PLAIN
        def size = properties.remove( "size" )
        size = size ? size : 12
        node.font = new Font( face, style as int, size as int )

        return node
    }

    public void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
       if( parent instanceof GroupNode || parent instanceof TextNode ) {
          parent << child
       } else if( parent instanceof TextStrokeNode ){
          parent.font = child
       } else {
          throw new IllegalArgumentException("font() can only be nested in [group, text, textStroke]")
       }
    }

    public boolean isLeaf(){
        return true
    }

    private def getStyle( style ){
       if( style instanceof String ){
          def s = Font.PLAIN
          style.split(/\|/).each { w ->
             if( w.equalsIgnoreCase("plain") ){ s |= Font.PLAIN }
             else if( w.equalsIgnoreCase("bold") ){ s |= Font.BOLD }
             else if( w.equalsIgnoreCase("italic") ) s |= Font.ITALIC
          }
          return s
       }
       return style
    }
}