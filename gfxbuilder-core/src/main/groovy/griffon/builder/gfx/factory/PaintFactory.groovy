/*
 * Copyright 2008-2013 the original author or authors.
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

import griffon.builder.gfx.Colors
import griffon.builder.gfx.MultiPaintProvider
import griffon.builder.gfx.PaintProvider
import griffon.builder.gfx.nodes.paints.BorderPaintNode
import griffon.builder.gfx.nodes.paints.ColorPaintNode
import griffon.builder.gfx.nodes.paints.MultiPaintNode

import java.awt.Color
import java.awt.Paint

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 * @author Alexander Klein <info@aklein.org>
 */
class MultiPaintFactory extends GfxBeanFactory {
    MultiPaintFactory() {
        super(MultiPaintNode, false)
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof PaintProvider) {
            parent.addPaint(child)
        }/* else {
         throw new RuntimeException("Node ${parent} does not accept nesting of ${child}.")
      }*/
    }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ColorPaintFactory extends AbstractGfxFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value,
                              Map properties) throws InstantiationException, IllegalAccessException {
        ColorPaintNode node = new ColorPaintNode()
        if (value != null && value instanceof Color || value instanceof String) {
            builder.context.color = Colors.getColor(value)
        }
        return node
    }

    public boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        if (attributes.containsKey("red") ||
            attributes.containsKey("green") ||
            attributes.containsKey("blue") ||
            attributes.containsKey("alpha")) {

            def red = attributes.remove("red")
            def green = attributes.remove("green")
            def blue = attributes.remove("blue")
            def alpha = attributes.remove("alpha")

            red = red != null ? red : 0
            green = green != null ? green : 0
            blue = blue != null ? blue : 0
            alpha = alpha != null ? alpha : 1

            red = red > 1 ? red / 255 : red
            green = green > 1 ? green / 255 : green
            blue = blue > 1 ? blue / 255 : blue
            alpha = alpha > 1 ? alpha / 255 : alpha

            attributes.color = new Color(red as float, green as float, blue as float, alpha as float)
        }

        Object color = attributes.get("color")

        if (color != null && color instanceof String) {
            attributes.put("color", Colors.getColor(color))
        }

        color = attributes.get("color")
        node.color = color == null ? builder.context.color : color

        return false
    }

    public boolean isLeaf() {
        return true
    }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class BorderPaintFactory extends AbstractGfxFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value,
                              Map properties) throws InstantiationException, IllegalAccessException {
        BorderPaintNode node = new BorderPaintNode()
        if (value != null &&
            (value instanceof PaintProvider || value instanceof MultiPaintProvider)) {
            node.paint = value
        }
        return node
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Paint || child instanceof PaintProvider || child instanceof MultiPaintProvider) {
            parent.paint = child
        } else {
            throw new IllegalArgumentException("$child can not be nested inside $parent")
        }
    }

    public boolean isLeaf() {
        return false
    }
}
