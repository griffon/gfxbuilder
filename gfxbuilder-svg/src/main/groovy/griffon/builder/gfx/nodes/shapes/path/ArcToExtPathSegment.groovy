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

package griffon.builder.gfx.nodes.shapes.path

import griffon.builder.gfx.GfxAttribute
import org.apache.batik.ext.awt.geom.ExtendedGeneralPath

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ArcToExtPathSegment extends AbstractExtPathSegment {
   @GfxAttribute float x
   @GfxAttribute float y
   @GfxAttribute float rx
   @GfxAttribute float ry
   @GfxAttribute(alias="a") float angle
   @GfxAttribute(alias="la") boolean largeArc
   @GfxAttribute(alias="sw") boolean sweep

   ArcToExtPathSegment(){
      super("xarcTo")
   }

   public void apply(ExtendedGeneralPath path) {
      path.arcTo( rx as float,
                  ry as float,
                  angle as float,
                  largeArc as boolean,
                  sweep as boolean,
                  x as float,
                  y as float )
   }
}