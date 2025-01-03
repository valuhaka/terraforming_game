package o1.adventure

import scala.collection.mutable.Map

/** A World represents the explorable area of a [[Planet]].
  *
  * @param biomeProbabilities A Map of the names of all the available biomes in the game with their
  *                           respective probabilities in the planet this World belongs to.
  */
class World(val biomeProbabilities: Map[String, Double]):

  val neighbors = Map[String, Location]()

  val locations = Map[(Int, Int), Location]()
  for { x <- -3 to 3; y <- -3 to 3 } do
    if isWithinBoundaries(x, y) then    // check whether point is inside the diamond-shaped map
      locations += (x, y) -> Location((x, y), None, this, "")

  // Assign neighbors to each of the locations.
  assignNeighbors(this.locations)

  // Assign a biome to each of the locations.
  assignBiomesToLocations(this)

  def printAllLocs() =
    this.locations.foreach { case ((x, y), loc) =>
      val biome = loc.getBiome.getOrElse(Rock(this))
      println(s"At ($x, $y) is $loc with a ${biome.name}. \nBiome prob: ${biome.winningProbability}\nBiome description: ${biome}.")
      biome match
        case b: Coast => println(s"Is coast at $loc frozen: ${b.frozen}")
        case b: Lake => println(s"Is lake at $loc frozen: ${b.frozen}")
        case b: Permafrost => println(s"Is permafrost at $loc frozen: ${b.frozen}")
        case b => println("Not a melting biome")
      println()
    }

