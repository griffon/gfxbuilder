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

package griffon.builder.gfx.nodes.transforms

import griffon.builder.gfx.GfxAttribute

import java.awt.geom.AffineTransform

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ScaleTransform extends AbstractTransform {
    @GfxAttribute double x = 1d
    @GfxAttribute double y = 1d

    ScaleTransform() {
       super("scale")
    }

    ScaleTransform clone() {
       new ScaleTransform(x: x, y: y, enabled: enabled)
    }

    AffineTransform getTransform() {
       AffineTransform.getScaleInstance(x as double, y as double)
    }
}
