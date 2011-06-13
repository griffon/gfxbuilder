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

import griffon.builder.gfx.nodes.shapes.AreaNode

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class AreaFactory extends AbstractGfxFactory {
     private String name
     private String methodName

     AreaFactory(String name, String methodName){
         this.name = name
         this.methodName = methodName
     }

     public Object newInstance( FactoryBuilderSupport builder, Object name, Object value,
             Map properties ) throws InstantiationException, IllegalAccessException {
         return new AreaNode(this.name, methodName)
     }
}