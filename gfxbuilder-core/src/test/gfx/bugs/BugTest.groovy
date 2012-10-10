package gfx.bugs

import gfx.test.TestUtils

import com.jhlabs.image.GrayscaleFilter
import griffon.builder.gfx.GfxBuilder

import java.awt.image.BufferedImage

/**
 * @author Alexander Klein <info@aklein.org>
 */
class BugTest extends GroovyTestCase {
    void testBorderWithGradientPaint() {
        def gfx = new GfxBuilder()
        def node = gfx.group {
            background(color('#FFFFFF'))
            rect(x: 10, y: 10, width: 200, height: 200, borderWidth: 5) {
                borderPaint {
                    // Nested elements of Paint had been invalid
                    gradientPaint(color1: color('green'), color2: color('red'))
                }
            }
        }
        BufferedImage img = TestUtils.createImage([300, 300], node, false, "BorderWithGradientPaint", true)
        //green on the left
        TestUtils.assertPixel img, 10, 10, [0, 255, 0, 255], 5
        TestUtils.assertPixel img, 10, 210, [0, 255, 0, 255], 5
        //red on the right
        TestUtils.assertPixel img, 210, 10, [255, 0, 0, 255], 5
        TestUtils.assertPixel img, 210, 210, [255, 0, 0, 255], 5
        //olive in the middle
        TestUtils.assertPixel img, 110, 10, [127, 127, 0, 255], 5
        TestUtils.assertPixel img, 110, 210, [127, 127, 0, 255], 5
        // white in the four corners
        TestUtils.assertPixel img, 16, 16, [255, 255, 255, 255]
        TestUtils.assertPixel img, 16, 204, [255, 255, 255, 255]
        TestUtils.assertPixel img, 204, 16, [255, 255, 255, 255]
        gfx.test.TestUtils.assertPixel img, 204, 204, [255, 255, 255, 255]

    }

    void testRectWithoutBorder() {
        def gfx = new GfxBuilder()
        def node = gfx.group {
            background(color('#FFFFFF'))
            // borderWidth == 0 hadn't been taken into account
            rect(x: 10, y: 10, width: 200, height: 200, borderWidth: 0) {
                gradientPaint(color1: color('green'), color2: color('red'))
            }
        }
        BufferedImage img = TestUtils.createImage([300, 300], node, false, "RectWithoutBorder", true)
        //green on the left
        TestUtils.assertPixel img, 11, 11, [0, 255, 0, 255], 5
        TestUtils.assertPixel img, 11, 209, [0, 255, 0, 255], 5
        //red on the right
        TestUtils.assertPixel img, 209, 11, [255, 0, 0, 255], 5
        TestUtils.assertPixel img, 209, 209, [255, 0, 0, 255], 5
        //olive in the middle
        TestUtils.assertPixel img, 110, 11, [127, 127, 0, 255], 5
        TestUtils.assertPixel img, 110, 209, [127, 127, 0, 255], 5
        //olive in the center
        TestUtils.assertPixel img, 110, 105, [127, 127, 0, 255], 5
    }

    void testLinearGradientDoesNotStopOnCorrectPosition() {
        def gfx = new GfxBuilder()
        def node = gfx.group {
            background(color('#FFFFFF'))
            // linearGradient always on x2=100
            rect(x: 10, y: 10, width: 200, height: 200, borderWidth: 0) {
                linearGradient {
                    stop(offset: 0.0, color: color('#00FF00'))
                    stop(offset: 0.5, color: color('#FF0000'))
                    stop(offset: 1.0, color: color('#0000FF'))
                }
            }
        }
        BufferedImage img = TestUtils.createImage([300, 300], node, false, "LinearGradientDoesNotStopOnCorrectPosition", true)
        //green on the left
        TestUtils.assertPixel img, 11, 11, [0, 255, 0, 255], 5
        TestUtils.assertPixel img, 11, 209, [0, 255, 0, 255], 5
        //blue on the right _localShape
        TestUtils.assertPixel img, 209, 11, [0, 0, 255, 255], 5
        TestUtils.assertPixel img, 209, 209, [0, 0, 255, 255], 5
        //red in the middle
        TestUtils.assertPixel img, 110, 11, [255, 0, 0, 255], 5
        TestUtils.assertPixel img, 110, 209, [255, 0, 0, 255], 5
        //olive on one quarte
        TestUtils.assertPixel img, 60, 11, [127, 127, 0, 255], 5
        TestUtils.assertPixel img, 60, 209, [127, 127, 0, 255], 5
        //olive on three quartes
        TestUtils.assertPixel img, 160, 11, [127, 0, 127, 255], 5
        TestUtils.assertPixel img, 160, 209, [127, 0, 127, 255], 5
    }

    void testOutlines() {
        def gfx = new GfxBuilder()
        def node = gfx.group {
            line(x1: 10, y1: 20, x2: 300, y2: 40, borderColor: 'red', borderWidth: 1)
            cubicCurve(x1: 10, y1: 40, x2: 300, y2: 50, ctrlx1: 150, ctrly1: 150, ctrlx2: 295, ctrly2: 0, borderColor: 'orange', borderWidth: 1)
            polyline(points: [10, 50, 20, 50, 20, 70, 30, 70, 100, 50], borderColor: 'black', borderWidth: 1)
            quadCurve(x1: 10, y1: 80, x2: 300, y2: 80, ctrlx: 15, ctrly: 35, borderColor: 'cyan', borderWidth: 1)
        }
        BufferedImage img = TestUtils.createImage([350, 200], node, false, "Outlines", true)
        // line
        TestUtils.assertPixel img, 10, 20, [255, 0, 0, 255], 5
        TestUtils.assertPixel img, 300, 40, [255, 0, 0, 255], 5
        //cubicCurve
        TestUtils.assertPixel img, 10, 40, [255, 200, 0, 255], 5
        TestUtils.assertPixel img, 115, 81, [255, 200, 0, 255], 5
        TestUtils.assertPixel img, 300, 50, [255, 200, 0, 255], 5
        // polyline
        TestUtils.assertPixel img, 10, 50, [0, 0, 0, 255], 5
        TestUtils.assertPixel img, 20, 50, [0, 0, 0, 255], 5
        TestUtils.assertPixel img, 20, 70, [0, 0, 0, 255], 5
        TestUtils.assertPixel img, 30, 70, [0, 0, 0, 255], 5
        TestUtils.assertPixel img, 100, 50, [0, 0, 0, 255], 5
        //quadCurve
        TestUtils.assertPixel img, 10, 80, [0, 255, 255, 255], 5
        TestUtils.assertPixel img, 185, 64, [0, 255, 255, 255], 5
        TestUtils.assertPixel img, 300, 80, [0, 255, 255, 255], 5
    }

    /**
     * GRIFFON_154
     */
    void testSingleLineInGroup() {
        def gfx = new GfxBuilder()
        def node = gfx.group {
            line(x1: 10, y1: 20, x2: 300, y2: 40, borderColor: 'red', borderWidth: 1)
        }
        BufferedImage img = TestUtils.createImage([350, 200], node, false, "SingleLineInGroup", true)
        TestUtils.assertPixel img, 10, 20, [255, 0, 0, 255], 5
        TestUtils.assertPixel img, 300, 40, [255, 0, 0, 255], 5
    }

    /**
     * GRIFFON_154
     */
    void testMultipleLinesInGroup() {
        def gfx = new GfxBuilder()
        def node = gfx.group {
            line(x1: 10, y1: 20, x2: 300, y2: 40, borderColor: 'red', borderWidth: 1)
            line(x1: 10, y1: 30, x2: 300, y2: 50, borderColor: 'red', borderWidth: 1)
        }
        BufferedImage img = TestUtils.createImage([350, 200], node, false, "MultipleLinesInGroup", true)
        TestUtils.assertPixel img, 10, 20, [255, 0, 0, 255], 5
        TestUtils.assertPixel img, 300, 40, [255, 0, 0, 255], 5
        TestUtils.assertPixel img, 10, 30, [255, 0, 0, 255], 5
        TestUtils.assertPixel img, 300, 40, [255, 0, 0, 255], 5
    }

    void testNPEWithTextNodeBounds() {
        def gfx = new GfxBuilder()
        def cards = [width: 200, height: 150]
        def node = gfx.group(bc: 'none') {
            antialias true
            rect(x: 0, y: 0, w: cards.width, h: cards.height) {
                gradientPaint(x1: 100, x2: 0, color2: color('blue'))
            }
            text(x: 0, y: 0, cx: cards.width / 2, cy: cards.height / 2, text: 'B', f: color('white')) {
                font(face: "Helvetica", size: (3 * cards.width) / 4)
            }
        }
        BufferedImage img = TestUtils.createImage([350, 200], node, false, "NPEWithTextNodeBounds", true)
    }

    void testFilter() {
        def gfx = new GfxBuilder()
        def node = gfx.group(bc: 'none') {
            antialias true
            rect(x: 50, y: 50, w: 100, h: 100, fill: color('red'), filter: new GrayscaleFilter())
        }
        BufferedImage img = TestUtils.createImage([200, 200], node, false, "NPEWithTextNodeBounds", true)
        TestUtils.assertPixel img, 60, 60, [76, 76, 76, 255], 0
        node = gfx.group(bc: 'none', filter: new GrayscaleFilter()) {
            antialias true
            rect(x: 50, y: 50, w: 100, h: 100, fill: color('red'))
        }
        img = TestUtils.createImage([200, 200], node, false, "NPEWithTextNodeBounds", true)
        TestUtils.assertPixel img, 60, 60, [76, 76, 76, 255], 0
    }
}
