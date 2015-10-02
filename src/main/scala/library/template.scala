package library

import com.tristanhunt.knockoff.DefaultDiscounter._
import com.tristanhunt.knockoff._

object Template {
  def apply(blocks: Seq[Block]): String = {
    val html = <html>
      <head>
        <meta charset="utf-8" />
        <meta content="width=device-width, initial-scale=1" name="viewport" />
        <link rel="stylesheet" href="css/scalresume.css" type="text/css" media="print"/>
        <link rel="stylesheet" href="css/scalresume-grid.css" type="text/css" media="print"/>
        <script type="text/javascript" src="js/scalresume.js" ></script>

        { toXHTML(blocks) }
      </head>
    </html>

    html.toString()
  }
}