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
import org.codehaus.griffon.jsilhouette.geom.Triangle

import java.awt.Shape

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class TriangleNode extends AbstractShapeGfxNode {
    @GfxAttribute float x = 10f
    @GfxAttribute float y = 10f
    @GfxAttribute(alias="w") float width = 10f
    @GfxAttribute(alias="h") float height = Float.NaN
    @GfxAttribute(alias="a") float angle = 0f
    @GfxAttribute(alias="ap") int anglePosition = Triangle.NONE
    @GfxAttribute(alias="rc") boolean rotateAtCenter

    public TriangleNode() {
        super( "triangle" )
    }

    public TriangleNode(Triangle triangle) {
        super( "triangle" )
        x = triangle.x
        y = triangle.y
        width = triangle.width
        angle = triangle.angle
        anglePosition = triangle.anglePosition
        rotateAtCenter = triangle.rotateAtCenter
    }

    Shape calculateShape() {
       if( anglePosition != Triangle.NONE ){
          return calculateRightTriangle()
       } else if( !Float.isNaN(height) ){
          return calculateIsoscelesTriangle()
       } else {
          return calculateEquilateralTriangle()
       }
    }

    private Shape calculateEquilateralTriangle(){
       def _a = angle != null ? angle : 0
       return new Triangle( x as float,
                            y as float,
                            width as float,
                            _a as float,
                            rotateAtCenter ? true : false )
    }

    private Shape calculateIsoscelesTriangle(){
       def _h = height != null ? height : Double.NaN
       def _a = angle != null ? angle : 0
       return new Triangle( x as float,
                            y as float,
                            width as float,
                            _a as float,
                            _h as float,
                            rotateAtCenter ? true : false )
    }

    private Shape calculateRightTriangle(){
       def ap = null
       switch( anglePosition ){
          case Triangle.ANGLE_AT_START:
          case Triangle.ANGLE_AT_END:
             ap = anglePosition
             break
          case 'start':
             ap = Triangle.ANGLE_AT_START
             break
          case 'end':
             ap = Triangle.ANGLE_AT_END
             break
          default:
             throw new IllegalArgumentException("rightAngleAt must be one of ['start','end',"+
                          "Triangle.ANGLE_AT_START,Triangle.ANGLE_AT_END]")
       }

       def _h = height != null ? height : Double.NaN
       def _a = angle != null ? angle : 0
       return new Triangle( x as float,
                            y as float,
                            width as float,
                            _a as float,
                            ap,
                            _h as float,
                            rotateAtCenter ? true : false )
    }
}
