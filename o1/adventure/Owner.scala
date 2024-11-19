package o1.adventure

import scala.collection.mutable.Map

/* Singleton objects to represent in-game goods. */

val Nuke = Item("nukes", "50 megatons of atomic fun. Will heat the planet.", 50)
val Capsule = Item("capsules", "Ever heard of seed bombs? This is a seed bomb, just with 700 kg of bacteria, algae, and the like.", 50)
val MicrobeTank = Item("microbe tanks", "Bacteria, viruses, and other micro-organisms to produce oxygen.", 25)

/** The trait `Owner` is a wrapper that helps with managing in-game goods.
  * It consists of a map for storing the goods and methods for adding and using them. */

trait Owner:

  /* Map: contains the items and their respective frequencies. */
  private var items = Map[Item, Int](
    Nuke -> 0,
    Capsule -> 0,
    MicrobeTank -> 0
  )

  def currentItems = items

  /* Boolean: check whether the inventory contains an item. For debugging. */
  def has(itemName: String): Boolean = this.items.keys.exists( _.name == itemName )

  /* Returns the frequency of the given item in the inventory. */
  def number(itemName: String): Int =
    this.items.keys.find( _.name == itemName ) match
      case Some(item) => items(item)
      case None => 0

  /* Add N items to the map. Return a message. */
  def add(itemName: String, countToAdd: Int): String =
    this.items = this.items.map( (item, count) => if item.name == itemName
                                        then (item, count + countToAdd)
                                        else (item, count) )
    s"You have acquired ${countToAdd} ${itemName}(s)."

  /* Remove n instances of the item from the map. Return the Item object wrapped in an Option.
  * Make sure that the frequency is always nonnegative. */
  def consume(itemName: String, countToConsume: Int): Option[Item] =
    var returnItem: Option[Item] = None
    if countToConsume <= number(itemName) then
      this.items = this.items.map( (item, count) =>
                                      if item.name == itemName
                                      then
                                        returnItem = Some(item)
                                        (item, count - countToConsume)
                                      else
                                        (item, count) )
    returnItem

  def examine(itemName: String): String =
    this.items.find((item, count) => item.name == itemName) match
      case Some(item -> count) => s"\nYou have ${count} ${item.name}(s). \n- ${item.description}"
      case None => itemName + "? There is no such item."

end Owner

/** The 'Market' class is used to purchase goods. */
class Market extends Owner:

  /* Init */
  this.add("nukes", 100)
  this.add("microbe tanks", 100)
  this.add("capsules", 20)

  /** Returns a short textual representation of the available goods and their prices. */
  override def toString = "\nContents:\n- " + currentItems.map( (item, count) => s"$count ${item}s, ${item.price} M€ each" ).mkString("\n- ")

end Market

/** The 'Inventory' class represents the player's discrete property, e.g. nuclear warheads. */
class Inventory extends Owner:

  /* Init */
  this.add("capsules", 10)

  /** Returns a short textual representation of the contents of the player's inventory. */
  override def toString = "\nContents:\n- " + currentItems.map( (item, count) => s"$count ${item}s" ).mkString("\n- ")

end Inventory
