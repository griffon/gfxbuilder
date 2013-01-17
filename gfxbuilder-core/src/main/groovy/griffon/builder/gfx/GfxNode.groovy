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

package griffon.builder.gfx

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
abstract class GfxNode /*extends GroovyObjectSupport*/ implements PropertyChangeListener {
    private final String _name
    private Map _props = new ObservableMap()
    private static final Map _cache = [:]

    @GfxAttribute(alias = "n", resets = false) String name
    @GfxAttribute(alias = "e") boolean enabled = true
    // protected boolean _dirty

    GfxNode(String name) {
        _name = name
        this.addPropertyChangeListener(this)
        this._props.addPropertyChangeListener(this)
    }

    String getNodeName() {
        _name
    }

    String toString() {
        name ? "${_name}[${name}]" : _name
    }

    final Map getProps() {
        _props
    }

    abstract void apply(GfxContext context)

    public void propertyChange(PropertyChangeEvent event) {
        if (event.source == this ||
            (event.source == _props && event instanceof ObservableMap.PropertyUpdatedEvent)) {
            // _dirty = true
            onDirty(event)
            // _dirty = false
        }
    }

    protected void onDirty(PropertyChangeEvent event) {
        if (triggersReset(event)) reset(event)
    }

    protected void reset(PropertyChangeEvent event) {
        // empty
    }

    protected boolean triggersReset(PropertyChangeEvent event) {
        return _cache.get(this.class.name, gatherFieldInfo())[(event.propertyName)]?.resets
    }

    private Map gatherFieldInfo() {
        Map map = [:]
        Class c = this.class
        while (c != null) {
            c.declaredFields.each { field ->
                GfxAttribute attr = field.getAnnotation(GfxAttribute)
                if (attr) map[(field.name)] = [resets: attr.resets()]
            }
            c = c.superclass
        }
        map
    }
}