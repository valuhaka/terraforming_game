package o1.adventure
import scala.collection.mutable.Map

/** The class `Item` represents items in a the game. Each item has a name,
  * a longer description, and a price.
  *
  * N.B. It is assumed, but not enforced by this class, that items have unique names.
  * That is, no two items in a game world have the same name.
  *
  * @param name         the item’s name
  * @param description  the item’s description */
case class Item(val name: String, val description: String, val price: Int):
  val paramDeltas: Map[String, Double] = Map(
    "temp"     -> 0,
    "moisture" -> 0,
    "pressure" -> 0,
    "density"  -> 0,
    "N2"       -> 0,
    "O2"       -> 0,
    "CO2"      -> 0
  )
  /** Returns a short textual representation of the item (its name, that is). */
  override def toString = this.name
end Item