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

package griffon.builder.gfx.nodes.misc

import griffon.builder.gfx.AbstractGfxNode
import griffon.builder.gfx.DrawableNode
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxNode
import griffon.builder.gfx.nodes.shapes.RectangleNode
import griffon.builder.gfx.runtime.GfxRuntime
import griffon.builder.gfx.runtime.GroupGfxRuntime

import java.awt.Graphics
import java.awt.Shape
import java.awt.geom.AffineTransform
import java.awt.geom.Area

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 * @author Alexander Klein <info@aklein.org>
 */
class GroupNode extends AbstractGfxNode {
  private Map _previousGroupSettings = [:]
  private Shape _shape
  private List<RectangleNode> _temporaries = []

  GroupNode() {
    super("group")
    passThrough = true
  }

  GfxRuntime createRuntime(GfxContext context) {
    new GroupGfxRuntime(this, context)
  }

  protected void beforeApply(GfxContext context) {
    def _nodes = new ArrayList(nodes)
    _nodes.each { node ->
      if (node instanceof AbstractGfxNode) {
        if(node.runtime == null)
          node.getRuntime(context)
        Shape s = node.calculateShape()
        def b = s.bounds2D
        if (new Area(s).isEmpty() && !b.isEmpty()) {
          def t = new RectangleNode(b)
          t.borderWidth = 0
          _temporaries << t
          addNode(t)
        }
      }
    }
    super.beforeApply(context)
  }

  protected void afterApply(GfxContext context) {
    context.groupSettings = _previousGroupSettings
    super.afterApply(context)
    new ArrayList(_temporaries).each { removeNode(it) }
  }

  protected void applyThisNode(GfxContext context) {
    _previousGroupSettings = [:]
    _previousGroupSettings.putAll(context.groupSettings)
    if (borderColor != null) context.groupSettings.borderColor = borderColor
    if (borderWidth != null) context.groupSettings.borderWidth = borderWidth
    if (fill != null) context.groupSettings.fill = fill
    AffineTransform affineTransform = new AffineTransform()
    affineTransform.concatenate context.g.transform
    affineTransform.concatenate runtime.getLocalTransforms()
    context.g.transform = affineTransform
  }

  protected void applyNestedNode(GfxNode node, GfxContext context) {
    applyWithFilter(context) {
      node.apply(context)
    }
  }

  Shape calculateShape() {
    GfxContext context = getRuntime().getContext()
    List shapes = []

    new ArrayList(getNodes()).each { node ->
      if (!node.enabled) return
      if (node instanceof DrawableNode) {
        if (!node.visible || !node.enabled) return
        if (!node.getRuntime()) node.getRuntime(context)
        Graphics gcopy = null
        if (node.txs.enabled()) {
          gcopy = context.g
          context.g = gcopy.create()
        }
        try {
          Shape s = node.getRuntime().getTransformedShape()
          if (s) shapes << s
        } finally {
          if (gcopy) {
            context.g.dispose()
            context.g = gcopy
          }
        }
      }
    }

    if (!shapes) return null

    Area area = new Area(shapes[0])
    if (shapes.size() > 1) {
      shapes[1..-1].each { shape ->
        area.add(shape instanceof Area ? shape : new Area(shape))
      }
    }
    area
  }
}
