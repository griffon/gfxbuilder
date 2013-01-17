/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.builder.gfx.factory

import griffon.builder.gfx.nodes.misc.RenderingHintNode

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class AntialiasFactory extends AbstractGfxFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value,
                              Map properties) throws InstantiationException, IllegalAccessException {
        RenderingHintNode node = new RenderingHintNode()
        node.key = 'antialiasing'
        setAntialiasValue(node, value)
        if (properties.containsKey("enabled")) {
            setAntialiasValue(node, properties.remove("enabled"))
        }
        return node
    }

    public boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        return false
    }

    public boolean isLeaf() {
        return true
    }

    private void setAntialiasValue(node, value) {
        if (value == null) {
            node.value = 'antialias on'
        } else if (value instanceof Boolean) {
            node.value = "antialias ${value ? 'on' : 'off'}"
        } else if (value instanceof String) {
            if ("off" == value) {
                node.value = 'antialias off'
            } else if ("on" == value) {
                node.value = 'antialias on'
            } else {
                throw new IllegalArgumentException("value must be a boolean or any of ['on'|'off']")
            }
        }
    }
}