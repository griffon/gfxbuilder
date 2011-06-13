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

package griffon.builder.gfx.factory

import java.awt.AlphaComposite

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class AlphaCompositeFactory extends AbstractGfxFactory {
   public boolean isLeaf(){
      return true
   }

   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value,
         Map properties ) throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, AlphaComposite) ){
         return value
      }else{
         def rule = properties.remove("rule")
         def alpha = properties.remove("alpha")

         if( rule == null ){
            throw new IllegalArgumentException("Null value for alphaComposite.rule")
         }

         def composite = null
         if( rule instanceof Number ){
            if( alpha != null ){
               return AlphaComposite.getInstance(rule, alpha as float)
            }else{
               return AlphaComposite.getInstance(rule)
            }
         }else if( rule instanceof String ){
            def r = rule.toUpperCase().replaceAll(" ","_")
            if(alpha != null){
               return AlphaComposite.getInstance(AlphaComposite."$r", alpha as float)
            }else{
               return AlphaComposite.getInstance(AlphaComposite."$r")
            }
         }
         throw new IllegalArgumentException("Invalid value for alphaComposite")
      }
   }

   public void setParent( FactoryBuilderSupport builder, Object parent, Object child ){
      // empty
   }
}