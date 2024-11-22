package o1.adventure

import scala.collection.mutable.Map

/** A `Player` object represents a player character controlled by the real-life user
  * of the program.
  *
  * A player object’s state is mutable: the player’s location and possessions can change,
  * for instance.
  *
  * @param startingMoney  the player’s initial wealth
  * @param startingEnergy  the player’s initial energy resources */
class Player(startingMoney: Int, startingEnergy: Int):

  private var currentMoney = startingMoney        // gatherer: changes in relation to the previous money
  private var currentEnergy = startingEnergy        // gatherer: changes in relation to the previous energy

  var onPlanet = false
  private var quitCommandGiven = false              // one-way flag

  val inventory = Inventory()
  val rover = Rover(World(testBiomeProbs))
  val planet = Planet(testParamMagnitudes, testBiomeProbs)
  val market = Market()

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit: Boolean = this.quitCommandGiven

  /** Returns the player’s current wealth. */
  def money: Int = this.currentMoney

  /** Returns the player’s current energy resources . */
  def energy: Int = this.currentEnergy

  /** Used to translate money into items. */
  def buy(itemName: String, count: Int): String =
    this.market.consume(itemName, count) match
      case Some(item: Item) => if this.money < item.price then
                                 "You cannot afford that!"
                                 else
                                   this.currentMoney -= item.price
                                   this.inventory.add(item.name, count)
      case None => s"The market hasn't got enough $itemName(s)!"

  /** Use an item. Remove it from the inventory and deploy it to the planet. */
  def use(itemName: String, count: Int): String =
    this.inventory.consume(itemName, count) match
      case Some(item) =>
        this.planet.adjustParams(item)
        s"$count ${item.name}(s) used."
      case None => "Not enough " + itemName + "s."

  /** Causes the player to skip a turn (this has no substantial effect in game terms). */
  def skip(): String =
    "You skip a turn."

  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit(): String =
    this.quitCommandGiven = true
    ""

  def land(): String =
    if !this.onPlanet then
      this.onPlanet = true
      "You have reached the planet's surface.\nYour rover's fusion reactors start up."
    else
      "You cannot land if you're already on the planet!"

  def takeOff(): String =
    if this.onPlanet then
      this.onPlanet = false
      "You pack up your rover, board the shuttle, and soon you are back in orbit."
    else
      "You cannot take off if you're already in space!"

  /** Returns a brief description of the player’s state. */
  override def toString =
    var string = ("Money: " + this.money + " M€" +
                  "\n" +
                  "Energy: " + this.money + " units")
    if !this.onPlanet then
      this.planet.toString +
               "\n\n" + string +
               "\n\n [H] Help    [I] Inventory   [S] Store   [L] Land    "
    else
      this.rover.lookAround +
               "\n\n" + string +
               "\n\n [H] Help    [I] Inventory   [M] Move   [R] Return to space   "

end Player