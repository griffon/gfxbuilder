/*
 * Copyright 2008-2010 the original author or authors.
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

package griffon.builder.gfx;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a groovy property, and indicates that it should be a bound
 * property according to the JavaBeans spec, announding to listeners that
 * the value has changed.
 *
 * It is a compilation error to place this annotation on a field (that is
 * nota property, i.e. has scope visibility modifiers).
 *
 * It is a compilation error to place this annotation on a property with
 * a user defined settter.
 *
 * @see GfxAttributeASTTransformation
 *
 * @author Danno Ferrin (shemnon)
 * @author Andres Almiray (aalmiray)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@GroovyASTTransformationClass("griffon.builder.gfx.ast.GfxAttributeASTTransformation")
public @interface GfxAttribute {
   String alias() default "";
   boolean resets() default true;
}
