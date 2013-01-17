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

package griffon.builder.gfx.nodes.paints

import griffon.builder.gfx.GfxAttribute

import javax.imageio.ImageIO
import java.awt.Image
import java.awt.Paint
import java.awt.TexturePaint
import java.awt.geom.Rectangle2D
import java.beans.PropertyChangeEvent

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class TexturePaintNode extends AbstractPaintNode {
    private Paint _paint
    private Image _image
    private Rectangle2D _bounds

    @GfxAttribute(alias = "i") Image image
    @GfxAttribute(alias = "f")
    def file
    @GfxAttribute(alias = "u")
    def url
    @GfxAttribute(alias = "cl")
    def classpath
    @GfxAttribute double x = 0d
    @GfxAttribute double y = 0d
    @GfxAttribute(alias = "w") double width = Double.NaN
    @GfxAttribute(alias = "h") double height = Double.NaN

    TexturePaintNode() {
        super("texturePaint")
    }

    TexturePaintNode(Image image) {
        super("texturePaint")
        this.image = image
    }

    TexturePaintNode(File file) {
        super("texturePaint")
        this.file = file
    }

    TexturePaintNode(URL url) {
        super("texturePaint")
        this.url = url
    }

    TexturePaintNode(String classpath) {
        super("texturePaint")
        this.classpath = classpath
    }

    TexturePaintNode(TexturePaint paint) {
        super("texturePaint")
        x = paint.anchorRext.x
        y = paint.anchorRext.y
        w = paint.anchorRext.width
        h = paint.anchorRext.height
        image = paint.image
    }

    void onDirty(PropertyChangeEvent event) {
        _image = null
        _paint = null
        _bounds = null
        super.onDirty(event)
    }

    Paint getPaint(Rectangle2D bounds) {
        if (areEqual(bounds, _bounds)) return _paint

        _bounds = new Rectangle2D.Double(x as double, y as double,
            !Double.isNaN(w) ? w as double : 0d,
            !Double.isNaN(h) ? h as double : 0d)
        _bounds.x += bounds.x
        _bounds.y += bounds.y

        if (image) {
            _image = image
        } else if (classpath) {
            URL imageUrl = Thread.currentThread().getContextClassLoader().getResource(classpath)
            _image = ImageIO.read(imageUrl)
        } else if (url) {
            _image = ImageIO.read(url instanceof URL ? url : url.toString().toURL())
        } else if (file) {
            _image = ImageIO.read(file instanceof File ? file : new File(file.toString()))
        } else {
            throw new IllegalArgumentException("${this}: must define one of [image, classpath, url, file]")
        }

        if (Double.isNaN(w)) _bounds.width = _image.getWidth(null)
        if (Double.isNaN(h)) _bounds.height = _image.getHeight(null)
        _paint = new TexturePaint(_image, _bounds)
        return _paint
    }

    private boolean areEqual(Rectangle2D a, Rectangle2D b) {
        return b && a.x == b.x && a.y == b.y &&
            a.width == b.width && a.height == b.height
    }
}
