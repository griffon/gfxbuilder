/*
 * @(#)CharcoalStroke.java
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

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;

import com.bric.geom.GeneralPathWriter;

/** This applies the {@link CharcoalEffect} to another <code>Stroke</code>.
 * 
 * By default this is built on top of a <code>BasicStroke</code>, but you can
 * use any other relatively simple stroke.  The nature of the charcoal effect
 * requires continuous areas of at least 2 pixels to really be visible.
 * <P>  It is not recommended to layer a <code>CharcoalStroke</code> on top
 * of another <code>CharcoalStroke</code>, because they are very
 * complex.
 * 
 * @version 1.1
 */
public class CharcoalStroke implements FilteredStroke {
	Stroke stroke;
	float size, angle;
	int randomSeed;
	
	/** Create a new <code>CharcoalStroke</code> built on top of
	 * a <code>BasicStroke</code>.
	 * 
	 * @param width the width of the <code>BasicStroke</code>.
	 * @param crackSize a value from 0-1 indicating how deep the crack should be.
	 * @param angle the angle, in radians.
	 */
	public CharcoalStroke(float width,float crackSize,float angle) {
		this(new BasicStroke(width),crackSize,angle,0);
	}
	
	/** Create a new <code>CharcoalStroke</code> built on top of
	 * another <code>Stroke</code>.
	 * 
	 * @param s the stroke to apply the charcoal effect to.
	 * @param crackSize a value from 0-1 indicating how deep the crack should be.
	 * @param angle the angle, in radians.
	 * @param randomSeed the random seed to use.
	 */
	public CharcoalStroke(Stroke s,float crackSize,float angle,int randomSeed) {
		stroke = s;
		size = crackSize;
		this.angle = angle;
		this.randomSeed = randomSeed;
	}
	
	/** Returns the random seed this object uses. */
	public int getRandomSeed() {
		return randomSeed;
	}
	
	/** Returns the angle (in radians) this effect uses. */
	public float getAngle() {
		return angle;
	}
	
	/** This creates a <code>CharcoalStroke</code> on top of a
	 * simple <code>BasicStroke</code>, with a fixed angle of 45 degrees.
	 * 
	 * @param width the width of the <code>BasicStroke</code>
	 * @param crackSize a value from 0-1 indicating how deep the crack should be.
	 */
	public CharcoalStroke(float width,float crackSize) {
		this(new BasicStroke(width),crackSize,(float)(Math.PI/4),0);
	}
	
	/** Creates a similar stroke where only the crack depth (0-1) is
	 * redefined.
	 * @param cracks the new crack depth.
	 * @return a new stroke.
	 */
	public CharcoalStroke deriveStroke(float cracks) {
		return new CharcoalStroke(stroke,cracks,angle,randomSeed);
	}
	
	/** Creates a similar stroke where the underlying stroke is
	 * redefined.
	 * @param newStroke the new underlying Stroke.  This could be
	 * a <code>BasicShape</code>, or your own stroke.  It is not
	 * recommended to layer a <code>CharcoalStroke</code> on top
	 * of another <code>CharcoalStroke</code>, because they are very
	 * complex.
	 */
	public FilteredStroke deriveStroke(Stroke newStroke) {
		return new CharcoalStroke(newStroke,size,angle,randomSeed);
	}

	public Shape createStrokedShape(Shape p) {
		Shape shape = stroke.createStrokedShape(p);
		if(size==0)
			return shape;

		GeneralPath newShape = new GeneralPath(shape.getPathIterator(null).getWindingRule());
		GeneralPathWriter writer = new GeneralPathWriter(newShape);
		
		float maxDepth = Float.MAX_VALUE;
		if(stroke instanceof BasicStroke) {
			maxDepth = ((BasicStroke)stroke).getLineWidth()*2;
		}
		
		CharcoalEffect charcoal = new CharcoalEffect(writer, size, angle, randomSeed, maxDepth);
		
		charcoal.write(shape);
		
		return (newShape);
	}
	
	/** Returns the underlying stroke this <code>CharcoalStroke</code> is layered
	 * on top of.
	 */
	public Stroke getStroke() {
		return stroke;
	}
	
	/** Returns the crack size.  This is a float from 0 to 1 indicating how
	 * deep the cracks in this effect run.
	 */
	public float getCrackSize() {
		return size;
	}
}
