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

import griffon.builder.gfx.Transformable
import griffon.builder.gfx.nodes.transforms.Transform
import griffon.builder.gfx.nodes.transforms.TransformTransform
import griffon.builder.gfx.nodes.transforms.Transforms

import java.awt.geom.AffineTransform

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TransformFactory extends GfxBeanFactory {
   TransformFactory(Class beanClass) {
      super(beanClass, true)
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TransformsFactory extends GfxBeanFactory {
   TransformsFactory() {
      super(Transforms, false)
   }

   public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
      if(parent instanceof Transformable) {
         parent.transforms = node
      }
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if(child instanceof Transform) {
         parent << child
      } else {
         throw new RuntimeException("Node ${parent} does not accept nesting of ${child}.")
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TransformTransformFactory extends AbstractGfxFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value,
          Map properties ) throws InstantiationException, IllegalAccessException {
      if( value != null ){
         if( TransformTransform.class.isAssignableFrom(value.getClass()) ){
              return value
          }else if(value instanceof AffineTransform){
              return new TransformTransform(transform: value)
          }
      }

      return new TransformTransform()
   }

   public boolean isLeaf() {
      true
   }
}