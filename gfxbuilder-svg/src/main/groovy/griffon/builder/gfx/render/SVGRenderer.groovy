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

package griffon.builder.gfx.render

import java.awt.*
import groovy.swing.j2d.*

import org.apache.batik.svggen.SVGGraphics2D
import org.apache.batik.dom.svg.SVGDOMImplementation

import org.w3c.dom.Document
import org.w3c.dom.DOMImplementation

import griffon.builder.gfx.GfxNode
import griffon.builder.gfx.GfxBuilder
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxUtils

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
final class SVGRenderer {
    private GfxBuilder _gfxb

    public SVGRenderer(){
       _gfxb = new GfxBuilder()
    }

    /**
     * Writes an svg document to a string.<br>
     *
     * @param width the width of the svg document
     * @param height the height of the svg document
     * @param closure a closure containg GfxBuilder's nodes
     *
     * @throws IOException if the string can't be writen.
     */
    public String render(int width, int height, Closure closure) {
       return render( width, height, _gfxb.group(closure) )
    }

    /**
     * Writes an svg document to a string.<br>
     *
     * @param width the width of the svg document
     * @param height the height of the svg document
     * @param node any GfxNode
     *
     * @throws IOException if the string can't be writen.
     */
    public String render(int width, int height, GfxNode node) {
       def sw = new StringWriter()
       renderToWriter( sw, width, height, node)
       return sw.buffer
    }

    /**
     * Writes an svg document to a writer.<br>
     *
     * @param writer a Writer
     * @param width the width of the svg document
     * @param height the height of the svg document
     * @param closure a closure containg GfxBuilder's nodes
     *
     * @throws IOException if the writer can't be writen to.
     */
    public void renderToWriter(Writer writer, int width, int height, Closure closure) {
       renderToWriter( writer, width, height, _gfxb.group(closure) )
    }

    /**
     * Writes an svg document to a writer.<br>
     *
     * @param writer a Writer
     * @param width the width of the svg document
     * @param height the height of the svg document
     * @param node any GfxNode
     *
     * @throws IOException if the writer can't be writen to.
     */
    public void renderToWriter(Writer writer, int width, int height, GfxNode node) {
       def svgGenerator = createGraphics( width, height, node)
       svgGenerator.stream( writer )
    }

    /**
     * Writes an svg document to a stream.<br>
     *
     * @param stream an OutputStream
     * @param width the width of the svg document
     * @param height the height of the svg document
     * @param closure a closure containg GfxBuilder's nodes
     *
     * @throws IOException if the stream can't be writen to.
     */
    public void renderToStream(OutputStream stream, int width, int height, Closure closure) {
       renderToStream( stream, width, height, _gfxb.group(closure) )
    }

    /**
     * Writes an svg document to a stream.<br>
     *
     * @param stream an OutputStream
     * @param width the width of the svg document
     * @param height the height of the svg document
     * @param node any GfxNode
     *
     * @throws IOException if the stream can't be writen to.
     */
    public void renderToStream( OutputStream stream, int width, int height, GfxNode node) {
       def svgGenerator = createGraphics( width, height, node)
       svgGenerator.stream( new OutputStreamWriter(stream,"UTF-8") )
    }

    /**
     * Writes an svg document to a file.<br>
     * Assumes that the filename follows the unix conventions ("/" as file separator)
     *
     * @param filename the name of the file where the movie will be written
     * @param width the width of the svg document
     * @param height the height of the svg document
     * @param closure a closure containg GfxBuilder's nodes
     *
     * @throws IOException if the file can't be created and writen to.
     */
    public File renderToFile(String filename, int width, int height, Closure closure) {
       return renderToFile( filename, width, height, _gfxb.group(closure) )
    }

    /**
     * Writes an image to a file.<br>
     * Assumes that the filename follows the unix conventions ("/" as file separator)
     *
     * @param filename the name of the file where the movie will be written
     * @param width the width of the svg document
     * @param height the height of the svg document
     * @param node any GfxNode
     *
     * @throws IOException if the file can't be created and writen to.
     */
    public File renderToFile(String filename, int width, int height, GfxNode node) {
       assert filename : "Must define a value for filename"

       filename -= ".svg"
       def parentDir = null
       def fileSeparator = "/"

       if( filename.lastIndexOf(fileSeparator) != -1 ){
          def dirs = filename[0..(filename.lastIndexOf(fileSeparator)-1)]
          parentDir = new File(dirs)
          parentDir.mkdirs()
       }

       def file = new File(filename+".svg")
       renderToStream( new FileOutputStream(file), width, height, node)
       return file
    }

    private SVGGraphics2D createGraphics(int width, int height, GfxNode node) {
       // Get a DOMImplementation.
       DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation()

       // Create an instance of org.w3c.dom.Document.
       Document document = domImpl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null)

       // Create an instance of the SVG Generator.
       SVGGraphics2D svgGenerator = new SVGGraphics2D(document)

       def context = new GfxContext()
       context.g = svgGenerator
       context.g.color = Color.BLACK
       context.g.clip = new Rectangle(0,0,width,height)
       context.g.setSVGCanvasSize( new Dimension(width,height) )
       node.apply(context)

       return svgGenerator
    }
}