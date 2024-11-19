package o1.adventure.ui

import o1.adventure.*
import scala.io.StdIn.*

/** The singleton object `AdventureTextUI` represents a fully text-based version of the
  * Adventure game application. The object serves as an entry point for the game, and
  * it can be run to start up a user interface that operates in the text console.
  * @see [[AdventureGUI]] */
object AdventureTextUI extends App:

  private val game = Game()
  private val player = game.player
  this.run()

  /** Runs the game. First, a welcome message is printed, then the player gets the chance to
    * play any number of turns until the game is over, and finally a goodbye message is printed. */
  private def run() =
    println(this.game.welcomeMessage)
    while !this.game.isOver do
      this.printStatus()
      this.playTurn()
    println("\n" + this.game.goodbyeMessage)


  /** Prints out a description of the current game state and available commands. */
  private def printStatus() =
    // println(planet)
    println(player)
    println("\n\n\n    [H] Help    [I] Inventory    ")

  /** Requests a command from the player, plays a game turn accordingly, and prints out a
    * report of what happened.  */
  private def playTurn() =
    println()
    val command = readLine("Command: ")
    val turnReport = this.game.playTurn(command)
    if turnReport.nonEmpty then
      println(turnReport)

end AdventureTextUI

