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

package griffon.builder.gfx.nodes.outlines

import java.awt.Shape
import java.awt.geom.CubicCurve2D
import griffon.builder.gfx.GfxAttribute

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class CubicCurveNode extends AbstractOutlineGfxNode {
    @GfxAttribute double x1
    @GfxAttribute double x2
    @GfxAttribute double y1
    @GfxAttribute double y2
    @GfxAttribute double ctrlx1
    @GfxAttribute double ctrly1
    @GfxAttribute double ctrlx2
    @GfxAttribute double ctrly2

    CubicCurveNode() {
        super("cubicCurve")
    }

    CubicCurveNode(CubicCurve2D, cubicCurve) {
        super("cubicCurve")
        x1 = cubicCurve.x1
        x2 = cubicCurve.x2
        y1 = cubicCurve.y1
        y2 = cubicCurve.y2
        ctrlx1 = cubicCurve.ctrlx1
        ctrly1 = cubicCurve.ctrly1
        ctrlx2 = cubicCurve.ctrlx2
        ctrly2 = cubicCurve.ctrly2
    }

    Shape calculateShape() {
       return new CubicCurve2D.Double( x1 as double,
                                       y1 as double,
                                       ctrlx1 as double,
                                       ctrly1 as double,
                                       ctrlx2 as double,
                                       ctrly2 as double,
                                       x2 as double,
                                       y2 as double )
    }
}
