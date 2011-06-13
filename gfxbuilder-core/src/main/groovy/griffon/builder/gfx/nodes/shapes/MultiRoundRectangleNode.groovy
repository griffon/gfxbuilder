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
import org.codehaus.griffon.jsilhouette.geom.MultiRoundRectangle
import griffon.builder.gfx.GfxAttribute

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class MultiRoundRectangleNode extends AbstractShapeGfxNode {
    @GfxAttribute float x = 0f
    @GfxAttribute float y = 0f
    @GfxAttribute(alias="w") float width = 10f
    @GfxAttribute(alias="h") float height = 10f
    @GfxAttribute(alias="tlw") float topLeftWidth
    @GfxAttribute(alias="tlh") float topLeftHeight
    @GfxAttribute(alias="trw") float topRightWidth
    @GfxAttribute(alias="trh") float topRightHeight
    @GfxAttribute(alias="blw") float bottomLeftWidth
    @GfxAttribute(alias="blh") float bottomLeftHeight
    @GfxAttribute(alias="brw") float bottomRightWidth
    @GfxAttribute(alias="brh") float bottomRightHeight

    MultiRoundRectangleNode() {
        super( "roundRect" )
    }

    MultiRoundRectangleNode(MultiRoundRectangle rect) {
        super( "roundRect" )
        x = rect.x
        x = rect.y
        x = rect.width
        x = rect.height
        topLeftWidth = rect.topLeftWidth
        topLeftHeight = rect.topLeftHeight
        topRightWidth = rect.topRightWidth
        topRightHeight = rect.topRightHeight
        bottomLeftWidth = rect.bottomLeftWidth
        bottomLeftHeight = rect.bottomLeftHeight
        bottomRightWidth = rect.bottomRightWidth
        bottomRightHeight = rect.bottomRightHeight
    }

    Shape calculateShape() {
       def _tlw = topLeftWidth != null ? topLeftWidth :
                    topLeftHeight != null ? topLeftHeight : 0
       def _tlh = topLeftHeight != null ? topLeftHeight :
                    topLeftWidth != null ? topLeftWidth : 0

       def _trw = topRightWidth != null ? topRightWidth :
                    topRightHeight != null ? topRightHeight : 0
       def _trh = topRightHeight != null ? topRightHeight :
                    topRightWidth != null ? topRightWidth : 0

       def _blw = bottomLeftWidth != null ? bottomLeftWidth :
                    bottomLeftHeight != null ? bottomLeftHeight : 0
       def _blh = bottomLeftHeight != null ? bottomLeftHeight :
                    bottomLeftWidth != null ? bottomLeftWidth : 0

       def _brw = bottomRightWidth != null ? bottomRightWidth :
                    bottomRightHeight != null ? bottomRightHeight : 0
       def _brh = bottomRightHeight != null ? bottomRightHeight :
                    bottomRightWidth != null ? bottomRightWidth : 0

       return new MultiRoundRectangle( x as float,
                                       y as float,
                                       width as float,
                                       height as float,
                                       _tlw as float,
                                       _tlh as float,
                                       _trw as float,
                                       _trh as float,
                                       _blw as float,
                                       _blh as float,
                                       _brw as float,
                                       _brh as float )
    }
}
