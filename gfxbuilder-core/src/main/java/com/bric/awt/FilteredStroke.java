/*
 * @(#)FilteredStroke.java
 *
 * Copyright (c) 2009 by Jeremy Wood.
 * All rights reserved.
 *
 * The copyright of this software is owned by Jeremy Wood. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Jeremy Wood. For details see accompanying license terms.
 * 
 * This software is probably, but not necessarily, discussed here:
 * http://javagraphics.blogspot.com/
 * 
 * And the latest version should be available here:
 * https://javagraphics.dev.java.net/
 */
package com.bric.awt;

import java.awt.Stroke;

/** This is a <code>Stroke</code> that modifies or sits on top of
 * another <code>Stroke</code>.
 * 
 * This model is especially convenient when you design a GUI to
 * manipulate the properties of your <code>Stroke</code>.
 * 
 * @version 1.0
 */
public interface FilteredStroke extends Stroke {
	/** Returns the underlying stroke being filtered. */
	public Stroke getStroke();
	
	/** Similar to <code>Font.deriveFont()</code>, this makes
	 * a stroke similar to this object, except the underlying
	 * <code>Stroke</code> this stroke filters is replaced.
	 * 
	 * @param s the new underlying stroke to use.
	 * @return a new stroke that is built on top of <code>s</code>
	 */
	public FilteredStroke deriveStroke(Stroke s);
}
