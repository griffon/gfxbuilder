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
package griffon.builder.gfx.ops

import com.jhlabs.image.CropFilter

import java.awt.image.BufferedImage
import java.awt.image.WritableRaster

/**
 * This Filter crops the source image to the minimum size possible when all fully transparent pixels will be ignored.
 *
 * @author Alexander Klein <info@aklein.org>
 */
class AutoCropOp extends CropFilter {
    @Override
    BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int type = src.type
        WritableRaster alphaRaster = src.alphaRaster

        int x1 = src.width
        int x2 = 0
        int y1 = src.height
        int y2 = 0

        int[] inPixels = new int[src.width]
        for (int y = 0; y < src.height; y++) {
            alphaRaster.getDataElements(0, y, src.width, 1, inPixels)
            for (int x = 0; x < src.width; x++) {
                if (inPixels[x] >> 24 != 0x00) {
                    x1 = Math.min(x1, x)
                    x2 = Math.max(x2, x)
                    y1 = Math.min(y1, y)
                    y2 = Math.max(y2, y)
                }
            }
        }
        x = x1
        y = y1
        width = x2 - x1 + 1
        height = y2 - y1 + 1
        return super.filter(src, dst)
    }
}
