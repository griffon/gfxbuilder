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
import org.codehaus.griffon.jsilhouette.geom.Arrow
import griffon.builder.gfx.GfxAttribute

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ArrowNode extends AbstractShapeGfxNode {
    @GfxAttribute float x = 0d
    @GfxAttribute float y = 0d
    @GfxAttribute(alias="w") float width = 16d
    @GfxAttribute(alias="h") float height = 10d
    @GfxAttribute(alias="r") float rise
    @GfxAttribute(alias="d") float depth
    @GfxAttribute(alias="a") float angle = 0d

    ArrowNode() {
        super("arrow")
    }

    ArrowNode(Arrow arrow) {
        super("arrow")
        x = arrow.x
        y = arrow.y
        width = arrow.width
        rise = arrow.rise
        depth = arrow.depth
        angle = arrow.angle
    }

    Shape calculateShape() {
       def _r = rise != null ? rise : 0.5
       def _d = depth != null ? depth : 0.5
       def _a = angle != null ? angle : 0
       return new Arrow( x as float,
                         y as float,
                         width as float,
                         height as float,
                         _r as float,
                         _d as float,
                         _a as float )
    }
}
