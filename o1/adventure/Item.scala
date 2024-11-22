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
case class Item(val name: String, val description: String,
                val price: Int, val isUsableOnPlanet: Boolean):
  var paramDeltas: Map[String, Double] = Map(
    "temp"     -> 0,
    "moisture" -> 0,
    "pressure" -> 0,
    "density"  -> 0,
    "N2"       -> 0,
    "O2"       -> 0,
    "CO2"      -> 0
  )

  def param(name: String): Option[Double] = paramDeltas.get(name)
  def changeParam(name: String, newValue: Double): Unit =
    paramDeltas = paramDeltas.map( (name1, value) => if name1 == name then (name1, newValue )
                                                                      else (name1, value) )

  /** Returns a short textual representation of the item (its name, that is). */
  override def toString = this.name
end Item