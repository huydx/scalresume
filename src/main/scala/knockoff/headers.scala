package knockoff

import com.tristanhunt.knockoff._
import scala.util.parsing.input.{ CharSequenceReader, Position, Reader }
import collection.mutable.ListBuffer

object ScalresumeDiscounter extends Discounter with TitleDiscounter

trait TitleDiscounter extends Discounter {
  override def newChunkParser: ChunkParser = new ChunkParser with TitleChunkParser
  override def blockToXHTML: Block => xml.Node = {
    val fallback: PartialFunction[Block, xml.Node] = { case x => super.blockToXHTML(x) }
  }
}

trait TitleChunkParser extends ChunkParser {
  override def chunk : Parser[ Chunk ] = {
    horizontalRule | leadingStrongTextBlock | leadingEmTextBlock | bulletItem |
      numberedItem | indentedChunk | header | blockquote | linkDefinition |
      htmlBlock | headerChunk | textBlockWithBreak | textBlock | emptyLines | emptySpace
  }

  def headerChunk: Parser[Chunk] = {
    """---(\\s+)""".r ~> """[^\n]+""".r ^^ { case(_, header) => TextChunk(header) }
  }
}

case class TitleChunk(content: String) extends Chunk {
  def appendNewBlock(list: ListBuffer[Block],
                     remaining: List[(Chunk, Seq[Span], Position)],
                     spans: Seq[Span], position: Position,
                     discounter: Discounter): Unit = discounter match {
    case td: TitleDiscounter => list += Title(content, position)
  }
}

case class Title(content: String,  position : Position) extends Block {
}

