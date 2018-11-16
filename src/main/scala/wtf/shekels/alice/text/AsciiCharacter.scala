package wtf.shekels.alice.text

import java.awt.image.BufferedImage
import java.awt.{Color, Font, Graphics}
import java.io.File

import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.filter.GrayscaleFilter
import javax.imageio.ImageIO
import wtf.shekels.alice.Util

/**
  * Wrapper class for character that provides some extra functionality for lightness and reading/writing images
  */
class AsciiCharacter(val char: String, val font: String) {

  /**
    * Parent directory that contains the character images
    */
  val dir: File = new File("characters")

  /**
    * The File this character belongs to
    */
  val file: File = new File(s"characters/$char.png")

  /**
    * Write character to image & check if dir exists
    */
  def writeImage(): Unit = {
    if (!dir.exists()) { // If dir doesn't exist make it
      dir.mkdir()
    } else if (!dir.isDirectory) { // If dir is a file warn the user
      println("File named `characters` exists, will not create directory, move or rename this file")
      System.exit(0)
    }

    // Else run like normal

    // Create new grayscale image to write our character to
    val image: BufferedImage = new BufferedImage(45, 70, BufferedImage.TYPE_BYTE_GRAY)

    // Get graphics context for the image so we can do stuff to it
    val graphics: Graphics = image.getGraphics

    // Set a white background
    graphics.setColor(Color.WHITE)
    graphics.fillRect(0, 0, 45, 75)

    // Write our character in the middle of the image in black
    graphics.setColor(Color.BLACK)
    graphics.setFont(new Font(font, Font.PLAIN, 75))
    graphics.drawString(char, 0, 60)

    // Make sure parent dir exists
    val parent: File = new File("characters")
    if (!parent.exists()) parent.mkdir()

    // Save the image to the file
    ImageIO.write(image, "png", file)
  }

  /**
    * Calculates the mean of the lightness values in the image
    * Checks if files & directories exist
    */
  def calcLightness(): Double = {
    if (file.exists()) {
      val pixels = Image.fromFile(file).filter(GrayscaleFilter).pixels
      val values = pixels.map(p => Util.rgbToLightness(p.toColor))
      values.sum / values.length
    } else {
      writeImage()
      calcLightness()
    }
  }

}
