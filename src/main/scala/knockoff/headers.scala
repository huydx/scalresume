package knockoff

import com.tristanhunt.knockoff._
import scala.util.parsing.input.Position
import collection.mutable.ListBuffer

/**
 * Some note about extends knockoff
 * - when need to append new parser rule:
 * - extends your “SpecialDiscounter”
 * - extends new “SpecialChunkParser” with parser rule which convert “your rule” to “your custom Chunk”using parser combinator ,
 * inside “your custom Chunk”, need to override appendNewBlock method which convert chunk to block
 * - inside your “SpecialDiscounter”override blockToXTML convert your block to html
 */

trait TitleDiscounter extends Discounter {
  override def newChunkParser: ChunkParser = new ChunkParser with TitleChunkParser
  override def blockToXHTML: Block ⇒ xml.Node = {
    val fallback: PartialFunction[Block, xml.Node] = { case x ⇒ super.blockToXHTML(x) }
    val toXHTML: PartialFunction[Block, xml.Node] = {
      case TitleBlock(content, pos) ⇒
        <div class="header">
          <div class="container">
            <div class="span-16 prepend-1 append-1">
              <div class="span-16 top nav">
                <div class="span-16 title">
                  <span>{ content }</span>
                </div>
              </div>
            </div>
          </div>
        </div>
    }
    toXHTML orElse fallback
  }
}

trait TitleChunkParser extends ChunkParser {
  override def chunk: Parser[Chunk] = {
    horizontalRule | leadingStrongTextBlock | leadingEmTextBlock | bulletItem |
      numberedItem | indentedChunk | header | blockquote | linkDefinition |
      htmlBlock | headerChunk | textBlockWithBreak | textBlock | emptyLines | emptySpace
  }

  def headerChunk: Parser[Chunk] = {
    """~~~ (\s)+""".r ~ """[^\n]+""".r ^^ {
      case symbol ~ header ⇒ TitleChunk(header)
    }
  }
}

case class TitleChunk(content: String) extends Chunk {
  def appendNewBlock(list: ListBuffer[Block],
    remaining: List[(Chunk, Seq[Span], Position)],
    spans: Seq[Span], position: Position,
    discounter: Discounter): Unit = discounter match {
    case td: TitleDiscounter ⇒ list += TitleBlock(content, position)
  }
}

case class TitleBlock(content: String, position: Position) extends Block

