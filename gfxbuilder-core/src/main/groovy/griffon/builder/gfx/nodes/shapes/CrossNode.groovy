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
import org.codehaus.griffon.jsilhouette.geom.Cross

import java.awt.Shape

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class CrossNode extends AbstractShapeGfxNode {
    @GfxAttribute float cx = 5f
    @GfxAttribute float cy = 5f
    @GfxAttribute(alias="r") float radius = 2.5f
    @GfxAttribute(alias="a") float angle = 0f
    @GfxAttribute(alias="rn") float roundness = 0f

    CrossNode() {
        super("cross")
    }

    CrossNode(Cross cross) {
        super("cross")
        cx = cross.cx
        cy = cross.cy
        radius = cross.radius
        roundness = cross.roundness
        angle = cross.angle
    }

    Shape calculateShape() {
       return new Cross( cx as float,
                         cy as float,
                         radius as float,
                         width as float,
                         angle as float,
                         roundness as float )
    }
}
