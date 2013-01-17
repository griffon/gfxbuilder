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

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class GfxBeanFactory extends AbstractGfxFactory {
    final Class beanClass
    final protected boolean leaf

    GfxBeanFactory(Class beanClass) {
        this(beanClass, true)
    }

    GfxBeanFactory(Class beanClass, boolean leaf) {
        this.beanClass = beanClass
        this.leaf = leaf
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value,
                              Map properties) throws InstantiationException, IllegalAccessException {
        if (value) {
            if (beanClass.isAssignableFrom(value.getClass())) {
                return value
            } else {
                return beanClass.getDeclaredConstructor([value.getClass()] as Class[]).newInstance([value] as Object[])
            }
        } else {
            return beanClass.newInstance()
        }
    }

    public boolean isLeaf() {
        return leaf
    }
}