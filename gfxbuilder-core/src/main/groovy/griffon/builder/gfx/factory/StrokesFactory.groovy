/*
 * Copyright 2008-2010 the original author or authors.
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

import java.awt.Stroke
import griffon.builder.gfx.DrawableNode
import griffon.builder.gfx.ContainerNode
import griffon.builder.gfx.StrokeProvider
import griffon.builder.gfx.nodes.strokes.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class StrokesFactory extends GfxBeanFactory {
    StrokesFactory(beanClass, leaf){
       super(beanClass, leaf)
    }

    StrokesFactory(beanClass){
       super(beanClass)
    }

    public void setParent(FactoryBuilderSupport builder, Object parent, Object node) {
       if(parent instanceof ContainerNode || parent instanceof ComposableStroke){
          parent << node
       } else {
          throw new IllegalArgumentException("node can not be nested inside $parent")
       }
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
       if(child instanceof StrokeProvider) {
          parent << child
       } else {
          throw new IllegalArgumentException("$child can not be nested inside $parent")
       }
    }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ShapeStrokeFactory extends GfxBeanFactory {
    ShapeStrokeFactory(){
       super(ShapeStrokeNode, false)
    }

    public void setParent(FactoryBuilderSupport builder, Object parent, Object node) {
       if(parent instanceof ContainerNode || parent instanceof ComposableStroke){
          parent << node
       } else {
          throw new IllegalArgumentException("node can not be nested inside $parent")
       }
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
       if(child instanceof DrawableNode) {
          parent << child
       } else {
          throw new IllegalArgumentException("$child can not be nested inside $parent")
       }
    }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class StrokeFactory extends AbstractGfxFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value,
          Map properties ) throws InstantiationException, IllegalAccessException {
       StrokeNode node = new StrokeNode()
       if( value != null && value instanceof StrokeProvider || value instanceof Stroke ) {
           node.stroke = value
       }
       return node
    }

    public void setParent(FactoryBuilderSupport builder, Object parent, Object node) {
       if(parent instanceof ContainerNode || parent instanceof ComposableStroke){
          parent << node
       } else {
          throw new IllegalArgumentException("node can not be nested inside $parent")
       }
    }
}