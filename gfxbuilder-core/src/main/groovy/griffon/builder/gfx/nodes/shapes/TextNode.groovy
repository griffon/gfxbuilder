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

import java.awt.Font
import java.awt.Shape
import java.awt.font.TextLayout
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Rectangle2D
import java.beans.PropertyChangeEvent

import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.nodes.misc.FontNode

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class TextNode extends AbstractShapeGfxNode {
    public static final int LEFT = 0
    public static final int CENTER = 1
    public static final int RIGHT = 2
    public static final int TOP = 0
    public static final int MIDDLE = 1
    public static final int BASELINE = 2
    public static final int BOTTOM = 3

    private Shape outline

    @GfxAttribute(alias="t") String text = "Groovy"
    @GfxAttribute double x = 0d
    @GfxAttribute double y = 0d
    @GfxAttribute double cx = Double.NaN
    @GfxAttribute double cy = Double.NaN
    @GfxAttribute double spacing = 0d
    @GfxAttribute def halign = 'left'
    @GfxAttribute def valign = 'bottom'

    TextNode() {
        super("text")
    }

    protected boolean shouldSkip(GfxContext context){
        FontNode fontNode = nodes.reverse().find{ it instanceof FontNode }
        fontNode?.apply(context)
        return super.shouldSkip(context)
    }

    Shape calculateShape() {
        def __t = text.split(/\n/)
        if(__t.size() == 1){
           // single row text
           return center(calculateSingleRowOutline())
        }else{
           // multiple row text
           return center(calculateMultipleRowOutline(context, __t))
        }
    }

    private Shape calculateSingleRowOutline() {
       GfxContext context = getRuntime()?.getContext()
       if(!context) return null

       FontNode fontNode = nodes.reverse().find{ it instanceof FontNode }
       fontNode?.apply(context)

       def g = context.g
       def frc = g.getFontRenderContext()
       def fm = g.fontMetrics

       def layout = new TextLayout(text, g.font, frc)

       def dx = 0
       def dy = 0
       def sb = fm.getStringBounds(text,g)

       switch(getHalignValue()){
          case LEFT: dx = x; break;
          case CENTER: dx = x - sb.width/2; break;
          case RIGHT: dx = x - sb.width; break;
       }

       switch(getValignValue()){
          case BOTTOM: dy = y + fm.ascent*2 - fm.height; break;
          case MIDDLE: dy = y + fm.height - fm.ascent; break;
          case TOP: dy = y - fm.height + fm.ascent; break;
          case BASELINE: dy = y; break;
       }

       return layout.getOutline(AffineTransform.getTranslateInstance(dx, dy))
    }

    private Shape calculateMultipleRowOutline(String[] textRows) {
       GfxContext context = getRuntime()?.getContext()
       if(!context) return null

       FontNode fontNode = nodes.reverse().find{ it instanceof FontNode }
       fontNode?.apply(context)

       def g = context.g
       def frc = g.getFontRenderContext()
       def fm = g.fontMetrics

       def layout = new TextLayout( rows[0], g.font, frc )
       def dy = fm.ascent*2 - fm.height
       outline = new Area(layout.getOutline(AffineTransform.getTranslateInstance( x, y + dy )))

       if(textRows.size() > 1){
          textRows[1..-1].inject(y+fm.ascent) { ny, txt ->
             layout = new TextLayout( txt, g.font, frc )
             def py = ny + spacing + dy
             def s = new Area(layout.getOutline(AffineTransform.getTranslateInstance(x,py)))
             outline.add(s)
             return ny + spacing + fm.ascent
          }
       }

       def dx = 0
       def sb = outline.bounds2D

       switch(getHalignValue()){
          case LEFT: dx = 0; break;
          case CENTER: dx = 0 - sb.width/2; break;
          case RIGHT: dx = 0 - sb.width; break;
       }

       switch(getValignValue()){
          case BOTTOM: dy = 0; break;
          case BASELINE:
          case MIDDLE: dy = 0 - sb.height/2; break;
          case TOP: dy = 0 - sb.height; break;
       }

       return AffineTransform.getTranslateInstance(dx,dy).createTransformedShape(outline)
    }

    private Shape center(Shape input) {
       if(Double.isNaN(cx) && Double.isNaN(cy)) return input
       def bounds = input.bounds
       def bcx = bounds.x + (bounds.width/2)
       def bcy = bounds.y + (bounds.height/2)
       return AffineTransform.getTranslateInstance(cx - bcx, cy - bcy).createTransformedShape(input)
    }

    private def getHalignValue(){
       if(!halign) return LEFT
       if(halign instanceof Number){
          return halign.intValue()
       }
       switch(halign){
          case "left": return LEFT
          case "center": return CENTER
          case "right": return RIGHT
       }
       return LEFT
    }

    private def getValignValue(){
       if( !valign ) return BOTTOM
       if( valign instanceof Number ){
          return halign.intValue()
       }
       switch( valign ){
          case "top": return TOP
          case "middle": return MIDDLE
          case "baseline": return BASELINE
          case "bottom": return BOTTOM
       }
       return BOTTOM
    }
}
