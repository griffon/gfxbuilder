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

package griffon.builder.gfx.factory

import java.awt.Shape
import griffon.builder.gfx.ShapeProvider
import griffon.builder.gfx.nodes.shapes.MorphNode

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class MorphFactory extends AbstractGfxFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value,
          Map properties ) throws InstantiationException, IllegalAccessException {
       if(value instanceof MorphNode) {
           return value
       }
       return new MorphNode()
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
       if(child instanceof Shape || child instanceof ShapeProvider){
          parent.addShape(child)
       } else {
          super.setChild(builder, parent, child)
       }
    }
}