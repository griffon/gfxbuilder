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

package griffon.builder.gfx.nodes.misc

import griffon.builder.gfx.AbstractDrawableNode
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.runtime.GfxRuntime
import griffon.builder.gfx.runtime.ImageGfxRuntime

import java.awt.Image
import java.awt.RenderingHints
import java.awt.Shape
import java.awt.geom.AffineTransform
import java.beans.PropertyChangeEvent

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ImageNode extends AbstractDrawableNode {
   private Image _image
   private Shape _shape

   @GfxAttribute(alias="i") Image image
   @GfxAttribute(alias="f") def file
   @GfxAttribute(alias="u") def url
   @GfxAttribute(alias="cl") def classpath
   @GfxAttribute double x = 0d
   @GfxAttribute double y = 0d
   @GfxAttribute(alias="w") double width = Double.NaN
   @GfxAttribute(alias="h") double height = Double.NaN
   @GfxAttribute(resets=false) def interpolation

   ImageNode() {
      super("image")
   }

   ImageNode(Image image) {
      super("image")
      this.image = image
   }

   ImageNode(File file) {
      super("image")
      this.file = file
   }

   ImageNode(URL url) {
      super("image")
      this.url = url
   }

   ImageNode(String classpath) {
      super("image")
      this.classpath = classpath
   }

   protected void reset(PropertyChangeEvent event) {
      _image = null
      _shape = null
   }

   Image getImg() {
      if(!_image) {
         _image = getRuntime().getImage()
      }
      _image
   }

   Shape getShape() {
      if(!_shape) {
         _shape = getRuntime().getShape()
      }
      _shape
   }

   protected void beforeApply(GfxContext context) {
      super.beforeApply(context)
      getImg()
   }

   protected void applyNode(GfxContext context) {
      def _interpolation = getInterpolationValue()
      if(_interpolation) {
         context.g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, _interpolation)
      }

      AffineTransform transform = new AffineTransform()
      transform.concatenate context.g.transform
      transform.concatenate getRuntime().getLocalTransforms()
      context.g.transform = transform

      applyWithFilter(context) {
         if(Double.isNaN(width) && Double.isNaN(height)) {
            context.g.drawImage(_image, x as int, y as int, context.component)
         } else {
            context.g.drawImage(_image, x as int, y as int, width as int, height as int, context.component)
         }
      }
   }

   GfxRuntime createRuntime(GfxContext context) {
      new ImageGfxRuntime(this, context)
   }

   private def getInterpolationValue() {
      switch( interpolation ){
          case ~/(?i:bicubic)/:  return RenderingHints.VALUE_INTERPOLATION_BICUBIC
          case ~/(?i:bilinear)/: return RenderingHints.VALUE_INTERPOLATION_BILINEAR
          case ~/(?i:nearest neighbor)/:
          case ~/(?i:nearest)/:
             return RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
      }
      return RenderingHints.VALUE_INTERPOLATION_BILINEAR
   }
}
