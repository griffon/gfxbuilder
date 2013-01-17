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

package griffon.builder.gfx.nodes.transforms

import griffon.builder.gfx.GfxAttribute

import java.awt.geom.AffineTransform

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class MatrixTransform extends AbstractTransform {
    @GfxAttribute double m00 = 1d
    @GfxAttribute double m10 = 0
    @GfxAttribute double m01 = 0
    @GfxAttribute double m11 = 1d
    @GfxAttribute double m02 = 0
    @GfxAttribute double m12 = 0

    MatrixTransform() {
        super("matrix")
    }

    MatrixTransform clone() {
        new MatrixTransform(m00: m00,
            m10: m10,
            m01: m01,
            m11: m11,
            m02: m02,
            m12: m12,
            enabled: enabled)
    }

    AffineTransform getTransform() {
        return new AffineTransform(m00 as double,
            m10 as double,
            m01 as double,
            m11 as double,
            m02 as double,
            m12 as double)
    }
}
