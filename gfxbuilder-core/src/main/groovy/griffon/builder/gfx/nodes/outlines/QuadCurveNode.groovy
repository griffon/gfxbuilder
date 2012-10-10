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

package griffon.builder.gfx.nodes.outlines

import griffon.builder.gfx.GfxAttribute

import java.awt.Shape
import java.awt.geom.QuadCurve2D

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class QuadCurveNode extends AbstractOutlineGfxNode {
    @GfxAttribute double x1
    @GfxAttribute double x2
    @GfxAttribute double y1
    @GfxAttribute double y2
    @GfxAttribute double ctrlx
    @GfxAttribute double ctrly

    QuadCurveNode() {
        super("quadCurve")
    }

    QuadCurveNode(QuadCurveNode, quadCurve) {
        super("quadCurve")
        x1 = quadCurve.x1
        x2 = quadCurve.x2
        y1 = quadCurve.y1
        y2 = quadCurve.y2
        ctrlx = quadCurve.ctrlx
        ctrly = quadCurve.ctrly
    }

    Shape calculateShape() {
       return new QuadCurve2D.Double( x1 as double,
                                      y1 as double,
                                      ctrlx as double,
                                      ctrly as double,
                                      x2 as double,
                                      y2 as double )
    }
}
