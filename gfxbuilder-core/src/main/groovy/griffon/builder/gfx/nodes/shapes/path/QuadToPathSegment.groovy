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

package griffon.builder.gfx.nodes.shapes.path

import griffon.builder.gfx.GfxAttribute

import java.awt.geom.GeneralPath

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class QuadToPathSegment extends AbstractPathSegment {
    @GfxAttribute double x1
    @GfxAttribute double x2
    @GfxAttribute double y1
    @GfxAttribute double y2

    QuadToPathSegment() {
        super("quadTo")
    }

    void apply(GeneralPath path) {
        path.quadTo(x1 as double,
            y1 as double,
            x2 as double,
            y2 as double)
    }
}
