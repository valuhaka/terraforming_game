package o1.adventure

import scala.annotation.targetName
import scala.util.Random
import scala.collection.mutable.Map

extension [A: Numeric](pair1: (A, A))
  // define addition for numeric paris
  @targetName("add")
  def +(pair2: (A, A)): (A, A) =
    val num = summon[Numeric[A]]
    (num.plus(pair1._1, pair2._1), num.plus(pair1._2, pair2._2))

  // subtraction for numeric pairs
  @targetName("subtract")
  def -(pair2: (A, A)): (A, A) =
    val num = summon[Numeric[A]]
    (num.minus(pair1._1, pair2._1), num.minus(pair1._2, pair2._2))

  @targetName("equals")
  def ==(pair2: (A, A)): Boolean = pair1._1 == pair2._1 && pair1._2 == pair2._2

  @targetName("unequalTo")
  def !=(pair2: (A, A)): Boolean = pair1._1 != pair2._1 || pair1._2 != pair2._2



/** This method takes the name of a [[Biome]] as a String and returns the corresponding biome with
  * the default description.
  *
  * @param biomeName The name of the biome
  * @return [[Biome]]
  */
def createBiome(biomeName: String, world: World): Biome = biomeName match {
  case "groundwater" => new Groundwater("", world)
  case "permafrost"  => new Permafrost("", world)
  case "soil"        => new Soil("", world)
  case "rock"        => new Rock("", world)
  case "coast"       => new Coast("", world)
  case "lake"        => new Lake("", world)
  case unknownBiome  => throw new IllegalArgumentException(s"Unknown biome: $unknownBiome")
}

// if you don't get this, draw each point x, y in [-3, 3] on a coordinate plain and write the product of x and y
def isValidCoastLocation(x: Int, y: Int): Boolean =
  math.abs(x * y) == 2 || (x == 0 && y * y == 9) || (x * x == 9 && y == 0)

// check whether a given coordinate pair is within the world boundaries
def isWithinBoundaries(x: Int, y: Int): Boolean =
  math.abs(x * y) < 3

/** This method assigns neighbors to all the locations. In this (version of the) game the world map is fixed, and so
  * are the neighbors of each location.
  *
  * @param locations All the locations of the world.
  * @return
  */
def assignNeighbors(locations: Map[(Int, Int), Location]) =
  // neighbors for row 3
  locations(0, 3).setNeighbors("south")
  // neighbors for row 2
  locations(-1, 2).setNeighbors("south")
  locations(0, 2).setNeighbors("north", "east", "south")
  locations(1, 2).setNeighbors("west", "south")
  // neighbors for row 1
  locations(-2, 1).setNeighbors("east")
  locations(-1, 1).setNeighbors("north", "east", "south", "west")
  locations(0, 1).setNeighbors("north", "south", "west")
  locations(1, 1).setNeighbors("north", "south")
  locations(2, 1).setNeighbors("south")
  // neighbors for row 0
  locations(-3, 0).setNeighbors("east")
  locations(-2, 0).setNeighbors("east", "south")
  locations(-1, 0).setNeighbors("north", "south", "west")
  locations(0, 0).setNeighbors("north", "east", "south")
  locations(1, 0).setNeighbors("north", "east", "west")
  locations(2, 0).setNeighbors("north", "east", "south", "west")
  locations(3, 0).setNeighbors("west")
  // neighbors for row -1
  locations(-2, -1).setNeighbors("north", "east")
  locations(-1, -1).setNeighbors("north", "east", "south", "west")
  locations(0, -1).setNeighbors("north", "east", "west")
  locations(1, -1).setNeighbors("east", "south", "west")
  locations(2, -1).setNeighbors("north", "west")
  // neighbors for row -2
  locations(-1, -2).setNeighbors("north", "east")
  locations(0, -2).setNeighbors("east", "south", "west")
  locations(1, -2).setNeighbors("north", "west")
  //neighbors for row -3
  locations(0, -3).setNeighbors("north")

/** This method is very complex. Below you can find method assignBiomesToLocationsDEBUGGING where
  * several print-statements are included in the code to aid the understanding of this method.
  *
  * This method takes to arguments: the locations of the game world and the probabilities of different biomes.
  *
  * When an instance of World is constructed, its locations' biomes are left undefined (None) to make the
  * code less hefty and to distribute the complex tasks to different parts of the program.
  *
  * After constructing the locations, the instance of World calls this method to assign each location
  * one of the available biomes.
  *
  * The number of locations is fixed to 25 (although this method can scale to Worlds of different sizes)
  * and of each biome this method produces a number of copies relative to the biome's probability in
  * biomeProbs: Map[String, Double].
  *
  * This method also takes care of placing coast biomes only at tiles that are at the border of given
  * location Map.
  *
  * For example, if this method was given a 0.08 probability of a Lake, it would assign exactly
  * 0.08 * 25 = 2 lake biomes to the location set. If this method was given a 0.16 probability of
  * a Coast, it would assign 0.16 * 25 = 4 coast biomes on locations *at the borders* of the given world.
  *
  * The method will take care of rounding errors by dropping off random biomes. Coasts will, however, not
  * be affected by this.
  *
  * This method could have been divided into several tinier methods, but the creators of the game saw that
  * as unnecessary for the implementation of the game logic; we valued more keeping the list of methods simple.
  *
  * @param world The world where the biomes are going to be assigned
  **/
def assignBiomesToLocations(world: World): Unit =
  val biomeProbs = world.biomeProbabilities
  val locations = world.locations
  
  // Count how many biomes are needed
  val totalLocations = locations.size
  val biomeCounts = biomeProbs.map { case (biome, prob) =>
    biome -> math.ceil(prob * totalLocations).toInt
  }

  // Separate valid and invalid coastal location tiles based on isValidCoastLocation condition
  val (coastCandidates, nonCoastLocations) = locations.partition { case ((x, y), _) =>
    isValidCoastLocation(x, y)
  }

  // Set coast biomes if there were any to set (and throw an error in case of erraneous coastCount)
  val coastCount = biomeCounts.getOrElse("coast", 0)
  if coastCount > coastCandidates.size then
    throw new IllegalArgumentException("Too many Coasts for valid locations")

  // Mix coastLocations
  val coastLocationsMixed = Random.shuffle(coastCandidates.keys.toSeq)

  // Set biome to coast for as many valid locations as needed
  coastLocationsMixed.take(coastCount).foreach { coords =>
    val location = locations(coords)
    location.setBiome(createBiome("coast", world))
  }

  // Put the remaining biomes and their respective quantities in a Map
  val remainingBiomeCounts = biomeCounts.filterNot( _._1 == "coast")

  // Add the locations invalid to coasts to the leftover (unused) coast locations and store in a variable
  val otherLocations = nonCoastLocations.keys.toSeq.diff(coastLocationsMixed.take(coastCount))
                       ++ coastLocationsMixed.drop(coastCount)

  // Make a vector with the length of the original vector minus coast locations and add all the
  // remaining biomes here with the correct quantities indicated by biomeProbs (biomeProbabilities)
  val remainingBiomes = remainingBiomeCounts.flatMap { case (biome, count) =>
    List.fill(count)(biome)
  }

  // Mix biomes, zip to otherLocations (consists of invalid coast locations and unused coast locations)
  // For each element (biomeName: String, coords: (Int, Int)) of the array find the location object
  // from the given locations and set its biome to biomeName
  Random.shuffle(remainingBiomes.toList).zip(otherLocations).foreach { case (biomeName, coords) =>
    val location = locations(coords)
    location.setBiome(createBiome(biomeName, world))
  }

/**
  * Altered method assignBiomesToLocationsDEBUGGING for debugging purposes.
  * For a more detailed explanation, see [[assignBiomesToLocations]].
  * @param biomeProbs
  * @param locations
  */
def assignBiomesToLocationsDEBUGGING(biomeProbs: Map[String, Double], locations: Map[(Int, Int), Location], world: World): Unit =
  // count how many biomes are needed
  val totalLocations = locations.size
  val biomeCounts = biomeProbs.map { case (biome, prob) =>
    biome -> math.round(prob * totalLocations).toInt
  }

  var i = 1
  println("BIOMECOUNTS *********************************************************")
  biomeCounts.foreach( count =>
    println(s"$i.\t${count._1}:\t\t\t${count._2}")
    i += 1
  )

  // Erota validit ja ei-validit Coast-sijainnit
  println("VALID COAST LOCATIONS FROM ASSIGNBIOMESTOLOCATIONS() ****************")
  i = 1
  // separate valid and invalid coastal location tiles based on isValidCoastLocation condition
  val (coastCandidates, nonCoastLocations) = locations.partition { case ((x, y), _) =>
    println(s"$i.\tIs ($x, $y) a valid coast location:\t${isValidCoastLocation(x, y)}")
    i += 1
    isValidCoastLocation(x, y)
  }
  println("VARIABLE COASTCANDIDATES ********************************************")
  println(s"coastCandidates (size: ${coastCandidates.size}): ${coastCandidates.values.mkString(", ")}")

  println("VARIABLE NONCOASTLOCATIONS ******************************************")
  println(s"nonCoastLocations (size: ${nonCoastLocations.size}): ${nonCoastLocations.values.mkString(", ")}")

  // set coast biomes if there were any to set (and throw an error in case of erraneous coastCount)
  val coastCount = biomeCounts.getOrElse("coast", 0)
  println("VARIABLE COASTCOUNT *************************************************")
  println(s"coastCount: $coastCount")
  if coastCount > coastCandidates.size then
    throw new IllegalArgumentException("Too many Coasts for valid locations")

  println("COASTLOCATIONS ******************************************************")
  i = 1
  // mix coastLocations
  val coastLocationsMixed = Random.shuffle(coastCandidates.keys.toSeq)
  println(coastLocationsMixed.take(coastCount).mkString(", "))
  // set biome to coast for as many valid locations as needed
  coastLocationsMixed.take(coastCount).foreach { coords =>
    val location = locations(coords)
    print(s"$i.\tOperating location: $location\t\tBiome before: ${location.getBiome}\t")
    location.setBiome(createBiome("coast", world))
    println(s"Biome after: ${location.getBiome.getOrElse("None")}")
    i += 1
  }

  // Jäljelle jääneiden biomeiden jakaminen
  println("REMAININGBIOMECOUNTS ************************************************")
  val remainingBiomeCounts = biomeCounts.filterNot( _._1 == "coast" )
  i = 1
  remainingBiomeCounts.foreach( count =>
    println(s"$i.\t${count._1}:\t\t\t${count._2}")
    i += 1
  )
  val otherLocations = nonCoastLocations.keys.toSeq.diff(coastLocationsMixed.take(coastCount))
                       ++ coastLocationsMixed.drop(coastCount)
  println("OTHERLOCATIONS ******************************************************")
  i = 1
  otherLocations.foreach( loc =>
    println(s"$i.\t(${loc._1}, ${loc._2})")
    i += 1
  )
  println("REMAININGBIOMES *****************************************************")
  i = 1
  val remainingBiomes = remainingBiomeCounts.flatMap { case (biome, count) =>
    println(s"$i.\tAdded list: ${List.fill(count)(biome)}")
    i += 1
    List.fill(count)(biome)
  }
  println("ENTIRE REMAININGBIOMES **********************************************")
  i = 1
  remainingBiomes.foreach( x =>
    println(s"$i.\t${x}")
  )

  // Sekoita ja jaa biomit
  println("LAST LOT ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||")
  println(s"size of remainingBiomes:\t\t${remainingBiomes.size}")
  println(s"size of otherLocations:\t\t${otherLocations.size}")
  Random.shuffle(remainingBiomes.toList).zip(otherLocations).foreach { case (biomeName, coords) =>
    val location = locations(coords)
    location.setBiome(createBiome(biomeName, world))
  }
