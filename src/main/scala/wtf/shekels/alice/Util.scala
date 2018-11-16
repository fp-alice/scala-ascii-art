package wtf.shekels.alice

import com.sksamuel.scrimage.RGBColor

object Util {

  /**
    * Converts an rgb color to a lightness value
    * These are magic numbers I pulled off StackOverflow, pretty sure this is the only part of this app I don't understand
    * But, I know how to use it!
    */
  def rgbToLightness(color: RGBColor): Double = {
    (0.2126 * color.red) + (0.7152 * color.green) + (0.0722 * color.blue)
  }
}
