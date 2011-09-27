package gfx.test

import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxNode
import griffon.builder.gfx.swing.GfxCanvas
import groovy.swing.SwingBuilder
import java.awt.GraphicsConfiguration
import java.awt.GraphicsEnvironment
import java.awt.Transparency
import java.awt.event.WindowAdapter
import java.awt.image.BufferedImage
import java.util.concurrent.CountDownLatch
import javax.swing.JFrame
import javax.swing.SwingConstants
import java.awt.Image
import java.awt.image.DataBuffer
import java.awt.Graphics2D
import com.sun.image.codec.jpeg.JPEGImageEncoder
import com.sun.image.codec.jpeg.JPEGCodec
import com.sun.image.codec.jpeg.JPEGEncodeParam

/**
 * @author Alexander Klein <info@aklein.org>
 */
class TestUtils {

  static void displayInFrame(def title, GfxNode node, List size = [400, 400], def wait = false) {
    def swing = new SwingBuilder()
    CountDownLatch closeSignal = new CountDownLatch(1);
    swing.edt {
      frame(id: 'main',
              title: title,
              defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
              size: size,
              visible: true) {
        panel(background: java.awt.Color.WHITE) {
        borderLayout()
        widget(new GfxCanvas(node: node, opaque: false))
        }
      }
    }
    if (wait) {
      swing.main.addWindowListener([windowClosed: {evt ->
        closeSignal.countDown()
      }] as WindowAdapter)
      closeSignal.await()
    }
  }

  static void displayInFrame(def title, BufferedImage img, def wait = false) {
    def swing = new SwingBuilder()
    CountDownLatch closeSignal = new CountDownLatch(1);
    swing.edt {
      frame(id: 'main',
              title: title,
              defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
              size: [img.width + 100, img.height + 100],
              visible: true) {
        panel(background: java.awt.Color.WHITE) {
        borderLayout()
        label(icon: imageIcon(image: img), horizontalAlignment: SwingConstants.CENTER, verticalAlignment: SwingConstants.CENTER)
        }
      }
    }
    if (wait) {
      swing.main.addWindowListener([windowClosed: {evt ->
        closeSignal.countDown()
      }] as WindowAdapter)
      closeSignal.await()
    }
  }

  static BufferedImage createImage(List size = [400, 400], def node, boolean show = false, def title = '', boolean wait = false) {
    GraphicsConfiguration gc = GraphicsEnvironment.localGraphicsEnvironment.defaultScreenDevice.defaultConfiguration
    def img = gc.createCompatibleImage(size[0], size[1], Transparency.TRANSLUCENT)
    def context = new GfxContext()
    context.g = img.getGraphics()
    context.g.setClip(* ([0, 0] + size))
    context.eventTargets = []
    context.groupSettings = [:]
    node.apply(context)
    if(show) {
      displayInFrame(title, img, wait)
    }
    return img
  }

  static assertPixel(Image img, def x, def y, def rgba, def threshold = 0, boolean alpha = true) {
        def data = img.data.getPixel(x, y, new int[4])
        def check = { color, idx ->
            Range range = (Math.max(0, rgba[idx] - threshold))..(Math.min(255, rgba[idx] + threshold))
            assert range.contains(data[idx]): "$color(${data[idx]}) of $data not in range: ${range.toString()}"
        }
        check('red', 0)
        check('green', 1)
        check('blue', 2)
        if (alpha)
            check('alpha', 3)
    }

    static assertImages(Image img, Image reference) {
        DataBuffer db = img.data.dataBuffer
        DataBuffer ref = reference.data.dataBuffer
        for (int i = 0; i < db.size; i++) {
            assert ref.getElem(i) == db.getElem(i)
        }
    }

    static assertImagesSimilarity(Image img, Image reference, int columns, int rows) {
        DataBuffer db = img.data.dataBuffer
        DataBuffer ref = reference.data.dataBuffer
        for (int i = 0; i < db.size; i++) {
            assert ref.getElem(i) == db.getElem(i)
        }
    }

    static BufferedImage imageToBufferedImage(Image img) {
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB)
        Graphics2D g2 = bi.createGraphics()
        g2.drawImage(img, null, null)
        return bi
    }

    static void saveJPG(BufferedImage img, OutputStream out) throws IOException {
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out)
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(img)
        param.setQuality(0.8f, false)
        encoder.setJPEGEncodeParam(param)
        encoder.encode(img)
    }
}
