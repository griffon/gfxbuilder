/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.builder.gfx.factory

import griffon.builder.gfx.DrawableNode
import griffon.builder.gfx.nodes.shapes.PathNode
import griffon.builder.gfx.nodes.shapes.path.ShapePathSegment

import java.awt.Shape

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class PathSegmentFactory extends GfxBeanFactory {
   public PathSegmentFactory( Class segmentClass ) {
      super(segmentClass, true)
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object node) {
      if(parent instanceof PathNode) {
         parent.addPathSegment(node)
      } else {
         throw new RuntimeException("Node ${parent} does not accept nesting of ${child}.")
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ShapePathSegmentFactory extends GfxBeanFactory {
   public ShapePathSegmentFactory() {
      super(ShapePathSegment, false)
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object node) {
      if(parent instanceof PathNode) {
         parent.addPathSegment(node)
      } else {
         throw new RuntimeException("Node ${parent} does not accept nesting of ${child}.")
      }
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if(child instanceof Shape || child instanceof DrawableNode) {
         parent.setShape(child)
      } else {
         throw new RuntimeException("Node ${parent} does not accept nesting of ${child}.")
      }
   }
}