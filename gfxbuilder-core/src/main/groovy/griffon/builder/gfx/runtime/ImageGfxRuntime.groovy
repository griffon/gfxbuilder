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

package griffon.builder.gfx.runtime

import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxNode

import javax.imageio.ImageIO
import java.awt.Image
import java.awt.Shape
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.beans.PropertyChangeEvent

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ImageGfxRuntime extends DrawableGfxRuntime {
    private def _image

    ImageGfxRuntime(GfxNode node, GfxContext context) {
        super(node, context)
    }

    void reset(PropertyChangeEvent event = null) {
        if (event == null) {
            _image = null
        }
        super.reset(event)
    }

    public def getImage() {
        if (!_image) {
            if (_node.image) {
                if (_node.image instanceof Image || _node.image instanceof BufferedImage) {
                    _image = _node.image
                }
            } else if (_node.classpath) {
                URL imageUrl = Thread.currentThread().getContextClassLoader().getResource(_node.classpath)
                _image = ImageIO.read(imageUrl)
            } else if (_node.url) {
                _image = ImageIO.read(_node.url instanceof URL ? _node.url : _node.url.toString().toURL())
            } else if (_node.file) {
                _image = ImageIO.read(_node.file instanceof File ? _node.file : new File(_node.file.toString()))
            }
        }
        _image
    }

    public Shape getShape() {
        def img = getImage()
        return new Rectangle2D.Double(_node.x as double,
            _node.y as double,
            !Double.isNaN(_node.width) ? _node.width as double : img.getWidth(_context.component) as double,
            !Double.isNaN(_node.height) ? _node.height as double : img.getHeight(_context.component) as double)
    }
}