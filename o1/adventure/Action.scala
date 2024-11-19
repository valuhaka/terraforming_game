package o1.adventure

/** The class `Action` represents actions that a player may take in a text adventure game.
  * `Action` objects are constructed on the basis of textual commands and are, in effect,
  * parsers for such commands. An action object is immutable after creation.
  * @param input  a textual in-game command such as “go east” or “rest” */
class Action(input: String):

  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ' ).toLowerCase
  private val target      = commandText.drop(verb.length + 1).takeWhile( _ != ' ' ).toLowerCase
  private val number      = commandText.drop(verb.length + target.length + 2).trim

  /** Causes the given player to take the action represented by this object, assuming
    * that the command was understood. Returns a description of what happened as a result
    * of the action (such as “You go west.”). The description is returned in an `Option`
    * wrapper; if the command was not recognized, `None` is returned. */
  def execute(actor: Player): Option[String] =
    this.verb match
      case "buy"       => Some(actor.buy(this.target, this.number.toInt))
      case "use"       => Some(actor.use(this.target, this.number.toInt))
      case "examine"   => Some(actor.inventory.examine(target))
      case "help"      => Some(actor.inventory.toString)
      case "H"         => Some(actor.inventory.toString)
      case "inventory" => Some(actor.inventory.toString)
      case "I"         => Some(actor.inventory.toString)
      case "skip"      => Some(actor.skip())
      case "quit"      => Some(actor.quit())
      case other       => None

  /** Returns a textual description of the action object, for debugging purposes. */
  override def toString = s"$verb (target: $target, number: $number)"

end Action

