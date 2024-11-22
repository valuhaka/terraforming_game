package o1.adventure

import scala.collection.mutable.Map

/** The trait `Owner` is a wrapper that helps with managing in-game goods.
  * It consists of a map for storing the goods and methods for adding and using them. */

trait Owner:

  /* Singleton objects to represent in-game goods. */

  private val Nuke = Item("nukes", "50 megatons of atomic fun. Will heat the planet.",
                  price = 50, isUsableOnPlanet = false)
  Nuke.changeParam("temp", 0.5)

  private val Capsule = Item("capsules", "Ever heard of seed bombs? This is a seed bomb, just with 700 kg of bacteria, algae, and the like.",
                  price = 50, isUsableOnPlanet = true)

  private val MicrobeTank = Item("microbe tanks", "Bacteria, viruses, and other micro-organisms to produce oxygen.",
                  price = 25, isUsableOnPlanet = false)
  MicrobeTank.changeParam("O2", 0.5)
  MicrobeTank.changeParam("CO2", -0.5)

  /* Map: contains the items and their respective frequencies. */
  private var items = Map[Item, Int](
    Nuke -> 0,
    Capsule -> 0,
    MicrobeTank -> 0
  )

  def currentItems = items

  /* Check whether the inventory contains N or more of an item. */
  def has(itemName: String, count: Int): Boolean =
    this.items.find((item, n) => item.name == itemName) match
      case Some(item -> n) => if n >= count then true else false
      case None => false

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
  override def toString = "The Earth's goods are available to you, but not for free.\nThe following items are currently available:" +
    "\n\n♦ " +
    currentItems.map( (item, count) => s"$count ${item}s, ${item.price} M€ each" ).mkString("\n♦ ") +
    "\n\nHint: to buy 12 nukes, write [ buy nukes 12 ]."

end Market

/** The 'Inventory' class represents the player's discrete property, e.g. nuclear warheads. */
class Inventory extends Owner:

  /* Init */
  this.add("capsules", 10)

  /** Returns a short textual representation of the contents of the player's inventory. */
  override def toString = "The ship's loading bays always bustle with action: crates coming and going, visitors arriving and leaving. \nJust now, the ship is carrying."
                + "\n\n♦ " + currentItems.map( (item, count) => s"$count ${item}s" ).mkString("\n♦ ") +
                "\n\nHint: If you need more, just check the [S]tore."

end Inventory
