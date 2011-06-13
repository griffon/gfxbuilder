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

import java.awt.Polygon
import java.awt.Shape
import java.beans.PropertyChangeEvent
import griffon.builder.gfx.GfxAttribute

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class PolygonNode extends AbstractShapeGfxNode {
    @GfxAttribute List points

    PolygonNode() {
        super("polygon")
    }

    PolygonNode(Polygon polygon) {
        super("polygon")
        def ps = []
        for(int i = 0; i < polygon.xpoints.length; i++) {
           ps << polygon.xpoints[i]
           ps << polygon.ypoints[i]
        }
        points = ps
    }

    Shape calculateShape() {
       if( points.size() == 0 ){
           return null
       }

       if( points.size() % 2 == 1 ){
           throw new IllegalStateException("Odd number of points")
       }

       int npoints = points.size() / 2
       int[] xpoints = new int[npoints]
       int[] ypoints = new int[npoints]
       npoints.times { i ->
           Object ox = points.get( 2 * i )
           Object oy = points.get( (2 * i) + 1 )
           xpoints[i] = convertToInteger( ox, 2 * 1 )
           ypoints[i] = convertToInteger( oy, (2 * i) + 1 )
       }
       return new Polygon(xpoints, ypoints, npoints)
    }

    private int convertToInteger( Object obj, int index ) {
       int point = 0
       if( obj == null ){
           throw new IllegalStateException( ((index % 2 == 0) ? "x" : "y") + "[" + index+ "] is null" )
       }
       if( obj instanceof Number ){
           point = obj.intValue()
       }else{
           throw new IllegalStateException( ((index % 2 == 0) ? "x" : "y") + "[" + index + "] is not a number" )
       }
       return point
    }
}
