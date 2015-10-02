package library

import java.io._

import com.tristanhunt.knockoff.DefaultDiscounter._

import scala.annotation.tailrec

object Produce {

  def apply(inputFile: File, outputDir: File) = {
    //convert to markdown
    val md = readFromFile(inputFile)
    val blocks= knockoff(md)
    val output = Template(blocks)

    recursiveCopy(new File(Shared.resources.getPath), outputDir)
    val printer = new PrintWriter(new File(s"${outputDir.getAbsolutePath}/webroot/index.html"))
    printer.print(output)
    printer.close()
  }

  private[this] def recursiveCopy(from: File, toDir: File): Unit = {
    from match {
      case dir if dir.isDirectory =>
        val newToDir = s"${toDir.getAbsolutePath}/${dir.getName}"
        new File(newToDir).mkdir()
        dir.listFiles().foreach { f => recursiveCopy(f, new File(newToDir)) }
      case file if file.isFile =>
        val inStream = new FileInputStream(from)
        val newFileLocation = s"$toDir/${from.getName}"
        val outStream = new FileOutputStream(newFileLocation)
        using(List(inStream, outStream)) {
          copy(inStream, outStream)
        }
      case _ => throw new RuntimeException(s"not found from dir at $from")
    }
  }

  private[this] def copy(in: InputStream, out: OutputStream): Unit = {
    @tailrec def doCopy(): Unit = {
      val byte = in.read()
      if (byte != -1) {
        out.write(byte)
        doCopy()
      }
    }
    doCopy()
    out.flush()
  }

  private[this] def using[A <: {def close()}](resources: Seq[A])(func: => Unit) = {
    try {
      func
    } catch {
      case ex: Exception => ex.printStackTrace()
    } finally {
      resources.foreach(r => r.close())
    }
  }

  private[this] def readFromFile(f: File): String = {
    val path = f.getAbsolutePath
    scala.io.Source.fromFile(path, "utf-8").mkString //mix between scala.io and java.io is seems not good
  }

}
