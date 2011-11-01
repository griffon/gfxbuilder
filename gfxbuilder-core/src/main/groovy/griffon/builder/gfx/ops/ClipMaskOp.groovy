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
package griffon.builder.gfx.ops

import com.jhlabs.image.PointFilter
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage

/**
 * This Filter acts like a clipping from the given image.
 *
 * @author Alexander Klein <info@aklein.org>
 */
class ClipMaskOp extends PointFilter {
    private BufferedImage clip
    boolean scaling = false
    boolean inverted = false
    int xOffset = 0
    int yOffset = 0
    private BufferedImage mask
    private int oX = xOffset
    private int oY = xOffset

    /**
     * If useAlpha is true, the alpha channel of the given image will be used to define the transparency of the clipping.
     * Otherwise the given will be grayscaled and the brightness values define the trancparency level. (white = opaque, black = transparent)
     *
     * @param clip
     * @param useAlpha
     */
    ClipMaskOp(BufferedImage clip, boolean useAlpha = false) {
        this.clip = clip
        if (clip.type != BufferedImage.TYPE_BYTE_GRAY) {
            this.clip = new BufferedImage(clip.width, clip.height, BufferedImage.TYPE_BYTE_GRAY)
            if (useAlpha) {
                if (!clip.alphaRaster)
                    throw new IllegalArgumentException("When using 'useAlpha' the clip image needs an alpha-channel")
                Object alphaData = clip.alphaRaster.getSamples(0, 0, clip.width, clip.height, 0, (int[]) null)
                this.clip.getRaster().setSamples(0, 0, clip.width, clip.height, 0, alphaData)
            } else {
                Graphics2D g = this.clip.createGraphics()
                g.drawImage(clip, 0, 0, clip.width, clip.height, null)
                g.dispose()
            }
        }
    }

    @Override
    void setDimensions(int width, int height) {
        int oX = xOffset
        int oY = xOffset
        if (scaling) {
            oX = oY = 0
            mask = scale(clip, width, height)
        } else
            mask = clip
    }

    @Override
    int filterRGB(int x, int y, int rgb) {
        int alpha
        try {
            int m = mask.getRGB(x + oX, y + oY) & 0xffffff
            alpha = (m & 0xff)
        } catch (ArrayIndexOutOfBoundsException e) {
            alpha = 0x00
        }
        if(inverted)
            alpha = 0xff - alpha;
        return (rgb & 0x00ffffff) | alpha << 24
    }

    BufferedImage scale(BufferedImage original, int width, int height) {
        int newWidth = (int) (original.width * width / original.width)
        int newHeight = (int) (original.height * height / original.height)
        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.type)
        Graphics2D g = resized.createGraphics()
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.width, original.height, null)
        g.dispose()
        return resized
    }
}
