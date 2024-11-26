package o1.adventure

class Rover(val world: World, private var coords: (Int, Int) = (0, 0)):

  private var hasBase = false

  def x = this.currentLocation.x
  def y = this.currentLocation.y

  def settle() =
    if !this.hasBase then
      this.currentLocation.settle()
      this.hasBase = true
      s"You have settled to location at ${this.currentLocation}."
    else
      s"You already have a base. You can't settle twice!"

  def pack() =
    if this.hasBase then
      if this.currentLocation.isSettled then
        this.currentLocation.unsettle()
        this.hasBase = false
        s"You have packed your stuff."
      else
        s"You can't pack your stuff because you haven't settled here."
    else
      s"You can't pack your stuff because haven't settled here."

  def load() =
    // if the rover is NOT at origo, proceed to examine whether it has settled
    if this.currentCoords != (0, 0) then
      // if the expedition crew does NOT have a base, then load rover to the origo
      if !this.hasBase then
        this.coords = (0, 0)
      // if rover HAS settled, find the location with the base (to be implemented...)
      // else...
    // if the rover is at origo, do nothing

    // print the surroundings
    s"${this.lookAround}"

  private def move(dir: String): String =
    this.currentLocation.neighbor(dir) match
      case None =>
        currentLocation.getBiome match
          case Some(_: Coast) => s"Hmmm... The sea seems to continue far to the horizon $dir. It would be too dangerous to go there."
          case Some(_) => s"There are impassable mountains to \"$dir\" of your location. \nTry another direction."
          case None => s"There are impassable mountains to \"$dir\" of your location. \nTry another direction."

      case Some(validDir) =>
        // store location before moving for return print
        val precedentLoc = this.currentCoords

        // write in the memory of the Location that the rover is no longer there
        this.currentLocation.unoccupy()
        this.coords += directionStep(dir)

        // write in the memory of the new currentLocation that the rover has arrived
        this.currentLocation.occupy()

        // return the coords change for debugging purposes
        s"Moved from $precedentLoc to ${this.currentCoords}."

        // return a string for print
        s"${this.lookAround}"

  def goto(direction: String): String =
    // call move method of this rover or return error print
    direction match
      case "north" => this.move(direction)
      case "east"  => this.move(direction)
      case "south" => this.move(direction)
      case "west"  => this.move(direction)
      case inValid => s"Could not move to invalid direction \"$inValid\"."

  def lookAround: String =
    this.currentLocation.getBiome match
      case None => throw new UnsupportedOperationException(s"No biome has been assigned to Location ${this.currentLocation}.")
      case Some(biome) => biome.toString

  def legalDirections: String =
    s"Possible exits: ${this.currentLocation.legalDirections.mkString(", ")}"

  def currentCoords = this.coords
  def currentLocation = this.world.locations(this.currentCoords)
