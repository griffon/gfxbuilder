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
import java.awt.geom.Ellipse2D
import griffon.builder.gfx.GfxAttribute

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class EllipseNode extends AbstractShapeGfxNode {
    @GfxAttribute double x = Double.NaN
    @GfxAttribute double y = Double.NaN
    @GfxAttribute double cx = 10d
    @GfxAttribute double cy = 5d
    @GfxAttribute(alias="rx") double radiusX = 10d
    @GfxAttribute(alias="ry") double radiusY = 5d

    EllipseNode() {
        super("ellipse")
    }

    EllipseNode( Ellipse2D ellipse ) {
        super( "circle" )
        x = ellipse.x
        y = ellipse.y
        radiusX = ellipse.width/2
        radiusY = ellipse.height/2
        cx = ellipse.x + radiusX
        cy = ellipse.y + radiusY
    }

    Shape calculateShape() {
       if(Double.isNaN(x) && Double.isNaN(y)) {
           return new Ellipse2D.Double( (cx - radiusX) as double,
                                        (cy - radiusY) as double,
                                        (radiusX * 2) as double,
                                        (radiusY * 2) as double )
       }
       return new Ellipse2D.Double( x as double,
                                    y as double,
                                    (radiusX * 2) as double,
                                    (radiusY * 2) as double )
    }
}
