package o1.adventure

import o1.adventure.old.Item

import scala.collection.mutable.Map

/** A Location class represents one single Location in the game [[World]].
  *
  * @param coords A pair of integers which represents the coordinates of this Location
  * @param biome An Option[Biome] which represents the [[Biome]] this location holds; defining this can be
  *              omitted at the construction of a Location.
  * @param world The World this Location belongs to.
  * @param description The description of this Location.
  */
class Location(val coords: (Int, Int), private var biome: Option[Biome], val world: World, val description: String):

  val x: Int = coords._1
  val y: Int = coords._2

  private val neighbors = Map[String, Location]()
  private val deployedItems = Map[Item, Int]()
  private var settled = false

  override def toString = s"($x, $y)"

  def deployItem(item: Item) =
    if deployedItems.contains(item) then
      deployedItems(item) += 1
    else
      deployedItems += (item -> 1)

  def undeployItem(item: Item) =
    if deployedItems.contains(item) && deployedItems(item) > 0 then
      deployedItems(item) -= 1
      if deployedItems(item) == 0 then
        deployedItems.remove(item)
    else
      throw new IllegalArgumentException(s"Could not undeploy $item from $this because there was no such item deployed.")

  def settle() = this.settled = true
  def unsettle() = this.settled = false

  // has the player settled here
  def isSettled = this.settled

  // give access to the location's biome
  def getBiome: Option[Biome] = biome

  /** Returns the area that can be reached from this area by moving in the given direction. The result
    * is returned in an `Option`; `None` is returned if there is no exit in the given direction. */
  def neighbor(direction: String) = this.neighbors(direction)

  // get all neighbors as a vector
  def allNeighbors = this.neighbors.values.toVector

  // make it possible to set a biome to the location ONLY IF there was no biome defined
  def setBiome(newBiome: Biome): Boolean =
    this.biome match
      case None => this.biome = Some(newBiome); true
      case Some(assignedBiome) => false

  /** Adds exits from this area to the given areas. Calling this method is equivalent to calling
    * the `setNeighbor` method on each of the given direction–area pairs.
    * @param exits  an array of directions (String) of undefined length
    * @see [[setNeighbor]] */
  def setNeighbors(exits: String*) =
    this.neighbors ++= exits.map( direction =>
      // take the location at the indicated direction
      val neighbor = this.world.locations((this.x + directionStep(direction)._1,
                                           this.y + directionStep(direction)._2))
      direction -> neighbor
    )
