package wtf.shekels.alice.image

import java.io.File
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.filter.{GrayscaleFilter, InvertFilter}
import wtf.shekels.alice.Util
import wtf.shekels.alice.text.Alphabet

/**
  * Transforms an image file into ascii art
  * @param path String path to image
  * @param compressionFactor Integer for how scaled down the output should be (10 x 10 img w/ factor of 2 comes out 5x5)
  * @param alphabet Alphabet object of chosen characters with calculated lightness values
  * @param lightnessModifier Integer used to offset the lightness value when image is read. Adding a negative modifier removes most _'s.
  * @param invert Whether or not the image should be inverted, default true
  */
class ImageTransformer(val path: String,
                       val compressionFactor: Int,
                       val alphabet: Alphabet,
                       val lightnessModifier: Int = 0,
                       val invert: Boolean = true) {

  val file: File = new File(path)
  val map: Map[Double, String] = alphabet.getMap
  val lowest: Double = map.keys.reduceLeft((l, r) => if (l < r) l else r)

  /**
    * Read image in and parse into usable format
    * This has got to be horrifically inefficient, but in my testing it's been fast enough for me.
    */
  def readImage(): List[List[List[Double]]] = {
    // Apply filters
    val grayImage = Image.fromFile(file).filter(GrayscaleFilter)
    // Invert image if specified, otherwise use the normal grayscale
    val image: Image = if (invert) grayImage.filter(InvertFilter) else grayImage
    // Get lightness values and offset with our modifier if we chose to use one
    val pixelLightness: List[Double] = image.pixels.map(p => Util.rgbToLightness(p.toColor) + lightnessModifier).toList
    // Group into rows
    val rows: List[List[Double]] = pixelLightness.grouped(image.width).toList
    // Cut the rows into slices the size of compressionFactor
    val groupedRows: List[List[List[Double]]] = rows.map(_.grouped(compressionFactor).toList)
    // Transpose and rearrange so all blocks are in order
    val blocks: List[List[List[List[Double]]]] = groupedRows.transpose.map(_.grouped(compressionFactor).toList).transpose
    // Compress according to compressionFactor
    val compressedBlocks = blocks.map(lines => {
      val coll = lines.grouped(image.width / compressionFactor).toList
      coll.map(row => row.map(block => {
        val values = block.flatten
        values.sum / values.length
      }))
    })
    compressedBlocks
  }

  override def toString: String = {
    // Small function to get the closest character for the block's lightness value
    val getClosest = (n: Double, coll: List[Double]) => coll.minBy(v => Math.abs(v - n))

    // Gets our character or potentially spaces. Characters are duplicated width-wise because text has a vertically biased aspect ratio.
    val getChar = (n: Double) => {
      if (n >= lowest) {
        map.get(getClosest(n, map.keys.toList)) match {
          case Some(x) => x + x
          case None => "  "
        }
      } else {
        "  "
      }
    }

    readImage().map(row => {
      row.flatMap(block => block.map(v => getChar(v))).mkString
    }).mkString("\n")
  }

  /**
    * Print the image
    */
  def print(): Unit = {
    println(this.toString)
  }
}