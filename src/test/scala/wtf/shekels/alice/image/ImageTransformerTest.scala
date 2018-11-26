package wtf.shekels.alice.image

import org.scalatest.{FlatSpec, Matchers}
import wtf.shekels.alice.text.Alphabet

import scala.io.{Codec, Source}

class ImageTransformerTest extends FlatSpec with Matchers {

  behavior of classOf[ImageTransformer].toString

  it should "correctly convert a sample image" in {
    // Given
    val chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM,;'[]<>?:{}\\\\1234567890-=!@#$%^&*()_+"
    val alphabet = new Alphabet(chars)
    val sourcePath = getClass.getResource("/star.png").getPath

    // When
    val imageTransformer = new ImageTransformer(path = sourcePath, compressionFactor = 8, alphabet = alphabet)
    val result = imageTransformer.toString

    // Then
    val expectedResult = Source.fromURL(getClass.getResource("/star.txt"))(Codec.UTF8)
    result shouldBe expectedResult.mkString
  }

}
