package wtf.shekels.alice.text

import scala.collection.immutable

/**
  * Collection of our selected characters + some functionality
  */
class Alphabet(val alphabet: String, val font: String = "Roboto") {

  /**
    * List of character objects for each character in `alphabet`
    */
  val characters: immutable.IndexedSeq[AsciiCharacter] = alphabet.map(c => new AsciiCharacter(c.toString, font))

  /**
    * Writes each character to a file
    * Can be used in development to force an alphabet to be spit to images
    */
  def writeAll(): Unit = {
    characters.foreach(c => c.writeImage())
  }

  /**
    * Generates a map that pairs each character to its lightness value, used later to determine what character to use
    */
  def getMap: Map[Double, String] = {
    characters.map(c => c.calcLightness() -> c.char).toMap
  }

}
