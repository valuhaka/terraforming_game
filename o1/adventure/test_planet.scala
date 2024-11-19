package o1.adventure

import scala.collection.mutable.Map

val testBiomeProbs = Map(
  "groundwater" -> 0.15,
  "permafrost" -> 0.1,
  "soil" -> 0.3,
  "rock" -> 0.2,
  "coast" -> 0.15,
  "lake" -> 0.1
)

def test(): Unit =
  val testworld = World(testBiomeProbs)
  val locs = testworld.locations

  println("LOCATION COORDINATE PAIRS *********************************************************")
  var i = 1
  locs.keys.toVector.sortBy(_._2).sortBy(_._1).foreach( loc =>
    println(s"$i.\t$loc")
    i += 1
  )

  println("LOCATION COORDINATE PRODUCTS ******************************************************")
  i = 1
  locs.keys.toVector.sortBy(_._2).sortBy(_._1).foreach( loc =>
    println(s"$i.\t${math.abs(loc._1 * loc._2)}\tisWithinBoundaries: ${isWithinBoundaries(loc._1, loc._2)}\tisValidCoastLocation: ${isValidCoastLocation(loc._1, loc._2)}")
    i += 1
  )

  println("LOCATION NEIGHBORS ****************************************************************")
  i = 1
  locs.toVector.sortBy(_.head._2).sortBy(_.head._1).foreach { case ((_, _), loc) =>
    println(s"$i.\tLocation: $loc\t\tNeighbors: ${loc.allNeighbors.mkString("\t")}")
    i += 1
  }

  println("LOCATION BIOMES BEFORE ASSIGNMENT *************************************************")
  i = 1
  locs.values.foreach( loc =>
    println(s"$i.\t${loc.getBiome}")
    i += 1
  )

  println("ASSIGNING BIOMES...")
  assignBiomesToLocations(testworld.biomeProbs, locs)
  println("BIOMES ASSIGNED! LOCATIONS WITH CORRESPONDING BIOMES:")
  i = 1
  locs.values.foreach( loc =>
    loc.getBiome match
      case Some(biome) => println(s"$i.\t$loc: $biome"); i += 1
      case None => println(s"$i.\t$loc: WARNING!!! NO BIOME ASSIGNED."); i += 1
  )

