/*
 * Copyright 2007-2013 the original author or authors.
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

package griffon.builder.gfx.runtime

import griffon.builder.gfx.DrawableNode
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxNode

import java.beans.PropertyChangeEvent

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class GroupGfxRuntime extends DrawableGfxRuntime {
    GroupGfxRuntime(GfxNode node, GfxContext context) {
        super(node, context)
    }
/*
   public Shape getLocalShape() {
      getShape()
   }

   public Shape getTransformedShape() {
      getShape()
   }
*/

    void reset(PropertyChangeEvent event = null) {
        super.reset(event)
        switch (event?.propertyName) {
            case "fill":
            case "borderColor":
            case "borderWidth":
                _node.nodes.each { n ->
                    if (n instanceof DrawableNode) {
                        n.runtime?.reset(event)
                    }
                }
        }
    }
}