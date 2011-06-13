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

package griffon.builder.gfx.nodes.transforms

import java.awt.Shape
import java.awt.geom.AffineTransform
import java.beans.PropertyChangeEvent
import groovy.util.ObservableList.ElementEvent

import griffon.builder.gfx.GfxNode
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxAttribute

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class Transforms extends GfxNode {
    private final ObservableList OLDVALUE = new ObservableList()
    private final ObservableList _transforms = new ObservableList()

    Transforms() {
       super("transforms")
       _transforms.addPropertyChangeListener(this)
    }

    String toString() {
      String str = "${super.toString()}$_transforms"
    }

    void apply(GfxContext context) {
       if(!enabled()) return
       AffineTransform transform = new AffineTransform()
       transform.concatenate context.g.transform
       concatenateTo(transform)
       context.g.transform = transform
    }

    void concatenateTo(AffineTransform transform) {
       if(!enabled()) return
       _transforms.each { t ->
          if(t.enabled && t.transform) transform.concatenate t.transform
       }
    }

    Transform getAt(String name) {
       return _transforms.find{ it?.name == name }
    }

    Transforms clone() {
       Transforms node = new Transforms(enabled: enabled)
       _transforms.each{ node.addTransform(it.clone()) }
       node
    }

    Transforms leftShift(Transform transform) {
       addTransform(transform)
       this
    }

    void addTransform(Transform transform) {
        if(!transform || _transforms.contains(transform)) return
        int oldSize = _transforms.size()
        _transforms << transform
        int newSize = _transforms.size()
        if(enabled && oldSize != newSize) firePropertyChange("size", oldSize, newSize)
    }

    void removeTransform(Transform transform) {
        if(!transform || _transforms.isEmpty()) return
        int oldSize = _transforms.size()
        _transforms.remove(transform)
        int newSize = _transforms.size()
        if(enabled && oldSize != newSize) firePropertyChange("size", oldSize, newSize)
    }

    boolean enabled() {
        if(_transforms.empty) return false
        def b = _transforms.any { it.enabled }
        b ? enabled : false
    }

    boolean isEmpty() {
       _transforms.isEmpty()
    }

    Iterator iterator() {
       _transforms.iterator()
    }

    void clear() {
       if(_transforms.isEmpty()) return
       int oldSize = _transforms.size()
       _transforms.clear()
       if(enabled && oldSize != newSize) firePropertyChange("size", oldSize, 0)
    }

    int size() {
       _transforms.size()
    }

    void propertyChange(PropertyChangeEvent event) {
       if(event.source == _transforms){
           handleElementEvent(event)
       } else {
           super.propertyChange(event)
       }
    }

    protected void handleElementEvent(ElementEvent event) {
      switch( event.type ) {
         case ElementEvent.ADDED:
             event.newValue.addPropertyChangeListener(this)
             break
         case ElementEvent.REMOVED:
             event.newValue.removePropertyChangeListener(this)
         case ElementEvent.MULTI_ADD:
             event.values.each { it.addPropertyChangeListener(this) }
             break
         case ElementEvent.CLEARED:
         case ElementEvent.MULTI_REMOVE:
             event.values.each { it.removePropertyChangeListener(this) }
         case ElementEvent.UPDATED:
             break
      }
      firePropertyChange("transforms", OLDVALUE, Collections.unmodifiableList(_transforms))
   }
}
