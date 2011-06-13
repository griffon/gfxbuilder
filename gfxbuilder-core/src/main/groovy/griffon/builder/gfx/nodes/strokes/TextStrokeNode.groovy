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

import java.awt.Font
import java.awt.Stroke
import com.jhlabs.awt.TextStroke
import griffon.builder.gfx.GfxAttribute

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class TextStrokeNode extends AbstractStrokeNode {
    @GfxAttribute(alias="t") String text = "Groovy"
    @GfxAttribute(alias="f") Font font = new Font( "Default", Font.PLAIN, 12 )
    @GfxAttribute(alias="s") boolean stretch
    @GfxAttribute(alias="r") boolean repeat

    TextStrokeNode() {
       super( "textStroke" )
    }

    protected Stroke createStroke() {
        return new TextStroke(text, font, stretch, repeat)
    }
}
