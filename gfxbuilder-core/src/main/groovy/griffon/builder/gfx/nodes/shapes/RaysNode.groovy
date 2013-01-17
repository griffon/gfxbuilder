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
import org.codehaus.griffon.jsilhouette.geom.Rays

import java.awt.Shape

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class RaysNode extends AbstractShapeGfxNode {
    @GfxAttribute float cx = 5f
    @GfxAttribute float cy = 5f
    @GfxAttribute(alias = "r") float radius = 5f
    @GfxAttribute(alias = "rs") int rays = 2i
    @GfxAttribute(alias = "a") float angle = 0f
    @GfxAttribute(alias = "ex") float extent = 0.5f
    @GfxAttribute(alias = "rn") boolean rounded = false

    RaysNode() {
        super("rays")
    }

    RaysNode(Rays rays) {
        super("rays")
        cx = rays.cx
        cy = rays.cy
        radius = rays.radius
        rays = rays.rays
        extent = rays.extent
        angle = rays.angle
        rounded = rays.rounded
    }

    Shape calculateShape() {
        return new Rays(cx as float,
            cy as float,
            radius as float,
            rays as int,
            angle as float,
            extent as float,
            rounded as boolean)
    }
}
