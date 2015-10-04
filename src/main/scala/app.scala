import java.io.File

import library.{Produce, Preview}

object Scalresume {
  def main(args: Array[String]): Unit = {
    System.exit(run(args))
  }

  private[this] def run(args: Array[String]): Int = {
    try {
      args match {
        case Array(SingleFile(input), Dir(output)) => {
          Produce(input, output)
        }
        case Array(Dir(output)) => preview(output)
        case Array() => println(
          """
            | Usage:  scalresume [SRC] [DST] to generate
            |         scalresume [DST] to preview
          """.stripMargin)
        case _ => println("intput file does not exist")
      }
      0
    } catch {
      case ex: Exception => {
        ex.printStackTrace()
        1
      }
    }
  }

  private[this] def preview(dir: File) = {
    Preview(dir.getAbsolutePath).run {
      server => unfiltered.util.Browser.open(
        "http://127.0.0.1:%d/".format(server.portBindings.head.port)
      )
      println("\nPreviewing `%s`. Press CTRL+C to stop.".format(dir))
    }
    0
  }

  object Dir {
    def unapply(path: String) = {
      val file = new File(path)
      if (file.exists && file.isDirectory) {
        Some(file)
      } else {
        file.mkdir()
        Some(file)
      }
    }
  }

  object SingleFile {
    def unapply(path: String) = {
      val file = new File(path)
      if (file.exists && file.isFile) {
        Some(file)
      } else None
    }
  }
}
