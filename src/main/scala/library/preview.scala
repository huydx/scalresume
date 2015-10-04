package library

import java.io.{ FileInputStream, OutputStream }

import unfiltered.filter.Plan
import unfiltered.jetty.Server
import unfiltered.request._
import unfiltered.response._

object Preview {
  def apply(outDir: String): Server = {
    val basedir = s"$outDir/webroot"
    val plan: Plan = unfiltered.filter.Planify {
      case GET(Path(Seg("css" :: name :: Nil))) ⇒ CssContent ~> responseStreamer(s"$basedir/css/$name")
      case GET(Path(Seg("js" :: name :: Nil)))  ⇒ JsContent ~> responseStreamer(s"$basedir/js/$name")
      case GET(Path(Seg(name :: Nil)))          ⇒ ResponseString(name)
    }

    val http = unfiltered.jetty.Server.anylocal
    http.plan(plan).resources(new java.net.URL(s"file://$basedir"))
  }

  def responseStreamer(file: String) =
    new ResponseStreamer {
      def stream(os: OutputStream) {
        val is = new FileInputStream(file)
        try {
          val buf = new Array[Byte](1024)
          Iterator.continually(is.read(buf)).takeWhile(_ != -1)
            .foreach(os.write(buf, 0, _))
        } finally {
          is.close()
        }
      }
    }
}