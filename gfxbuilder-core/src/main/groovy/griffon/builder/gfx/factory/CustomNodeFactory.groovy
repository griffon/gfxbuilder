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

import griffon.builder.gfx.CustomGfxNode

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class CustomNodeFactory extends AbstractGfxFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        if (value instanceof CustomGfxNode) {
            return value
        } else if (value instanceof Class && CustomGfxNode.isAssignableFrom(value)) {
            return value.newInstance()
        }
        throw new RuntimeException("in $name value must be either an instance of CustomGfxNode or a CustomGfxNode subclass.")
    }

    public boolean isLeaf() {
        return false
    }
}