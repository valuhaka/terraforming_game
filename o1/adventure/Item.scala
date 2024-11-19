package o1.adventure
import scala.collection.mutable.Map

/** The class `Item` represents items in a text adventure game. Each item has a name
  * and a longer description. (In later versions of the adventure game, items may
  * have other features as well.)
  *
  * N.B. It is assumed, but not enforced by this class, that items have unique names.
  * That is, no two items in a game world have the same name.
  *
  * @param name         the item’s name
  * @param description  the item’s description */
class Item(val name: String, val description: String, val price: Int):

  /** Returns a short textual representation of the item (its name, that is). */
  override def toString = this.name

end Item

class Inventory():

  /* Map: contains the items and their respective frequencies in the inventory. */
  var items = Map[Item, Int]()

  /* Boolean: does the inventory contain an item of the specified name? */
  def has(itemName: String): Boolean = this.items.keys.exists( _.name == itemName )

  /* */
  def add(item: Item, number: Int): String =
    this.items.filter{
      case Item(name, _) if item.name == name -> amount =>
    }
    this.items += item -> 1
    s"You have acquired ${item.name}."

  /*
  def drop(itemName: String): String =
    this.items.get(itemName) match
      case Some(item) =>
        items = items - itemName
        currentLocation.addItem(item)
        s"You drop the $itemName."
      case None =>
        "You don't have that!"
  */
  /*
  def examine(itemName: String): String =
    this.items.filter{
      case (Item(name: String, description: String), count: Int) if name == itemName =>
        s"You have ${count} ${name}(s).\n" + item.description
    }
   */

  /** Returns a short textual representation of the contents of the player's inventory. */
  override def toString =
    if this.items.isEmpty
      then "[Your inventory is empty.]"
      else "You have the following items:\n" // + this.items.map( s"{_._1} [{_._2}]" ).mkString("\n")

end Inventory

