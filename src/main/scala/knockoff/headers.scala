package knockoff

import com.tristanhunt.knockoff._
import scala.util.parsing.input.{ Position }
import collection.mutable.ListBuffer

trait TitleDiscounter extends Discounter {
  override def newChunkParser: ChunkParser = new ChunkParser with TitleChunkParser
  override def blockToXHTML: Block => xml.Node = {
    val fallback: PartialFunction[Block, xml.Node] = { case x => super.blockToXHTML(x) }
    val toXHTML: PartialFunction[Block, xml.Node] = {
      case TitleBlock(content, pos) =>
        <p class="header">{ content }</p>
    }
    toXHTML orElse fallback
  }
}

trait TitleChunkParser extends ChunkParser {
  override def chunk : Parser[ Chunk ] = {
    horizontalRule | leadingStrongTextBlock | leadingEmTextBlock | bulletItem |
      numberedItem | indentedChunk | header | blockquote | linkDefinition |
      htmlBlock | headerChunk | textBlockWithBreak | textBlock | emptyLines | emptySpace
  }

  def headerChunk: Parser[Chunk] = {
    """~~~ """.r ~ """[^\n]+""".r ^^ {
      case symbol ~ header => {
        TextChunk(header)
      }
    }
  }
}

case class TitleChunk(content: String) extends Chunk {
  def appendNewBlock(list: ListBuffer[Block],
                     remaining: List[(Chunk, Seq[Span], Position)],
                     spans: Seq[Span], position: Position,
                     discounter: Discounter): Unit = discounter match {
    case td: TitleDiscounter => list += TitleBlock(content, position)
  }
}

case class TitleBlock(content: String,  position : Position) extends Block

