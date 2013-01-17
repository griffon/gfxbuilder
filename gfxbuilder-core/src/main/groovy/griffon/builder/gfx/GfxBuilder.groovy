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

import griffon.builder.gfx.factory.*
import griffon.builder.gfx.nodes.misc.*
import griffon.builder.gfx.nodes.outlines.CubicCurveNode
import griffon.builder.gfx.nodes.outlines.LineNode
import griffon.builder.gfx.nodes.outlines.PolylineNode
import griffon.builder.gfx.nodes.outlines.QuadCurveNode
import griffon.builder.gfx.nodes.paints.GradientPaintNode
import griffon.builder.gfx.nodes.paints.LinearGradientPaintNode
import griffon.builder.gfx.nodes.paints.RadialGradientPaintNode
import griffon.builder.gfx.nodes.paints.TexturePaintNode
import griffon.builder.gfx.nodes.shapes.*
import griffon.builder.gfx.nodes.shapes.path.*
import griffon.builder.gfx.nodes.strokes.*
import griffon.builder.gfx.nodes.transforms.*
import groovy.swing.SwingBuilder
import groovy.swing.factory.BindFactory
import groovy.swing.factory.BindProxyFactory
import groovy.swing.factory.CollectionFactory
import org.codehaus.griffon.jsilhouette.geom.ReuleauxTriangle

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class GfxBuilder extends FactoryBuilderSupport {
    static {
        GfxUtils.enhanceShapes()
        GfxUtils.enhanceColor()
        GfxUtils.enhanceBasicStroke()
    }

    public GfxBuilder(boolean init = true) {
        super(false)
        if (init) {
            gfxbAutoRegister()
        }
    }

    public gfxbAutoRegister() {
        // if java did atomic blocks, this would be one
        synchronized (this) {
            if (autoRegistrationRunning || autoRegistrationComplete) {
                // registration already done or in process, abort
                return
            }
        }
        autoRegistrationRunning = true
        try {
            gfxbCallAutoRegisterMethods(getClass())
        } finally {
            autoRegistrationComplete = true
            autoRegistrationRunning = false
        }
    }

    private void gfxbCallAutoRegisterMethods(Class declaredClass) {
        if (declaredClass == null) {
            return
        }
        gfxbCallAutoRegisterMethods(declaredClass.getSuperclass())

        for (Method method in declaredClass.getDeclaredMethods()) {
            if (method.getName().startsWith("register") && method.getParameterTypes().length == 0) {
                registringGroupName = method.getName().substring("register".length())
                registrationGroup.put(registringGroupName, new TreeSet<String>())
                try {
                    if (Modifier.isPublic(method.getModifiers())) {
                        method.invoke(this)
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Could not init ${this.class.name} because of an access error in ${declaredClass.name}.${method.name}", e)
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Could not init ${this.class.name} because of an exception in ${declaredClass.name}.${method.name}", e)
                } finally {
                    registringGroupName = ""
                }
            }
        }

        ServiceLoader.load(GfxBuilderPlugin)?.each { plugin ->
            plugin.properties.each { name, closure ->
                if (!name.startsWith("register")) return
                registringGroupName = name.substring("register".length())
                if (registrationGroup.get(registringGroupName) == null) {
                    registrationGroup.put(registringGroupName, new TreeSet<String>())
                }
                try {
                    closure.resolveStrategy = Closure.DELEGATE_FIRST
                    closure.delegate = this
                    closure()
                } catch (Exception e) {
                    throw new RuntimeException("Could not init ${this.class.name} because of an exception in ${plugin.class.name}.${property.name}", e)
                } finally {
                    registringGroupName = ""
                }
            }
        }
    }

    private registerGfxBeanFactory(String name, Class beanClass) {
        registerFactory(name, new GfxBeanFactory(beanClass, false))
    }

    private registerGfxBeanFactory(String name, Class beanClass, boolean leaf) {
        registerFactory(name, new GfxBeanFactory(beanClass, leaf))
    }

    void registerGfxSupportNodes() {
        addAttributeDelegate(GfxBuilder.&objectIDAttributeDelegate)

//         registerFactory( "draw", new DrawFactory() )
        registerFactory("font", new FontFactory())
        registerGfxBeanFactory("group", GroupNode)
        registerGfxBeanFactory("renderingHint", RenderingHintNode, true)
        registerFactory("noparent", new CollectionFactory())
//         registerFactory( "shape", new ShapeFactory() )

        // binding related classes
        BindFactory bindFactory = new BindFactory()
        registerFactory("bind", bindFactory)
        addAttributeDelegate(bindFactory.&bindingAttributeDelegate)
        registerFactory("bindProxy", new BindProxyFactory())

        registerGfxBeanFactory("image", ImageNode)
        registerFactory("color", new ColorFactory())
        registerFactory("rgba", factories.color)
        registerFactory("hsl", new HSLColorFactory())
        registerFactory("clip", new ClipFactory())
        registerFactory("antialias", new AntialiasFactory())
//         registerFactory("alphaComposite", new AlphaCompositeFactory())
        registerGfxBeanFactory("alphaComposite", AlphaCompositeNode, true)
        registerGfxBeanFactory("composite", CompositeNode, true)
//         registerFactory( "viewBox", new ViewBoxFactory() )
        registerFactory("props", new PropsFactory())
        registerFactory("background", new BackgroundFactory())
        registerFactory("customNode", new CustomNodeFactory())
        registerFactory("rawNode", new RawFactory())

        // variables
        variables['on'] = true
        variables['yes'] = true
        variables['off'] = false
        variables['no'] = false
    }

    void registerGfxShapes() {
        registerGfxBeanFactory("arc", ArcNode)
        registerGfxBeanFactory("circle", CircleNode)
        registerGfxBeanFactory("ellipse", EllipseNode)
        registerGfxBeanFactory("polygon", PolygonNode)
        registerGfxBeanFactory("rect", RectangleNode)
        registerGfxBeanFactory("text", TextNode)
        registerGfxBeanFactory("almond", AlmondNode)
        registerGfxBeanFactory("arrow", ArrowNode)
        registerGfxBeanFactory("asterisk", AsteriskNode)
        registerGfxBeanFactory("astroid", AstroidNode)
        registerGfxBeanFactory("balloon", BalloonNode)
        registerGfxBeanFactory("cross", CrossNode)
        registerGfxBeanFactory("donut", DonutNode)
        registerGfxBeanFactory("fan", FanNode)
        registerGfxBeanFactory("lauburu", LauburuNode)
        registerGfxBeanFactory("multiRoundRect", MultiRoundRectangleNode)
        registerGfxBeanFactory("rays", RaysNode)
        registerGfxBeanFactory("regularPolygon", RegularPolygonNode)
        registerGfxBeanFactory("reuleauxTriangle", ReuleauxTriangle)
        registerGfxBeanFactory("pin", RoundPinNode)
        registerGfxBeanFactory("star", StarNode)
        registerGfxBeanFactory("triangle", TriangleNode)

        //
        // paths
        //
        registerGfxBeanFactory("path", PathNode)
        registerFactory("moveTo", new PathSegmentFactory(MoveToPathSegment))
        registerFactory("lineTo", new PathSegmentFactory(LineToPathSegment))
        registerFactory("quadTo", new PathSegmentFactory(QuadToPathSegment))
        registerFactory("curveTo", new PathSegmentFactory(CurveToPathSegment))
        registerFactory("hline", new PathSegmentFactory(HLinePathSegment))
        registerFactory("vline", new PathSegmentFactory(VLinePathSegment))
        registerFactory("shapeTo", new ShapePathSegmentFactory())
        registerFactory("close", new PathSegmentFactory(ClosePathSegment))

        //
        // outlines
        //
        registerGfxBeanFactory("line", LineNode)
        registerGfxBeanFactory("cubicCurve", CubicCurveNode)
        registerGfxBeanFactory("polyline", PolylineNode)
        registerGfxBeanFactory("quadCurve", QuadCurveNode)

        //
        // area operations
        //
        registerFactory("add", new AreaFactory("add", "add"))
        registerFactory("subtract", new AreaFactory("subtract", "subtract"))
        registerFactory("intersect", new AreaFactory("intersect", "intersect"))
        registerFactory("xor", new AreaFactory("xor", "exclusiveOr"))
    }

    void registerGfxTransforms() {
        registerFactory("transforms", new TransformsFactory())
        registerFactory("rotate", new TransformFactory(RotateTransform))
        registerFactory("scale", new TransformFactory(ScaleTransform))
        registerFactory("shear", new TransformFactory(ShearTransform))
        registerFactory("translate", new TransformFactory(TranslateTransform))
        registerFactory("matrix", new TransformFactory(MatrixTransform))
        registerFactory("transform", new TransformTransformFactory())
    }

    void registerGfxPaints() {
        registerFactory("borderPaint", new BorderPaintFactory())
        registerGfxBeanFactory("gradientPaint", GradientPaintNode, true)
        registerFactory("multiPaint", new MultiPaintFactory())
//         registerFactory( "paint", new PaintFactory() )
        registerGfxBeanFactory("texturePaint", TexturePaintNode, true)
        registerFactory("colorPaint", new ColorPaintFactory())
        registerFactory("stop", new GradientStopFactory())
        registerGfxBeanFactory("linearGradient", LinearGradientPaintNode)
        registerGfxBeanFactory("radialGradient", RadialGradientPaintNode)
    }

    void registerGfxStrokes() {
        registerFactory("stroke", new StrokeFactory())
        registerFactory("basicStroke", new StrokesFactory(BasicStrokeNode))
        registerFactory("compositeStroke", new StrokesFactory(CompositeStrokeNode, false))
        registerFactory("compoundStroke", new StrokesFactory(CompoundStrokeNode, false))
        registerFactory("textStroke", new StrokesFactory(TextStrokeNode))
        registerFactory("shapeStroke", new ShapeStrokeFactory())
        registerFactory("wobbleStroke", new StrokesFactory(WobbleStrokeNode))
        registerFactory("zigzagStroke", new StrokesFactory(ZigzagStrokeNode, false))
        registerFactory("bristleStroke", new StrokesFactory(BristleStrokeNode))
        registerFactory("brushStroke", new StrokesFactory(BrushStrokeNode))
        registerFactory("calligraphyStroke", new StrokesFactory(CalligraphyStrokeNode))
        registerFactory("charcoalStroke", new StrokesFactory(CharcoalStrokeNode, false))
    }
    //
    // filters
    //
//         registerFactory( "filters", new FilterGroupFactory() )

    void registerGfxSwing() {
        registerFactory("canvas", new GfxCanvasFactory())
    }

    public static objectIDAttributeDelegate(def builder, def node, def attributes) {
        def theID = attributes.remove('id')
        if (theID) {
            builder.setVariable(theID, node)
        }
    }

    // Fix to prevent nodes without context to throw MPE when asked for id (e.g. in BindFactory)
    public Object getProperty(String property) {
        try {
            return super.getProperty(property)
        } catch (MissingPropertyException mpe) {
            if (property == SwingBuilder.DELEGATE_PROPERTY_OBJECT_ID)
                return null
            throw mpe
        }
    }
}
