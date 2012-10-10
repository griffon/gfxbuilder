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

package griffon.builder.gfx.nodes.paints

import griffon.builder.gfx.GfxAttribute

import java.awt.Color

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class GradientStop {
    @GfxAttribute(alias="c", resets=false) Color color
    @GfxAttribute(alias="s", resets=false) float offset = 0f
    @GfxAttribute(alias="o", resets=false) float opacity = Float.NaN
    @GfxAttribute(alias="n", resets=false) String name

//     public void setColor(String color) {
//        setColor((Color) Colors.getColor(color))
//     }

    GradientStop clone() {
       new GradientStop(color: color,
                        offset: offset,
                        opacity: opacity,
                        name: name)
    }

    public String toString(){
       return "stop[offset: $offset, color: $color, opacity: $opacity, name: $name]"
    }
}
