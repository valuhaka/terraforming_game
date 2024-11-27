package o1.adventure

/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of “hard-coded” information that pertains to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Game:

  /** the name of the game */
  val title = "Terraforming"

  /** The character that the player controls in the game. */
  val player = Player(1000, 1000)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 40


  /** Determines if the player has completed his objects: terraformed a planet and planted life on it. */
  def isComplete = this.player.planet.isHabited

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage =
  s"""The year is 2124 AD. You are the captain of SS Väinämö, a terraforming ship in orbit around the exoplanet Tao-42.
Your task: life in outer space.

In this game, you [ use ] and [ buy ] items that help you terraform a planet by adjusting its parametres. For instance,
a nuclear warhead (or nuke) is going to heat up the planet, while a shipment of microbial life will convert CO2 to O2.

Once you are comfortable in your planet being sufficiently close to the Earth's conditions, you may deploy
a bio-capsule of macroscopic life. If the life takes root, you win.

At any time, you can [ land ] from the safety of your spaceship, [ move ] around the planet's surface, and [ return ]
to the ship, where you can trade and use items. The planet's surface is generated procedurally but remains stable within one
game, so you can admire the frozen tundras turn to lush lakesides.

For more help, see [ help ]."""

  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether the player has completed their quest. */
  def goodbyeMessage =
    if this.isComplete then
      "You win."
    else if this.turnCount == this.timeLimit then
      "Game over!"
    else  // game over due to player quitting
      "Quitter!"

  /** Returns a message that is to be displayed when the player asks for help. */
  def helpMessage =
    """This is a game of terraforming.
Your goal is to alter the conditions of an exoplanet by using items, which you may buy from the market.
After you planet is suitable for life, you can deploy capsules of macroscopic life on it, hoping they take root.

You have the following commands:
[ I ] for [ inventory ]
[ L ] to [land] on the planet
[ R ] to [ return ] to orbit
[ M ] to [ move ] around on the planet
[ S ] to open the [ store ] menu, that is, the markets.
[ quit ] to leave the game

You can [ buy ], [ use ], and [ examine ] items.
Note that you must specify the item with the commands. [Buy] and [use] can also take numbers.
You may write: [ examine ] [ buy bio-capsule 2 ] or [ use nuke 10 ].
[ Use nuke ] is equivalent to [ use nuke 1 ]."""

  /** Plays a turn by executing the given in-game command, such as “go west”. Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String): String =
    val action = Action(command)
    val outcomeReport = action.execute(this)
    if outcomeReport.isDefined then
      this.turnCount += 1
    if this.player.onPlanet then
      this.player.rover.legalDirections
        + "\n\n"
        + outcomeReport.getOrElse(s"""Unknown command: "$command".""")
    else
      outcomeReport.getOrElse(s"""Unknown command: "$command".""")

  override def toString =
    this.player.toString

end Game

