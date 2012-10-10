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

package griffon.builder.gfx

import java.awt.Component
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Rectangle2D

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
final class GfxContext {
   Graphics2D g
   Component component
   List eventTargets = []
   Map groupSettings = [:]
   Rectangle2D bounds
   RenderingHints renderingHints = new RenderingHints(null)
   GfxContext parent

   GfxContext copy() {
      GfxContext copy = new GfxContext()
      copy.g = g
      copy.parent = this
      copy.component = component
      copy.eventTargets.addAll(eventTargets)
      copy.groupSettings.putAll(groupSettings)
      if(renderingHints) copy.renderingHints.add(renderingHints)
      if(bounds) {
         copy.bounds = new Rectangle2D.Double(bounds.x as double,
                                              bounds.y as double,
                                              bounds.width as double,
                                              bounds.height as double)
      }
      return copy
   }
}