package o1.adventure

import o1.adventure.old.Item

import scala.annotation.targetName
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

  def x: Int = coords._1
  def y: Int = coords._2

  private val neighbors = Map[String, Location]()
  private val deployedItems = Map[String, Set[Item]]()
  private var settled = false
  private var isOccupied = false

  override def toString = s"($x,$y)"

  def deployItem(item: Item) =
    this.biome match
      case None => throw new UnsupportedOperationException(s"Could not deploy $item to biome None.")
      case Some(someBiome) =>
        // check if there is already a copy of the item deployed at the location
        if deployedItems.contains(item.name) then
          // if the item is a capsule and the present biome is a lake, only 5 capsules can fit
          if item.name == "capsule" && someBiome.name == "lake" then
            // max number of capsules already deployed
            if deployedItems(item.name).size > 4 then
              false
            // there is room for more capsules
            else
              deployedItems(item.name) += item
              true
          // if it's not a capsule at a lake
          else
            //if item.name == "capsule" then
            deployedItems(item.name) += item
            true
        // if no copies of this item were previously deployed
        else
          deployedItems += (item.name -> Set(item))
          true

  def undeployItem(item: Item) =
    if deployedItems(item.name).nonEmpty && deployedItems.contains(item.name) then
      deployedItems(item.name) -= item
      if deployedItems(item.name).isEmpty then
        deployedItems.remove(item.name)
    else
      throw new IllegalArgumentException(s"Could not undeploy $item from $this because there was no such item deployed.")

  // in case we want to implement a settling feature; the rover could always teleport to their base and from within the base
  // could launch nukes (but not in other parts of this World). Only one base can exist at a time. If player re-lands on the World
  // from the spaceship, he lands directly on the base, instead of the origo (0, 0) point.
  def settle() = this.settled = true
  def unsettle() = this.settled = false

  // keep record of whether this Location is occupied by the rover or not
  def occupy() = this.isOccupied = true
  def unoccupy() = this.isOccupied = false

  // has the player settled here
  def isSettled = this.settled

  // give access to the location's biome
  def getBiome: Option[Biome] = biome

  /** Returns the area that can be reached from this area by moving in the given direction. The result
    * is returned in an `Option`; `None` is returned if there is no exit in the given direction. */
  def neighbor(direction: String) = this.neighbors.get(direction)

  // get all neighbors as a vector
  def allNeighbors = this.neighbors.values.toVector

  // get all legal directions
  def legalDirections = this.neighbors.keys.toVector

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
