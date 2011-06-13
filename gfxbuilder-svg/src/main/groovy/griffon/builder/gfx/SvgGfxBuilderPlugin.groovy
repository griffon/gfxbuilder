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

import griffon.builder.gfx.factory.ExtPathSegmentFactory
import griffon.builder.gfx.factory.ShapeExtPathSegmentFactory
import griffon.builder.gfx.nodes.shapes.ExtPathNode
import griffon.builder.gfx.nodes.shapes.path.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class SvgGfxBuilderPlugin implements GfxBuilderPlugin {
   def registerGfxShapes = {
      registerGfxBeanFactory("xpath", ExtPathNode)
      registerFactory("xarcTo", new ExtPathSegmentFactory(ArcToExtPathSegment))
      registerFactory("xmoveTo", new ExtPathSegmentFactory(MoveToExtPathSegment))
      registerFactory("xlineTo", new ExtPathSegmentFactory(LineToExtPathSegment))
      registerFactory("xquadTo", new ExtPathSegmentFactory(QuadToExtPathSegment))
      registerFactory("xcurveTo", new ExtPathSegmentFactory(CurveToExtPathSegment))
      registerFactory("xhline", new ExtPathSegmentFactory(HLineExtPathSegment))
      registerFactory("xvline", new ExtPathSegmentFactory(VLineExtPathSegment))
      registerFactory("xshapeTo", new ShapeExtPathSegmentFactory())
      registerFactory("xclose", new ExtPathSegmentFactory(CloseExtPathSegment))
   }
}