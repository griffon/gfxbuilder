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
import org.codehaus.griffon.jsilhouette.geom.Balloon

import java.awt.Shape

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class BalloonNode extends AbstractShapeGfxNode {
    @GfxAttribute float x = 0f
    @GfxAttribute float y = 0f
    @GfxAttribute(alias="w") float width = 20f
    @GfxAttribute(alias="h") float height = 20f
    @GfxAttribute(alias="a") float arc = 5f
    @GfxAttribute(alias="tw") float tabWidth = 5f
    @GfxAttribute(alias="th") float tabHeight
    @GfxAttribute(alias="tl") int tabLocation = Balloon.TAB_AT_BOTTOM
    @GfxAttribute(alias="td") float tabDisplacement = 0.5f
    @GfxAttribute(alias="ap") int anglePosition = Balloon.NONE

    BalloonNode() {
        super( "balloon" )
    }

    BalloonNode(Balloon balloon) {
        super( "balloon" )
        x = balloon.x
        y = balloon.y
        width = balloon.width
        height = balloon.height
        arc = balloon.arc
        tabWidth = balloon.tabWidth
        tabHeight = balloon.tabHeight
        tabLocation = balloon.tabLocation
        tabDisplacement = balloon.tabDisplacement
        anglePosition = balloon.anglePosition
    }

    Shape calculateShape() {
       def _tl = getTabLocation()
       def _ap = getAnglePosition()
       def _th = tabHeight == null ? tabWidth/2 : tabHeight
       return new Balloon( x as float,
                           y as float,
                           width as float,
                           height as float,
                           arc as float,
                           tabWidth as float,
                           _th as float,
                           _tl as int,
                           tabDisplacement as float,
                           _ap as int )
    }

    private def getTabLocation(){
       if( tabLocation == null ) return Balloon.TAB_AT_BOTTOM
       if( tabLocation instanceof Number ){
          return tabLocation.intValue()
       }
       if( tabLocation instanceof String ){
          switch( tabLocation ){
             case "bottom": return Balloon.TAB_AT_BOTTOM
             case "left": return Balloon.TAB_AT_LEFT
             case "right": return Balloon.TAB_AT_RIGHT
             case "top": return Balloon.TAB_AT_TOP
          }
       }
       return Balloon.TAB_AT_BOTTOM
    }

    private def getAnglePosition(){
       if( anglePosition == null ) return Balloon.NONE
       if( anglePosition instanceof Number ){
          return anglePosition.intValue()
       }
       if( anglePosition instanceof String ){
          switch( anglePosition ){
             case "start": return Balloon.ANGLE_AT_START
             case "end": return Balloon.ANGLE_AT_END
             default: return Balloon.NONE
          }
       }
       return Balloon.NONE
    }
}
