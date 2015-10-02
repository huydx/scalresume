package knockoff

import com.tristanhunt.knockoff._

object Knock {
  lazy val discounter = ScalresumeDiscounter
  def apply(md: String): Seq[Block] = {
    discounter.knockoff(md)
  }
}