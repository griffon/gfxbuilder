/*
 * Copyright 2009-2010 the original author or authors.
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

import groovy.swing.factory.ComponentFactory
import griffon.builder.gfx.swing.GfxCanvas

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class GfxCanvasFactory extends ComponentFactory {
   GfxCanvasFactory() {
      super(GfxCanvas, false)
   }

   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      return new GfxCanvas()
   }

   public boolean isHandlesNodeChildren() {
      return true
   }

   public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
      node.node = childContent()
      return false
   }
}
