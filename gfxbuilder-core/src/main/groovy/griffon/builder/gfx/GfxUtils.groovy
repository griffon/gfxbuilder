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

package griffon.builder.gfx

import java.awt.geom.Area
import java.awt.image.BufferedImage
import java.awt.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class GfxUtils {
   static boolean hasProperty( Object target, String property ) {
      try {
         target."$property"
      } catch( MissingPropertyException e ) {
         return false
      }
      return true
   }

   /**
    * Finds out if <i>property</i> is a GfxAttribute annotated property on <i>target</i>.<p>
    * this method assumes that <i>property</i> is a field declared on the target's class.
    */
   static boolean isAttribute( Object target, String property ) {
      if( !target || !property ) return false
      try {
         def field = target.getClass().getDeclaredField(property)
         return field.getAnnotation(GfxAttribute)
      } catch( NoSuchFieldException nsfe ) {
         // ignore
      }
      false
   }

   static boolean compare( oldvalue, newvalue ) {
       if( oldvalue == null && newvalue == null ) return false
       if( oldvalue == null && newvalue != null ) return true
       if( oldvalue != null && newvalue == null ) return true

       switch( oldvalue.class ){
          case Boolean:
             if( newvalue instanceof String ) return (oldvalue as String) != newvalue
             if( newvalue instanceof Boolean ) return /*oldvalue != newvalue*/ !oldvalue.equals(newvalue)
             return true
             break;
          case String:
             if( newvalue instanceof Boolean ) return oldvalue != (newvalue as String)
             if( newvalue instanceof Color ) return Colors.getColor(oldvalue) != newvalue
             return /*oldvalue != newvalue*/ !oldvalue.equals(newvalue)
             break;
          case Color:
             if( newvalue instanceof Boolean ) return true
             if( newvalue instanceof String ) return oldvalue != Colors.getColor(newvalue)
             return /*oldvalue != newvalue*/ !oldvalue.equals(newvalue)
             break;
       }
       switch( newvalue.class ){
          case Boolean:
             if( oldvalue instanceof String ) return (newvalue as String) != oldvalue
             if( oldvalue instanceof Boolean ) return /*oldvalue != newvalue*/ !oldvalue.equals(newvalue)
             return true
             break;
          case String:
             if( oldvalue instanceof Boolean ) return newvalue != (oldvalue as String)
             if( oldvalue instanceof Color ) return Colors.getColor(newvalue) != oldvalue
             return /*oldvalue != newvalue*/ !oldvalue.equals(newvalue)
             break;
          case Color:
             if( oldvalue instanceof Boolean ) return true
             if( oldvalue instanceof String ) return newvalue != Colors.getColor(oldvalue)
             return /*oldvalue != newvalue*/ !oldvalue.equals(newvalue)
             break;
       }

       return /*oldvalue != newvalue*/ !oldvalue.equals(newvalue)
   }

   static void enhanceShapes() {
      def shapeMethods = Shape.metaClass.methods
      def methodMap = [
         'plus':'add',
         'minus':'subtract',
         'and':'intersect',
         'xor':'exclusiveOr'
      ]
      methodMap.each { op, method ->
         if( !shapeMethods.name.find{ it == op } ){
            Shape.metaClass."$op" << { Shape other ->
               def area = new Area(delegate)
               area."$method"( new Area(other) )
               return area
            }
         }
      }
   }

   static void enhanceColor() {
      def colorMethods = Color.metaClass.methods
      if( !colorMethods.name.find{ it == "derive" } ){
         Color.metaClass.derive = { Map props ->
            if( props.red == null ) props.red = props.remove("r")
            if( props.green == null ) props.green = props.remove("g")
            if( props.blue == null ) props.blue = props.remove("b")
            if( props.alpha == null ) props.alpha = props.remove("a")

            def red = props.red != null ? props.red: delegate.red
            def green = props.green != null ? props.green: delegate.green
            def blue = props.blue != null ? props.blue: delegate.blue
            def alpha = props.alpha != null ? props.alpha: delegate.alpha
            return new Color( (red > 1 ? red/255: red) as float,
                              (green > 1 ? green/255: green) as float,
                              (blue > 1 ? blue/255: blue) as float,
                              (alpha > 1 ? alpha/255: alpha) as float )
         }
      }
      if( !colorMethods.name.find{ it == "rgb" } ){
         Color.metaClass.rgb = {
            return delegate.getRGB()
         }
      }
   }

   static void enhanceBasicStroke(){
      def strokeMethods = BasicStroke.metaClass.methods
      if( !strokeMethods.name.find{ it == "derive" } ){
         BasicStroke.metaClass.derive << { Map props ->
            if( props.width == null ) props.width = props.remove("w")
            def width = props.width != null ? props.width : delegate.lineWidth
            def cap = props.cap != null ? props.cap : delegate.endCap
            def join = props.join != null ? props.join : delegate.lineJoin
            def miterlimit = props.miterlimit != null ? props.miterlimit : delegate.miterLimit
            def dash = props.dash != null ? props.dash : delegate.dashArray
            def dashphase = props.dashphase != null ? props.dashphase : delegate.dashPhase
            if( dash != null && dashphase != null ){
               return new BasicStroke( width as float,
                                       GfxUtils.getCapValue(cap),
                                       GfxUtils.getJoinValue(join),
                                       miterlimit as float,
                                       GfxUtils.getDashValue(dash),
                                       dashphase )
            }else{
               return new BasicStroke( width as float,
                                       GfxUtils.getCapValue(cap),
                                       GfxUtils.getJoinValue(join),
                                       miterlimit as float )
            }
         }
      }
   }

   static int getCapValue( cap ) {
      if( cap == null ) return BasicStroke.CAP_SQUARE
      if( cap instanceof Number ){
         return cap
      }else if( cap instanceof String ){
         if( "butt".compareToIgnoreCase( cap ) == 0 ){
             return BasicStroke.CAP_BUTT
         }else if( "round".compareToIgnoreCase( cap ) == 0 ){
            return BasicStroke.CAP_ROUND
         }else if( "square".compareToIgnoreCase( cap ) == 0 ){
            return BasicStroke.CAP_SQUARE
         }
         throw new IllegalArgumentException( "'cap=$cap' is not one of [butt,round,square]" )
      }
      throw new IllegalArgumentException( "'cap' value is not a String nor an int" )
   }

   static float[] getDashValue( dash ) {
      if( dash == null ) return null
      if( dash instanceof float[] ) return dash
      float[] array = new float[dash.size()]
      array.eachWithIndex { value, index ->
         if( value instanceof Number ){
             array[index] = value
         }else{
             throw new IllegalArgumentException( "dash[${index}] is not a Number" );
         }
      }
      return array
   }

   static int getJoinValue( join ) {
      if( join == null ) return BasicStroke.JOIN_MITER
      if( join instanceof Number ){
         return join
      }else if( join instanceof String ){
         if( "bevel".compareToIgnoreCase( join ) == 0 ){
            return BasicStroke.JOIN_BEVEL
         }else if( "round".compareToIgnoreCase( join ) == 0 ){
            return BasicStroke.JOIN_ROUND
         }else if( "miter".compareToIgnoreCase( join ) == 0 ){
            return BasicStroke.JOIN_MITER
         }
         throw new IllegalArgumentException( "'join=$join' is not one of [bevel,miter,round]" )
      }
      throw new IllegalArgumentException( "'join' value is not a String nor an int" )
   }

   static BufferedImage createCompatibleImage( int width, int height ) {
      return createCompatibleImage( width, height, false )
   }

   static BufferedImage createCompatibleImage( int width, int height, boolean withAlpha ) {
      if(GraphicsEnvironment.isHeadless()){
         return new BufferedImage(width as int,
                                  height as int,
                                  (withAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB) as int)
      } else {
         GraphicsConfiguration gc = GraphicsEnvironment.localGraphicsEnvironment.defaultScreenDevice.defaultConfiguration
         return gc.createCompatibleImage(width as int,
                                         height as int,
                                         (withAlpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE) as int)
      }
      throw new IllegalStateException("Couldn't create BufferedImage")
   }
}
