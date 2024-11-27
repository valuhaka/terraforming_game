package o1.adventure

import scala.collection.mutable.Map
import scala.util.Random

/** A `Player` object represents a player character controlled by the real-life user
  * of the program.
  *
  * A player object’s state is mutable: the player’s location and possessions can change,
  * for instance.
  *
  * @param startingMoney  the player’s initial wealth
  * @param startingEnergy  the player’s initial energy resources */
class Player(startingMoney: Int, startingEnergy: Int):

  private var currentMoney = startingMoney          // gatherer: changes in relation to the previous money
  private var currentEnergy = startingEnergy        // gatherer: changes in relation to the previous energy

  var onPlanet = false
  private var quitCommandGiven = false              // one-way flag

  val inventory = Inventory()
  val rover = Rover(World(testBiomeProbs))
  val planet = Planet(tutorialPlanetMagnitudes, testBiomeProbs)
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
      case Some(item: Item) => if this.money < item.price * count then
                                 "You cannot afford that!"
                                 else
                                   this.currentMoney -= item.price * count
                                   this.inventory.add(item.name, count)
      case None => s"The market hasn't got '$count ${itemName}s'!"

  /** Use an item. Remove it from the inventory and deploy it to the planet. */
  def use(itemName: String, count: Int): String =
    this.inventory.consume(itemName, count) match
      case Some(item) =>
        for _ <- (1 to count) do this.planet.adjustParams(item)
        if count == 1 then s"One $itemName used." else s"$count ${itemName}s used."
      case None => "Not enough " + itemName + "s."

  //* Use the capsule. May win the game. */
  def useCapsule: String =
    if !this.onPlanet then
      "You may not use bio-capsules if you have not landed!"
    else if this.planet.isLivable then
      this.use("bio-capsule", 1) match
        case str if str.startsWith("Not enough") => "Not enough bio-capsules."
        case str =>
          this.rover.currentLocation.getBiome match
            case Some(biome) =>
              if Random.nextDouble() <= biome.winningProbability then
                this.planet.habit()
                "Hooray! The bio-capsule has succesfully been deployed."
              else
                "Uh oh! Looks like your bio-capsule died. Find a better location or deploy another bio-capsule."
            case None => "Error: no biome here."

    else
      "You may not use a capsule before the planet is sufficiently terraformed!"


  /** Causes the player to skip a turn (this has no substantial effect in game terms). */
  def skip(): String =
    "You skip a turn."

  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit(): String =
    this.quitCommandGiven = true
    ""

  /** Return commentary on the industrial society. */
  def tedTalk: String = "The Industrial Revolution and its consequences have been a disaster for the human race. They have greatly increased the life-expectancy of those of us who live in 'advanced' countries, but they have destabilized society, have made life unfulfilling, have subjected human beings to indignities, have led to widespread psychological suffering (in the Third World to physical suffering as well) and have inflicted severe damage on the natural world. The continued development of technology will worsen the situation. It will certainly subject human beings to greater indignities and inflict greater damage on the natural world, it will probably lead to greater social disruption and psychological suffering, and it may lead to increased physical suffering even in 'advanced' countries. The industrial-technological system may survive or it may break down. If it survives, it MAY eventually achieve a low level of physical and psychological suffering, but only after passing through a long and very painful period of adjustment and only at the cost of permanently reducing human beings and many other living organisms to engineered products and mere cogs in the social machine. Furthermore, if the system survives, the consequences will be inevitable: There is no way of reforming or modifying the system so as to prevent it from depriving people of dignity and autonomy. If the system breaks down the consequences will still be very painful. But the bigger the system grows the more disastrous the results of its breakdown will be, so if it is to break down it had best break down sooner rather than later."

  def land(): String =
    if !this.onPlanet then
      this.onPlanet = true
      "You have reached the planet's surface.\nYour rover's fusion reactors start up.\n" +
      s"${this.rover.load()}"
    else
      "You cannot land if you're already on the planet!\n"

  def takeOff(): String =
    if this.onPlanet then
      this.onPlanet = false
      "You pack up your rover, board the shuttle, and soon you are back in orbit."
    else
      "You cannot take off if you are already in space!"

  /** Returns a brief description of the player’s state. */
  override def toString =
    var string = ("Money: " + this.money + " M€" +
                  "\n" +
                  "Energy: " + this.energy + " units")
    if !this.onPlanet then
      this.planet.toString +
               "\n\n" + string +
               "\n\n [H] Help    [I] Inventory   [S] Store   [L] Land    "
    else
      this.rover.lookAround +
               "\n\n" + string +
               "\n\n [H] Help    [I] Inventory   [M] Move   [R] Return to space   "

end Player