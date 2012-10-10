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

import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.ShapeProvider

import java.awt.Shape
import java.awt.geom.Area

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class AreaNode extends AbstractShapeGfxNode {
    private final String areaMethod

    AreaNode(String name, String methodName) {
        super("area-"+name)
        this.areaMethod = methodName
    }

    Shape calculateShape() {
        def shapeNodes = nodes.findAll{ it instanceof ShapeProvider }
        if( !shapeNodes ) {
           throw new IllegalArgumentException("No nested shapes on ${this}")
        }

        GfxContext context = getRuntime().getContext()
        List shapes = []
        for(node in shapeNodes) {
           if(!node.visible || !node.enabled) continue
           if(!node.getRuntime()) node.getRuntime(context)
           def s = node.runtime.localShape
           if(s) shapes << s
        }

        if(!shapes) return null
        if(shapes.size() == 1) return shapes[0]
        Area area = new Area(shapes[0])
        shapes[1..-1].each { s ->
           area."$areaMethod"(s instanceof Area ? s : new Area(s))
        }
        area
    }
}
