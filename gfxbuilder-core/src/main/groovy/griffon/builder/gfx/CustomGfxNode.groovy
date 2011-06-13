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

package griffon.builder.gfx

import java.awt.Shape
import java.awt.geom.AffineTransform
import java.beans.PropertyChangeEvent

import griffon.builder.gfx.runtime.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
abstract class CustomGfxNode extends AbstractDrawableNode {
   private DrawableNode _node
   private static final GfxBuilder GFXBUILDER = new GfxBuilder()

   CustomGfxNode() {
      super("customNode")
      passThrough = false
   }

   CustomGfxNode(String name) {
      super(name)
      passThrough = false
   }

   DrawableNode getNode() {
      if(!_node) {
         _node = createNode(GFXBUILDER)
         _node.addPropertyChangeListener(this)
      }
      _node
   }

   abstract DrawableNode createNode(GfxBuilder builder)

   GfxRuntime getRuntime(GfxContext context) {
      getNode().getRuntime(context)
      super.getRuntime(context)
   }

   Shape getShape() {
      getNode().getShape()
   }

   void propertyChanged(PropertyChangeEvent event) {
      if(event.source == _node) {
         onDirty(event)
      } else {
         super.propertyChanged(event)
      }
   }

   protected boolean triggersReset(PropertyChangeEvent event) {
      if(event.source == _node) return _node.triggersReset(event)
      return super.triggersReset(event)
   }

   protected void reset(PropertyChangeEvent event) {
      _node?.reset()
      runtime?.reset()
   }

   protected void beforeApply(GfxContext context) {
      super.beforeApply(context)
   }

   protected void applyNode(GfxContext context) {
      AffineTransform transform = new AffineTransform()
      transform.concatenate context.g.transform
      transform.concatenate getRuntime().getLocalTransforms()
      context.g.transform = transform

      _node.apply(context)
   }

//    protected boolean shouldSkip(GfxContext context) {
//       if(super.shouldSkip(context)) true
//    }
}