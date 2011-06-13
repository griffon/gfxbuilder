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

package griffon.builder.gfx

import java.awt.Color
import com.camick.awt.HSLColor

/**
 * A collection of named colors.<br>
 * Colors registers all Java, CSS2 and HTML colors by default.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class Colors {
    private static final Colors instance
    static{
        instance = new Colors()
        /*
        Colors.metaClass.'static'.propertyMissing = { String name, Object value ->
           setColor(name,value)
        }
        */
//         Colors.metaClass.'static'.propertyMissing << { String name ->
//            instance.getColor(name)
//         }
    }

    /*
     * Returns the singleton instance.
     *
    public static Colors getInstance() {
        return instance
    }
    */

    private Map customColors = new TreeMap()
    private Map standardColors = new TreeMap()

    private Colors() {
        initStandardColors()
    }

    /**
     * Retrieves a Color from the cache.<br>
     * It will look first into the user-defined custom colors, if not found then
     * it will try the standard colors.
     *
     * @param name the name of the color to retrieve
     * @return the named color, null if not found
     */
    public static Color getColor(String name) {
       return instance._getColor(name)
    }

    public static Color getColor(Color color) {
       return instance._getColor(color)
    }

    public static Color getColor(Number value) {
       return instance._getColor(value)
    }

    public static Color getColor(Map value) {
       return instance._getColor(value)
    }

    public static Color get(String name) {
       return instance._getColor(name)
    }

    public static Color get(Color color) {
       return instance._getColor(color)
    }

    public static Color get(Number value) {
       return instance._getColor(value)
    }

    public static Color get(Map value) {
       return instance._getColor(value)
    }

    /**
     * Stores a color on the cache.<br>
     * It will override any existing custom color with the same name.
     *
     * @param name the name of the color to store
     * @param color the Color to store
     */
    public static void setColor(String name, Color color) {
       instance._setColor(name,color)
    }

    public static void set(String name, Color color) {
       instance._setColor(name,color)
    }

    def propertyMissing(String name) {
       return getColor(name)
    }

    void propertyMissing(String name, value) {
       setColor( name, value )
    }

    private Color _getColor(String name) {
        if(!name) return null
        Color color = customColors[normalize(name)]
        if( color == null ) {
            color = standardColors[normalize(name)]
        }
        if( color == null && name.startsWith("#") ) {
           def cdef = name[1..-1]
           if( cdef.length() == 3 ) {
              color = new Color( Integer.parseInt("${cdef[0]}${cdef[0]}",16),
                                 Integer.parseInt("${cdef[1]}${cdef[1]}",16),
                                 Integer.parseInt("${cdef[2]}${cdef[2]}",16))
              customColors[normalize(name)] = color
           }else if( cdef.length() == 6 ) {
              color = new Color( Integer.parseInt(cdef[0..1],16),
                                 Integer.parseInt(cdef[2..3],16),
                                 Integer.parseInt(cdef[4..5],16))
              customColors[normalize(name)] = color
           }
        }
        return color
    }

    private Color _getColor(Color color) {
       return color
    }

    private Color _getColor(Number value) {
       return new Color(value.intValue(), true)
    }

    private Color _getColor(Map map) {
        if(map.red == null)        map.red        = map.remove("r")
        if(map.green == null)      map.green      = map.remove("g")
        if(map.blue == null)       map.blue       = map.remove("b")
        if(map.alpha == null)      map.alpha      = map.remove("a")
        if(map.hue == null)        map.hue        = map.remove("h")
        if(map.saturation == null) map.saturation = map.remove("s")
        if(map.luminance == null)  map.luminance  = map.remove("l")

        if( map.containsKey("red")   ||
            map.containsKey("green") ||
            map.containsKey("blue")  ||
            map.containsKey("alpha") ) {

            def red = map.remove("red")
            def green = map.remove("green")
            def blue = map.remove("blue")
            def alpha = map.remove("alpha")

            red = red != null ? red : 0
            green = green != null ? green : 0
            blue = blue != null ? blue : 0
            alpha = alpha != null ? alpha : 255

            red = red > 1 ? red/255 : red
            green = green > 1 ? green/255 : green
            blue = blue > 1 ? blue/255 : blue
            alpha = alpha > 1 ? alpha/255 : alpha

            return new Color(red as float, green as float, blue as float, alpha as float)
        } else if( map.containsKey("hue")        ||
                   map.containsKey("saturation") ||
                   map.containsKey("luminance") ) {
            HSLColor hsl = new HSLColor(Color.BLACK)

            def h = map.remove("hue")
            def s = map.remove("saturation")
            def l = map.remove("luminance")
            def a = map.remove("alpha")
            def shade = map.remove("shade")
            def tone = map.remove("tone")

            def _h = h != null ? h : hsl.hue
            def _s = s != null ? s : hsl.saturation
            def _l = l != null ? l : hsl.luminance
            def _a = a != null ? a : hsl.alpha

            hsl = new HSLColor(_h as float, _s as float, _l as float, _a as float)
            if(shade != null) hsl = new HSLColor(hsl.adjustShade(shade as float))
            if(tone != null) hsl = new HSLColor(hsl.adjustTone(tone as float))
            return hsl.getRGB()
        }
    }

    private void _setColor(String name, Color color) {
        if(!name) return
        customColors[normalize(name)] = color
    }

    private String normalize(String str) {
       str.toLowerCase().replaceAll(/[_ ]/,"")
    }

    private void initStandardColors() {
        standardColors[normalize("none")] = new Color(0,0,0,0)

        // java colors
        standardColors[normalize("black")] = Color.black
        standardColors[normalize("blue")] = Color.blue
        standardColors[normalize("cyan")] = Color.cyan
        standardColors[normalize("darkGray")] = Color.darkGray
        standardColors[normalize("gray")] = Color.gray
        standardColors[normalize("green")] = Color.green
        standardColors[normalize("lightGray")] = Color.lightGray
        standardColors[normalize("magenta")] = Color.magenta
        standardColors[normalize("orange")] = Color.orange
        standardColors[normalize("pink")] = Color.pink
        standardColors[normalize("red")] = Color.red
        standardColors[normalize("white")] = Color.white
        standardColors[normalize("yellow")] = Color.yellow
        // css colors
        standardColors[normalize("fuchsia")] = new Color( 255, 0, 255 )
        standardColors[normalize("silver")] = new Color( 192, 192, 192 )
        standardColors[normalize("olive")] = new Color( 50, 50, 0 )
        standardColors[normalize("purple")] = new Color( 50, 0, 50 )
        standardColors[normalize("maroon")] = new Color( 50, 0, 0 )
        standardColors[normalize("aqua")] = new Color( 0, 255, 255 )
        standardColors[normalize("lime")] = new Color( 0, 255, 0 )
        standardColors[normalize("teal")] = new Color( 0, 50, 50 )
        standardColors[normalize("navy")] = new Color( 0, 0, 50 )
        // html colors
        standardColors[normalize("aliceBlue")] = new Color( 240, 248, 255 )
        standardColors[normalize("antiqueWhite")] = new Color( 250, 235, 215 )
        standardColors[normalize("aquamarine")] = new Color( 127, 255, 212 )
        standardColors[normalize("azure")] = new Color( 240, 255, 255 )
        standardColors[normalize("bakersChocolate")] = new Color( 92, 51, 23 )
        standardColors[normalize("beige")] = new Color( 245, 245, 220 )
        standardColors[normalize("bisque")] = new Color( 255, 228, 196 )
        standardColors[normalize("blanchedAlmond")] = new Color( 255, 235, 205 )
        standardColors[normalize("blueViolet")] = new Color( 138, 43, 226 )
        standardColors[normalize("brass")] = new Color( 181, 166, 66 )
        standardColors[normalize("brightGold")] = new Color( 217, 217, 25 )
        standardColors[normalize("bronze")] = new Color( 140, 120, 83 )
        standardColors[normalize("brown")] = new Color( 165, 42, 42 )
        standardColors[normalize("burlyWood")] = new Color( 222, 184, 135 )
        standardColors[normalize("cadetBlue")] = new Color( 95, 158, 160 )
        standardColors[normalize("chartreuse")] = new Color( 127, 255, 0 )
        standardColors[normalize("chocolate")] = new Color( 210, 105, 30 )
        standardColors[normalize("coolCopper")] = new Color( 217, 135, 25 )
        standardColors[normalize("copper")] = new Color( 184, 115, 51 )
        standardColors[normalize("coral")] = new Color( 255, 127, 80 )
        standardColors[normalize("cornflowerBlue")] = new Color( 100, 149, 237 )
        standardColors[normalize("cornsilk")] = new Color( 255, 248, 220 )
        standardColors[normalize("crimson")] = new Color( 220, 20, 60 )
        standardColors[normalize("darkBlue")] = new Color( 0, 0, 139 )
        standardColors[normalize("darkBrown")] = new Color( 92, 64, 51 )
        standardColors[normalize("darkCyan")] = new Color( 0, 139, 139 )
        standardColors[normalize("darkGoldenRod")] = new Color( 184, 134, 11 )
        standardColors[normalize("darkGreen")] = new Color( 0, 100, 0 )
        standardColors[normalize("darkGreenCopper")] = new Color( 74, 118, 110 )
        standardColors[normalize("darkKhaki")] = new Color( 189, 183, 107 )
        standardColors[normalize("darkMagenta")] = new Color( 139, 0, 139 )
        standardColors[normalize("darkOliveGreen")] = new Color( 85, 107, 47 )
        standardColors[normalize("darkOrange")] = new Color( 255, 140, 0 )
        standardColors[normalize("darkOrchid")] = new Color( 153, 50, 204 )
        standardColors[normalize("darkPurple")] = new Color( 135, 31, 120 )
        standardColors[normalize("darkRed")] = new Color( 139, 0, 0 )
        standardColors[normalize("darkSalmon")] = new Color( 233, 150, 122 )
        standardColors[normalize("darkSeaGreen")] = new Color( 143, 188, 143 )
        standardColors[normalize("darkSlateBlue")] = new Color( 72, 61, 139 )
        standardColors[normalize("darkSlateGray")] = new Color( 47, 79, 79 )
        standardColors[normalize("darkTan")] = new Color( 151, 105, 79 )
        standardColors[normalize("darkTurquoise")] = new Color( 0, 206, 209 )
        standardColors[normalize("darkViolet")] = new Color( 148, 0, 211 )
        standardColors[normalize("darkWood")] = new Color( 133, 94, 66 )
        standardColors[normalize("deepPink")] = new Color( 255, 20, 147 )
        standardColors[normalize("deepSkyBlue")] = new Color( 0, 191, 255 )
        standardColors[normalize("dimGray")] = new Color( 105, 105, 105 )
        standardColors[normalize("dodgerBlue")] = new Color( 30, 144, 255 )
        standardColors[normalize("dustyRose")] = new Color( 133, 99, 99 )
        standardColors[normalize("fadedBrown")] = new Color( 245, 204, 176 )
        standardColors[normalize("feldspar")] = new Color( 209, 146, 117 )
        standardColors[normalize("fireBrick")] = new Color( 178, 34, 34 )
        standardColors[normalize("floralWhite")] = new Color( 255, 250, 240 )
        standardColors[normalize("forestGreen")] = new Color( 34, 139, 34 )
        standardColors[normalize("gainsboro")] = new Color( 220, 220, 220 )
        standardColors[normalize("ghostWhite")] = new Color( 248, 248, 255 )
        standardColors[normalize("gold")] = new Color( 255, 215, 0 )
        standardColors[normalize("goldenRod")] = new Color( 218, 165, 32 )
        standardColors[normalize("greenCopper")] = new Color( 82, 127, 118 )
        standardColors[normalize("greenYellow")] = new Color( 173, 255, 47 )
        standardColors[normalize("honeyDew")] = new Color( 240, 255, 240 )
        standardColors[normalize("hotPink")] = new Color( 255, 105, 180 )
        standardColors[normalize("hunterGreen")] = new Color( 33, 94, 33 )
        standardColors[normalize("indianRed")] = new Color( 205, 92, 92 )
        standardColors[normalize("indigo")] = new Color( 75, 0, 130 )
        standardColors[normalize("ivory")] = new Color( 255, 255, 240 )
        standardColors[normalize("khaki")] = new Color( 240, 230, 140 )
        standardColors[normalize("lavender")] = new Color( 230, 230, 250 )
        standardColors[normalize("lavenderBlush")] = new Color( 255, 240, 245 )
        standardColors[normalize("lawnGreen")] = new Color( 124, 252, 0 )
        standardColors[normalize("lemonChiffon")] = new Color( 255, 250, 205 )
        standardColors[normalize("lightBlue")] = new Color( 173, 216, 230 )
        standardColors[normalize("lightCoral")] = new Color( 240, 128, 128 )
        standardColors[normalize("lightCyan")] = new Color( 224, 255, 255 )
        standardColors[normalize("lightGoldenRodYellow")] = new Color( 250, 250, 210 )
        standardColors[normalize("lightGreen")] = new Color( 144, 238, 144 )
        standardColors[normalize("lightPink")] = new Color( 255, 182, 193 )
        standardColors[normalize("lightSalmon")] = new Color( 255, 160, 122 )
        standardColors[normalize("lightSeaGreen")] = new Color( 32, 178, 170 )
        standardColors[normalize("lightSkyBlue")] = new Color( 135, 206, 250 )
        standardColors[normalize("lightSlateBlue")] = new Color( 132, 112, 255 )
        standardColors[normalize("lightSlateGray")] = new Color( 119, 136, 153 )
        standardColors[normalize("lightSteelBlue")] = new Color( 176, 196, 222 )
        standardColors[normalize("lightWood")] = new Color( 233, 194, 166 )
        standardColors[normalize("lightYellow")] = new Color( 255, 255, 224 )
        standardColors[normalize("limeGreen")] = new Color( 50, 205, 50 )
        standardColors[normalize("linen")] = new Color( 250, 240, 230 )
        standardColors[normalize("mandarinOrange")] = new Color( 228, 120, 51 )
        standardColors[normalize("mediumAquaMarine")] = new Color( 102, 205, 170 )
        standardColors[normalize("mediumBlue")] = new Color( 0, 0, 205 )
        standardColors[normalize("mediumGoldenRod")] = new Color( 234, 234, 174 )
        standardColors[normalize("mediumOrchid")] = new Color( 186, 85, 211 )
        standardColors[normalize("mediumPurple")] = new Color( 147, 112, 216 )
        standardColors[normalize("mediumSeaGreen")] = new Color( 60, 179, 113 )
        standardColors[normalize("mediumSlateBlue")] = new Color( 123, 104, 238 )
        standardColors[normalize("mediumSpringGreen")] = new Color( 0, 250, 154 )
        standardColors[normalize("mediumTurquoise")] = new Color( 72, 209, 204 )
        standardColors[normalize("mediumVioletRed")] = new Color( 199, 21, 133 )
        standardColors[normalize("mediumWood")] = new Color( 166, 128, 100 )
        standardColors[normalize("midnightBlue")] = new Color( 25, 25, 112 )
        standardColors[normalize("mintCream")] = new Color( 245, 255, 250 )
        standardColors[normalize("mistyRose")] = new Color( 255, 228, 225 )
        standardColors[normalize("moccasin")] = new Color( 255, 228, 181 )
        standardColors[normalize("navajoWhite")] = new Color( 255, 222, 173 )
        standardColors[normalize("navyBlue")] = new Color( 35, 35, 142 )
        standardColors[normalize("neonBlue")] = new Color( 77, 77, 255 )
        standardColors[normalize("neonPink")] = new Color( 255, 110, 199 )
        standardColors[normalize("newMidnightBlue")] = new Color( 0, 0, 156 )
        standardColors[normalize("newTan")] = new Color( 235, 199, 158 )
        standardColors[normalize("oldGold")] = new Color( 207, 181, 59 )
        standardColors[normalize("oldLace")] = new Color( 253, 245, 230 )
        standardColors[normalize("oliveDrab")] = new Color( 107, 142, 35 )
        standardColors[normalize("orangeRed")] = new Color( 255, 69, 0 )
        standardColors[normalize("orchid")] = new Color( 218, 112, 214 )
        standardColors[normalize("paleGoldenRod")] = new Color( 238, 232, 170 )
        standardColors[normalize("paleGreen")] = new Color( 152, 251, 152 )
        standardColors[normalize("paleTurquoise")] = new Color( 175, 238, 238 )
        standardColors[normalize("paleVioletRed")] = new Color( 216, 112, 147 )
        standardColors[normalize("papayaWhip")] = new Color( 255, 239, 213 )
        standardColors[normalize("peachPuff")] = new Color( 255, 218, 185 )
        standardColors[normalize("peru")] = new Color( 205, 133, 63 )
        standardColors[normalize("plum")] = new Color( 221, 160, 221 )
        standardColors[normalize("powderBlue")] = new Color( 176, 224, 230 )
        standardColors[normalize("quartz")] = new Color( 217, 217, 243 )
        standardColors[normalize("richBlue")] = new Color( 89, 89, 171 )
        standardColors[normalize("rosyBrown")] = new Color( 188, 143, 143 )
        standardColors[normalize("royalBlue")] = new Color( 65, 105, 225 )
        standardColors[normalize("saddleBrown")] = new Color( 139, 69, 19 )
        standardColors[normalize("salmon")] = new Color( 250, 128, 114 )
        standardColors[normalize("sandyBrown")] = new Color( 244, 164, 96 )
        standardColors[normalize("scarlet")] = new Color( 140, 23, 23 )
        standardColors[normalize("seaGreen")] = new Color( 46, 139, 87 )
        standardColors[normalize("seaShell")] = new Color( 255, 245, 238 )
        standardColors[normalize("semiSweetChocolate")] = new Color( 107, 66, 38 )
        standardColors[normalize("sienna")] = new Color( 160, 82, 45 )
        standardColors[normalize("skyBlue")] = new Color( 135, 206, 235 )
        standardColors[normalize("slateBlue")] = new Color( 106, 90, 205 )
        standardColors[normalize("slateGray")] = new Color( 112, 128, 144 )
        standardColors[normalize("snow")] = new Color( 255, 250, 250 )
        standardColors[normalize("spicyPink")] = new Color( 255, 28, 174 )
        standardColors[normalize("springGreen")] = new Color( 0, 255, 127 )
        standardColors[normalize("steelBlue")] = new Color( 70, 130, 180 )
        standardColors[normalize("summerSky")] = new Color( 56, 176, 222 )
        standardColors[normalize("tan")] = new Color( 210, 180, 140 )
        standardColors[normalize("thistle")] = new Color( 216, 191, 216 )
        standardColors[normalize("tomato")] = new Color( 255, 99, 71 )
        standardColors[normalize("turquoise")] = new Color( 64, 224, 208 )
        standardColors[normalize("veryLightGrey")] = new Color( 205, 205, 205 )
        standardColors[normalize("violet")] = new Color( 238, 130, 238 )
        standardColors[normalize("violetRed")] = new Color( 208, 32, 144 )
        standardColors[normalize("wheat")] = new Color( 245, 222, 179 )
        standardColors[normalize("whiteSmoke")] = new Color( 245, 245, 245 )
        standardColors[normalize("yellowGreen")] = new Color( 154, 205, 50 )
    }
}
