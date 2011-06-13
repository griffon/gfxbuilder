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

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath
import griffon.builder.gfx.GfxAttribute

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class QuadToExtPathSegment extends AbstractExtPathSegment {
    @GfxAttribute float x1
    @GfxAttribute float x2
    @GfxAttribute float y1
    @GfxAttribute float y2

    QuadToExtPathSegment(){
       super("xquadTo")
    }

    void apply(ExtendedGeneralPath path) {
       path.quadTo( x1 as float,
                    y1 as float,
                    x2 as float,
                    y2 as float )
    }
}