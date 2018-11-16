package wtf.shekels.alice

import java.io.{File, FileOutputStream, PrintStream}

import scopt.OptionParser
import wtf.shekels.alice.image.ImageTransformer
import wtf.shekels.alice.text.Alphabet


object Main {

  /**
    * Command line config
    */
  case class Config(invert: Boolean = true,
                    lightnessModifier: Int = 0,
                    compressionFactor: Int = 0,
                    file: String = null,
                    out: String = null)

  /**
    * Command line option parser
    */
  val parser: OptionParser[Config] = new scopt.OptionParser[Config]("scala-ascii-art") {
    head("scala-ascii-art")

    opt[Boolean]('i', "invert").valueName("<bool>").action((x, c) => {
      c.copy(invert = x)
    })

    opt[Int]('c', "compression").required().valueName("<compression factor>").action((x, c) => {
      c.copy(compressionFactor = x)
    })

    opt[Int]('l', "lightness").valueName("<lightness modifier>").action((x, c) => {
      c.copy(lightnessModifier = x)
    })

    opt[String]('w', "write").valueName("<output file>").action((x, c) => {
      c.copy(out=x)
    })

    help("help").text("prints this usage text")

    arg[String]("<file>").required().action((x, c) => {
      c.copy(file = x)
    }).validate(x => {
      if (new File(x).exists()) {
        success
      } else {
        failure("File does not exist.")
      }
    })

  }

  def main(args: Array[String]): Unit = {
    parser.parse(args, Config()) match {
      case Some(config) =>
        val chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM,;'[]<>?:{}\\\\1234567890-=!@#$%^&*()_+"
        val alphabet = new Alphabet(chars, "Roboto Mono")
        val ir = new ImageTransformer(
          config.file,
          config.compressionFactor,
          alphabet,
          config.lightnessModifier,
          config.invert
        )

        val string = ir.toString

        println(string)

        // If output file specified
        if (config.out != null) {
          val file = new File(config.out)
          // If file doesn't already exist
          if (!file.exists()) {
            // Create and try writing
            file.createNewFile()
            try {
              new PrintStream(new FileOutputStream(file)).print(string)
            } catch {
              case e: Exception => e.printStackTrace()
            }
          } else {
            println("File already exists. Remove or delete, or run again with a different output file.")
          }
        }
      case None =>
    }
  }
}