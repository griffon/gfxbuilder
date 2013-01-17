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

import com.camick.awt.HSLColor
import griffon.builder.gfx.Colors

import java.awt.Color

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ColorFactory extends AbstractGfxFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value,
                              Map properties) throws InstantiationException, IllegalAccessException {
        if (value != null) {
            if (Color.class.isAssignableFrom(value.getClass())) {
                return value
            } else {
                return Colors.getColor(value)
            }
        }

        if (properties.red == null) properties.red = properties.remove("r")
        if (properties.green == null) properties.green = properties.remove("g")
        if (properties.blue == null) properties.blue = properties.remove("b")
        if (properties.alpha == null) properties.alpha = properties.remove("a")

        if (properties.containsKey("red") ||
            properties.containsKey("green") ||
            properties.containsKey("blue") ||
            properties.containsKey("alpha")) {

            def red = properties.remove("red")
            def green = properties.remove("green")
            def blue = properties.remove("blue")
            def alpha = properties.remove("alpha")

            red = red != null ? red : 0
            green = green != null ? green : 0
            blue = blue != null ? blue : 0
            alpha = alpha != null ? alpha : 255

            red = red > 1 ? red / 255 : red
            green = green > 1 ? green / 255 : green
            blue = blue > 1 ? blue / 255 : blue
            alpha = alpha > 1 ? alpha / 255 : alpha

            return new Color(red as float, green as float, blue as float, alpha as float)
        }

        return Color.BLACK
    }

    public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        // empty
    }

    public boolean isLeaf() {
        return true
    }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class HSLColorFactory extends AbstractGfxFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value,
                              Map properties) throws InstantiationException, IllegalAccessException {
        HSLColor hsl = new HSLColor(Color.BLACK)
        if (value != null) {
            if (Color.class.isAssignableFrom(value.getClass())) {
                return value
            } else if (HSLColor.class.isAssignableFrom(value.getClass())) {
                hsl = value
            } else {
                return Colors.getColor(value)
            }
        }

        if (properties.hue == null) properties.hue = properties.remove("h")
        if (properties.saturation == null) properties.saturation = properties.remove("s")
        if (properties.luminance == null) properties.luminance = properties.remove("l")
        if (properties.alpha == null) properties.alpha = properties.remove("a")

        if (properties.containsKey("hue") ||
            properties.containsKey("saturation") ||
            properties.containsKey("luminance") ||
            properties.containsKey("alpha")) {

            def h = properties.remove("hue")
            def s = properties.remove("saturation")
            def l = properties.remove("luminance")
            def a = properties.remove("alpha")
            def shade = properties.remove("shade")
            def tone = properties.remove("tone")

            def _h = h != null ? h : hsl.hue
            def _s = s != null ? s : hsl.saturation
            def _l = l != null ? l : hsl.luminance
            def _a = a != null ? a : hsl.alpha

            hsl = new HSLColor(_h as float, _s as float, _l as float, _a as float)
            if (shade != null) hsl = new HSLColor(hsl.adjustShade(shade as float))
            if (tone != null) hsl = new HSLColor(hsl.adjustTone(tone as float))
        }

        return hsl.getRGB()
    }

    public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        // empty
    }

    public boolean isLeaf() {
        return true
    }
}