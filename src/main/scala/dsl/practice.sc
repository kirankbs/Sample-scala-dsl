import scala.util.matching.Regex

"[A-Z]+".r ~ Regex ("[0-9]+".r, handleWhiteSpace = false)