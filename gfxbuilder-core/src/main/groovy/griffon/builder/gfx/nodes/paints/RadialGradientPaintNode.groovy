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

package griffon.builder.gfx.nodes.paints

import java.awt.Color
import java.awt.Paint
import java.awt.RadialGradientPaint
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.awt.geom.AffineTransform
import java.awt.MultipleGradientPaint.CycleMethod
import java.awt.MultipleGradientPaint.ColorSpaceType

import static java.lang.Math.*
import java.beans.PropertyChangeEvent

import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.Transformable
import griffon.builder.gfx.nodes.transforms.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class RadialGradientPaintNode extends AbstractPaintNode implements MultipleGradientPaintProvider, Transformable {
   private List/*<GradientStop>*/ stops = []
   private Transforms _transforms

   @GfxAttribute double cx = Double.NaN
   @GfxAttribute double cy = Double.NaN
   @GfxAttribute double fx = Double.NaN
   @GfxAttribute double fy = Double.NaN
   @GfxAttribute(alias="r") double radius = Double.NaN
   @GfxAttribute(alias="c") def cycle = 'nocycle'
   @GfxAttribute(alias="f") boolean fit = true
   @GfxAttribute(alias="a") boolean absolute = true
   @GfxAttribute def linkTo

   RadialGradientPaintNode() {
      super("radialGradient")
      setTransforms(new Transforms())
   }

   RadialGradientPaintNode(RadialGradientPaintNode paint) {
      super("radialGradient")
//       setTransforms(new Transforms())
      cx = paint.centerPoint.x
      cy = paint.centerPoint.y
      fx = paint.focusPoint.x
      fy = paint.focusPoint.y
      radius = paint.radius
      cycle = paint.cycleMethod
      paint.colors.eachWithIndex { c, int i ->
         addStop(new GradientStop(offset: paint.fractions[i], color: c))
      }
   }

   void setTransforms(Transforms transforms) {
      def oldValue = _transforms
      if(_transforms) _transforms.removePropertyChangeListener(this)
      _transforms = transforms
      if(_transforms) _transforms.addPropertyChangeListener(this)
      firePropertyChange("transforms", oldValue, transforms)
   }

   Transforms getTransforms() {
      _transforms
   }

   Transforms getTxs() {
      _transforms
   }

   public List getStops(){
      return Collections.unmodifiableList(stops)
   }

   RadialGradientPaintNode clone() {
       RadialGradientPaintNode node = new RadialGradientPaintNode(cx: cx,
                                                                  cy: cy,
                                                                  fx: fx,
                                                                  fy: fy,
                                                                  r: r,
                                                                  cycle: cycle,
                                                                  fit: fit)
       stops.each{ stop -> node.addStop(stop.clone()) }
       node.transforms = _transforms.clone()
       return node
   }

   public void addStop(GradientStop stop) {
      if( !stop ) return
      boolean replaced = false
      int size = stops.size()
      for( index in (0..<size) ){
         if( stops[index].offset == stop.offset ){
            stops[index] = stop
            replaced = true
            break
         }
      }
      if( !replaced ) stops.add( stop )
      stop.addPropertyChangeListener(this)
   }

   public void propertyChange(PropertyChangeEvent event){
      if(event.source == _transforms || stops.contains(event.source)){
         onDirty(event)
      }else{
         super.propertyChange(event)
      }
   }

   void onDirty(PropertyChangeEvent event) {
      if(event.propertyName == "linkTo" && event.newValue instanceof MultipleGradientPaintProvider){
         even.newValue.stops.each { stop ->
            addStop(stop)
         }
      }
      super.onDirty(event)
   }

   Paint getPaint(Rectangle2D bounds) {
      double bcx = bounds.x + (bounds.width/2)
      double bcy = bounds.y + (bounds.height/2)

      double _cx = Double.isNaN(cx) ? bcx : cx + bounds.x
      double _cy = Double.isNaN(cy) ? bcy : cy + bounds.y
      double _fx = Double.isNaN(fx) ? _cx : fx + bounds.x
      double _fy = Double.isNaN(fy) ? _cy : fy + bounds.y
      double _r  = Double.isNaN(r)  ? min(bounds.width/2, bounds.height/2) : r
      double _rx = bounds.width/2
      double _ry = bounds.height/2
      double _sx = bounds.width/(_r*2)
      double _sy = bounds.height/(_r*2)
      double _tx = _cx-((_cx/_r)*_rx)
      double _ty = _cy-((_cy/_r)*_ry)

//       println([this,[cx,cy],[fx,fy],[r],bounds])
//       println([this,[_cx,_cy],[_fx,_fy],[_rx,_ry,_r],[_sx,_sy],[_tx,_ty]])

      AffineTransform transform = new AffineTransform()
      if(fit) {
         transform.concatenate AffineTransform.getTranslateInstance(_tx,_ty)
         transform.concatenate AffineTransform.getScaleInstance(_sx,_sy)
      }
      _transforms.concatenateTo(transform)

      stops = stops.sort { a, b -> a.offset <=> b.offset }
      int n = stops.size()
      float[] fractions = new float[n]
      Color[] colors = new Color[n]
      n.times { i ->
         GradientStop stop = stops[i]
         fractions[i] = stop.offset
         colors[i] = stop.color
         if(!Float.isNaN(stop.opacity)) colors[i] = colors[i].derive(alpha: stop.opacity)
      }

      return new RadialGradientPaint( new Point2D.Float(_cx as float, _cy as float),
                                      _r as float,
                                      new Point2D.Float(_fx as float, _fy as float),
                                      fractions,
                                      colors,
                                      getCycleMethod(),
                                      ColorSpaceType.SRGB,
                                      transform )
   }

   private def getCycleMethod() {
      if( cycle instanceof CycleMethod ){
         return cycle
      }else if( cycle instanceof String ){
         if( "nocycle".compareToIgnoreCase(cycle) == 0 || "pad".compareToIgnoreCase(cycle) == 0 ){
            return CycleMethod.NO_CYCLE
         }else if( "reflect".compareToIgnoreCase(cycle) == 0 ){
            return CycleMethod.REFLECT
         }else if( "repeat".compareToIgnoreCase(cycle) == 0 ){
            return CycleMethod.REPEAT
         }else{
            throw new IllegalStateException( "'cycle=" + cycle
                  + "' is not one of [nocycle,pad,reflect,repeat]" )
         }
      }
      throw new IllegalStateException( "'cycle' value is not a String nor a CycleMethod" );
   }
}
