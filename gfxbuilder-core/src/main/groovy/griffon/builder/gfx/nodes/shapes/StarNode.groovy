/*
 * Copyright 2007-2013 the original author or authors.
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
import org.codehaus.griffon.jsilhouette.geom.Star

import java.awt.Shape

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class StarNode extends AbstractShapeGfxNode {
    @GfxAttribute float cx = 5f
    @GfxAttribute float cy = 5f
    @GfxAttribute float or = 8f
    @GfxAttribute float ir = 3f
    @GfxAttribute(alias = "cn") int count = 5i
    @GfxAttribute(alias = "a") float angle = 0f

    StarNode() {
        super("star")
    }

    StarNode(Star star) {
        super("star")
        cx = star.cx
        cy = star.cy
        or = star.or
        ir = star.ir
        count = star.count
        angle = star.angle
    }

    Shape calculateShape() {
        return new Star(cx as float,
            cy as float,
            or as float,
            ir as float,
            count as int,
            angle as float)
    }
}
