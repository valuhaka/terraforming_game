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

  private var quitCommandGiven = false              // one-way flag
  val inventory = Inventory()

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit: Boolean = this.quitCommandGiven

  /** Returns the player’s current wealth. */
  def wealth: Int = this.currentMoney

  /** Returns the player’s current energy resources . */
  def energy: Int = this.currentEnergy

  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() =
    "You rest for a while. Better get a move on, though."

  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() =
    this.quitCommandGiven = true
    ""

  /** Returns a brief description of the player’s state. */
  override def toString =
    "Current wealth: " + this.wealth + " €" +
    "\n" +
    "Current energy: " + this.wealth + " units"

end Player