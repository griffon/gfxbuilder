/*
 * @(#)BrushStroke.java
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
import java.util.Random;

import com.bric.geom.GeneralPathWriter;
import com.bric.geom.InsetPathWriter;
import com.bric.geom.MeasuredShape;

/** This <code>Stroke</code> that resembles a brush.
 * <P>It's still a work in progress, but I'm distributing it for public
 * use in hopes I can get some useful feedback; maybe even some improvements?
 * 
 * @author mickleness@gmail.com
 *
 */
public class BrushStroke implements Stroke {
	private static BasicStroke[] thinStrokes = null;
	float width;
	float theta;
	float thickness = 0;
	BasicStroke[] strokes;
	int layers;
	float anchor;
	int randomSeed = 0;
	
	/** Creates a new BrushStroke using the default angle (pi/2).
	 * 
	 * @param width the width (in pixels) of this stroke
	 * @param thickness a float between zero and one indicating how
	 * "thick" this stroke should be.
	 * @param randomSeed the random seed for this stroke.
	 */
	public BrushStroke(float width,float thickness,int randomSeed) {
		this(width,thickness,(float)(Math.PI/2.0),randomSeed);
	}

	/** Creates a new BrushStroke using the default angle (pi/2) and
	 * a random seed of zero.
	 * 
	 * @param width the width (in pixels) of this stroke
	 * @param thickness a float between zero and one indicating how
	 * "thick" this stroke should be.
	 */
	public BrushStroke(float width,float thickness) {
		this(width,thickness,(float)(Math.PI/2.0), 0);
	}
	
	/** Creates a new BrushStroke.
	 * 
	 * @param width the width (in pixels) of this stroke
	 * @param thickness a float between zero and one indicating how
	 * "thick" this stroke should be.
	 * @param theta the angle stroke is drawn at to the original path.
	 * An angle of (pi/2) is considered "normal".  A value of 0 or pi
	 * is very extreme.
	 * @param randomSeed the random seed for this stroke.
	 */
	public BrushStroke(float width,float thickness,float theta,int randomSeed) {
		if(width<=0) throw new IllegalArgumentException("the width ("+width+") must be positive");
		if(thickness<0 || thickness>1)
			throw new IllegalArgumentException("the thickness ("+thickness+") must be between 0 and 1");
		this.width = width;
		this.thickness = thickness;
		this.randomSeed = randomSeed;
		this.theta = theta;
		layers = (int)(2*width)+2;
		strokes = new BasicStroke[layers];
		for(int a = 0; a<strokes.length; a++) {
			strokes[a] = new BasicStroke(((float)a+1)*width/((float)layers),BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,10);
		}
	}
	
	public int getRandomSeed() {
		return randomSeed;
	}
	
	public float getThickness() {
		return thickness;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getTheta() {
		return theta;
	}
	
	public Shape createStrokedShape(Shape p) {
		if(thinStrokes==null) {
			thinStrokes = new BasicStroke[100];
			for(int a = 0; a<thinStrokes.length; a++) {
				float f = .15f+(2.05f-.15f)*((float)a)/((float)thinStrokes.length);
				thinStrokes[a] = new BasicStroke(f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,10);
			}
		}
		
		GeneralPath path = new GeneralPath();
		
		Random r = new Random(randomSeed);
	
		float h = thickness*thickness;
		int thicknessMax = Math.min(thinStrokes.length, (int)(thinStrokes.length*h+thinStrokes.length*.2f));
		int thicknessMin = (int)(thinStrokes.length*h/2f);
		
		GeneralPath thisLayer = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		GeneralPathWriter writer = new GeneralPathWriter(thisLayer);		
		for(int a = 0; a<layers; a++) {
			writer.reset();
			float k1 = a*width/((float)layers-1f);
			float k2 = k1-width/2;
			InsetPathWriter insetWriter;
			if(k2>0) {
				insetWriter = new InsetPathWriter(writer,Math.abs(k2),theta);
			} else {
				insetWriter = new InsetPathWriter(writer,Math.abs(k2),(float)(Math.PI+theta));
			}
			insetWriter.write(p);
			MeasuredShape[] measuredLayers = MeasuredShape.getSubpaths(thisLayer);

			float minStreakDistance = (4+10*thickness)/1f;
			float maxStreakDistance = (40+10*thickness)/1f;
			float k3 = Math.abs(k2);
			float minGapDistance = (4+10*k3)/1f;
			float maxGapDistance = (40+10*k3)/1f;
			
			for(int b = 0; b<measuredLayers.length; b++) {
				r.setSeed(randomSeed+1000*a+10000*b);
				
				float x = 0;
				if(a!=layers/2) {
					float k4 = Math.abs(k2/width);
					x = (maxGapDistance-minGapDistance)*r.nextFloat()+k4*(.3f*r.nextFloat()+.7f)*minGapDistance;
				}
				
				boolean first = true;
				while(x<measuredLayers[b].getOriginalDistance()) {
					float streakDistance = minStreakDistance+(maxStreakDistance-minStreakDistance)*r.nextFloat();
					float gapDistance;
					if(first) {
						first = false;
						gapDistance = (.2f+.8f*r.nextFloat())*minGapDistance+(maxGapDistance-minGapDistance)*r.nextFloat();
					} else {
						gapDistance = minGapDistance+(maxGapDistance-minGapDistance)*r.nextFloat();
					}
					
					if(x+streakDistance>measuredLayers[b].getOriginalDistance()) {
						float z = 0;
						if(a!=layers/2)
							z = (maxGapDistance-minGapDistance)*r.nextFloat();
						streakDistance = measuredLayers[b].getOriginalDistance()-x-z;
					}
					if(streakDistance>0) {
						GeneralPath p2 = measuredLayers[b].getShape(x/measuredLayers[b].getDistance(),
								streakDistance/measuredLayers[b].getDistance());
						path.append(thinStrokes[r.nextInt(thicknessMax-thicknessMin)+thicknessMin].createStrokedShape(p2),false);
					}
				
					x = x+(streakDistance+gapDistance);
				}
			}
		}
		return path;
	}
}
