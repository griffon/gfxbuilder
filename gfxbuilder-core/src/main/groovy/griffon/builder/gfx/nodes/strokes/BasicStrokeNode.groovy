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

package griffon.builder.gfx.nodes.strokes


import java.awt.BasicStroke
import java.awt.Stroke
import java.awt.Shape

import griffon.builder.gfx.GfxNode
import griffon.builder.gfx.GfxUtils
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.StrokeProvider


/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class BasicStrokeNode extends AbstractStrokeNode {
   @GfxAttribute(alias="w") float width = 1f
   @GfxAttribute int cap
   @GfxAttribute(alias="j") int join
   @GfxAttribute(alias="m") float miterlimit = 1
   @GfxAttribute(alias="d") def dash
   @GfxAttribute(alias="dp") float dashphase = 0

   BasicStrokeNode() {
      super("basicStroke")
   }

    protected Stroke createStroke() {
        def _w = width
        def _c = cap
        def _j = join
        def _m = miterlimit
        def _d = dash

        _c = GfxUtils.getCapValue(_c)
        _j = GfxUtils.getJoinValue(_j)
        _d = GfxUtils.getDashValue(_d)
        if( _m == null ) _m = 10

        if( _d != null && dashphase != null ){
           return new BasicStroke(_w as float, _c as int, _j as int, _m as float, _d as float[], dashphase as float)
        }else{
           return new BasicStroke(_w as float, _c as int, _j as int, _m as float)
        }
    }
}
