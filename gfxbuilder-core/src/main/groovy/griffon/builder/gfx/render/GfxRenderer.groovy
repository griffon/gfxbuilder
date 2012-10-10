/*
 * Copyright 2007-2012 the original author or authors.
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

import griffon.builder.gfx.GfxBuilder
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxNode
import griffon.builder.gfx.GfxUtils

import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
final class GfxRenderer {
    private GfxBuilder _gfxb
    RenderingHints renderingHints = new RenderingHints(null)

    public GfxRenderer(){
       _gfxb = new GfxBuilder()
    }

    public GfxBuilder getGfxBuilder(){
       return _gfxb
    }

    /**
     * Renders an image.<br>
     * Assumes that the closure contains nodes that GfxBuilder can understand.
     * Will create a compatible BufferedImage with dimensions [width,height] and with
     * a clip set to the rectangle [0,0,width,height]
     *
     * @param width the width of the image/clip
     * @param height the height of the image/clip
     * @param closure a closure containg GfxBuilder's nodes
     */
    public BufferedImage render(int width, int height, Closure closure) {
       return render( width, height, _gfxb.group(closure) )
    }

    /**
     * Renders an image.<br>
     * Will create a compatible BufferedImage with dimensions [width,height] and with
     * a clip set to the rectangle [0,0,width,height]
     *
     * @param width the width of the image/clip
     * @param height the height of the image/clip
     * @param node any GfxNode
     */
    public BufferedImage render(int width, int height, GfxNode node) {
       return render( GfxUtils.createCompatibleImage(width, height, true), node )
    }

    /**
     * Renders an image.<br>
     * Assumes that the closure contains nodes that GfxBuilder can understand.
     * Will create a compatible BufferedImage with dimensions [clip.width,clip.height] and with
     * a clip set to the clip parameter
     *
     * @param clip the dimensions of the image/clip
     * @param closure a closure containg GfxBuilder's nodes
     */
    public BufferedImage render(Rectangle clip, Closure closure) {
       return render( clip, _gfxb.group(closure) )
    }

    /**
     * Renders an image.<br>
     * Will create a compatible BufferedImage with dimensions [clip.width,clip.height] and with
     * a clip set to the clip parameter
     *
     * @param clip the dimensions of the image/clip
     * @param node any GfxNode
     */
    public BufferedImage render(Rectangle clip, GfxNode node) {
       return render( GfxUtils.createCompatibleImage(clip.width as int, clip.height as int, true), clip, node )
    }

    /**
     * Renders an image.<br>
     * Assumes that the closure contains nodes that GfxBuilder can understand.
     *
     * @param image the destination image
     * @param closure a closure containg GfxBuilder's nodes
     */
    public BufferedImage render(BufferedImage dst, Closure closure) {
       return render( dst, _gfxb.group(closure) )
    }

    /**
     * Renders an image.<br>
     *
     * @param image the destination image
     * @param node any GfxNode
     */
    public BufferedImage render(BufferedImage dst, GfxNode node) {
       return render( dst, [0,0,dst.width,dst.height] as Rectangle, node )
    }

    /**
     * Renders an image.<br>
     * Assumes that the closure contains nodes that GfxBuilder can understand.
     * Will set a clip as defined by the clip parameter
     *
     * @param image the destination image
     * @param clip the dimensions of the clip
     * @param closure a closure containg GfxBuilder's nodes
     */
    public BufferedImage render(BufferedImage dst, Rectangle clip, Closure closure) {
       return render( dst, clip, _gfxb.group(closure) )
    }

    /**
     * Renders an image.<br>
     * Will set a clip as defined by the clip parameter
     *
     * @param image the destination image
     * @param clip the dimensions of the clip
     * @param node any GfxNode
     */
    public BufferedImage render(BufferedImage dst, Rectangle clip, GfxNode node) {
       def context = new GfxContext()
       def g = dst.createGraphics()
       g.renderingHints = renderingHints
       def cb = g.clipBounds
       g.setClip( clip )
       context.g = g
       node.apply(context)
       g.dispose()
       if( !cb ) g.setClip( cb )
       return dst
    }

    /**
     * Writes an image to a file.<br>
     * Assumes that the filename follows the unix conventions ("/" as file separator)
     * and the it ends with a file extension recognizable by the plugins registered
     * with javax.imageio.ImageIO.
     * Assumes that the closure contains nodes that GfxBuilder can understand.
     * Will create a compatible BufferedImage with dimensions [width,height] and with
     * a clip set to the rectangle [0,0,width,height]
     *
     * @param filename the name of the file where the image will be written
     * @param width the width of the image/clip
     * @param height the height of the image/clip
     * @param closure a closure containg GfxBuilder's nodes
     *
     * @throws IOException if the file can't be created and writen to.
     *
     * @return a File reference to the written image
     */
    public File renderToFile(String filename, int width, int height, Closure closure) {
       return renderToFile( filename, width, height, _gfxb.group(closure) )
    }

    /**
     * Writes an image to a file.<br>
     * Assumes that the filename follows the unix conventions ("/" as file separator)
     * and the it ends with a file extension recognizable by the plugins registered
     * with javax.imageio.ImageIO.
     * Assumes that the closure contains nodes that GfxBuilder can understand.
     * Will create a compatible BufferedImage with dimensions [width,height] and with
     * a clip set to the rectangle [0,0,width,height]
     *
     * @param filename the name of the file where the image will be written
     * @param width the width of the image/clip
     * @param height the height of the image/clip
     * @param node any GfxNode
     *
     * @throws IOException if the file can't be created and writen to.
     *
     * @return a File reference to the written image
     */
    public File renderToFile(String filename, int width, int height, GfxNode node) {
       return renderToFile( filename, GfxUtils.createCompatibleImage(width, height, withAlpha(filename)), node )
    }

    /**
     * Writes an image to a file.<br>
     * Assumes that the filename follows the unix conventions ("/" as file separator)
     * and the it ends with a file extension recognizable by the plugins registered
     * with javax.imageio.ImageIO.
     * Assumes that the closure contains nodes that GfxBuilder can understand.
     * Will create a compatible BufferedImage with dimensions [clip.width,clip.height] and with
     * a clip set to the clip parameter
     *
     * @param filename the name of the file where the image will be written
     * @param clip the dimensions of the image/clip
     * @param closure a closure containg GfxBuilder's nodes
     *
     * @throws IOException if the file can't be created and writen to.
     *
     * @return a File reference to the written image
     */
    public File renderToFile(String filename, Rectangle clip, Closure closure) {
       return renderToFile( filename, clip, _gfxb.group(closure) )
    }

    /**
     * Writes an image to a file.<br>
     * Assumes that the filename follows the unix conventions ("/" as file separator)
     * and the it ends with a file extension recognizable by the plugins registered
     * with javax.imageio.ImageIO.
     * Will create a compatible BufferedImage with dimensions [clip.width,clip.height] and with
     * a clip set to the clip parameter
     *
     * @param filename the name of the file where the image will be written
     * @param clip the dimensions of the image/clip
     * @param node any GfxNode
     *
     * @throws IOException if the file can't be created and writen to.
     *
     * @return a File reference to the written image
     */
    public File renderToFile(String filename, Rectangle clip, GfxNode node) {
       return renderToFile(filename, GfxUtils.createCompatibleImage(
             clip.width as int, clip.height as int, withAlpha(filename)), clip, node)
    }

    /**
     * Writes an image to a file.<br>
     * Assumes that the filename follows the unix conventions ("/" as file separator)
     * and the it ends with a file extension recognizable by the plugins registered
     * with javax.imageio.ImageIO.
     * Assumes that the closure contains nodes that GfxBuilder can understand.
     *
     * @param filename the name of the file where the image will be written
     * @param image the destination image
     * @param closure a closure containg GfxBuilder's nodes
     *
     * @throws IOException if the file can't be created and writen to.
     *
     * @return a File reference to the written image
     */
    public File renderToFile(String filename, BufferedImage dst, Closure closure) {
       return renderToFile( filename, dst, [0,0,dst.width,dst.height] as Rectangle, _gfxb.group(closure) )
    }

    /**
     * Writes an image to a file.<br>
     * Assumes that the filename follows the unix conventions ("/" as file separator)
     * and the it ends with a file extension recognizable by the plugins registered
     * with javax.imageio.ImageIO.
     *
     * @param filename the name of the file where the image will be written
     * @param image the destination image
     * @param node any GfxNode
     *
     * @throws IOException if the file can't be created and writen to.
     *
     * @return a File reference to the written image
     */
    public File renderToFile(String filename, BufferedImage dst, GfxNode node) {
       return renderToFile( filename, dst, [0,0,dst.width,dst.height] as Rectangle, node )
    }

    /**
     * Writes an image to a file.<br>
     * Assumes that the filename follows the unix conventions ("/" as file separator)
     * and the it ends with a file extension recognizable by the plugins registered
     * with javax.imageio.ImageIO.
     * Assumes that the closure contains nodes that GfxBuilder can understand.
     * Will set a clip as defined by the clip parameter
     *
     * @param filename the name of the file where the image will be written
     * @param image the destination image
     * @param clip the dimensions of the clip
     * @param closure a closure containg GfxBuilder's nodes
     *
     * @throws IOException if the file can't be created and writen to.
     *
     * @return a File reference to the written image
     */
    public File renderToFile(String filename, BufferedImage dst, Rectangle clip, Closure closure) {
       return renderToFile( filename, dst, clip, _gfxb.group(closure) )
    }

    /**
     * Writes an image to a file.<br>
     * Assumes that the filename follows the unix conventions ("/" as file separator)
     * and the it ends with a file extension recognizable by the plugins registered
     * with javax.imageio.ImageIO.
     *
     * @param filename the name of the file where the image will be written
     * @param image the destination image
     * @param clip the dimensions of the clip
     * @param node any GfxNode
     *
     * @throws IOException if the file can't be created and writen to.
     *
     * @return a File reference to the written image
     */
    public File renderToFile(String filename, BufferedImage dst, Rectangle clip, GfxNode node) {
       def fileSeparator = "/" /*System.getProperty("file.separator")*/
       def file = null
       def extension = "png"

       if(filename.lastIndexOf(fileSeparator) != -1){
          def dirs = filename[0..(filename.lastIndexOf(fileSeparator)-1)]
          def fname = filename[(filename.lastIndexOf(fileSeparator)+1)..-1]
          extension = fname[(fname.lastIndexOf(".")+1)..-1]
          File parent = new File(dirs)
          parent.mkdirs()
          file = new File(parent,fname)
       } else {
          file = new File(filename)
          extension = filename[(filename.lastIndexOf(".")+1)..-1]
       }

       if(!dst) dst = GfxUtils.createCompatibleImage(width, height, withAlpha(filename))
       ImageIO.write(render(dst, clip, node), extension, file)
       return file
    }

    private boolean withAlpha(String filename) {
       return filename.toLowerCase() ==~ /.*\.png/ || filename.toLowerCase() ==~ /.*\.gif/
    }
}