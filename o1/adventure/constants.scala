package o1.adventure

import scala.collection.mutable.Map


val tutorialPlanetMagnitudes = Map(
  "temp" -> 250.0,
  "N2"   -> 50.0,
  "O2"   -> 4.0,
  "CO2"  -> 46.0,
  "logisticDistance" -> 10.0
)

val testBiomeProbs = Map(
  "groundwater" -> 0.15,
  "permafrost" -> 0.1,
  "soil" -> 0.3,
  "rock" -> 0.2,
  "coast" -> 0.15,
  "lake" -> 0.1
)

val referenceValues: Map[String, Vector[Double]] = Map(
  "temp"     -> Vector(288.0, 298.0),
  "N2"       -> Vector(0.70,  0.86),
  "O2"       -> Vector(0.15,  0.27),
  "CO2"      -> Vector(0.005,  0.02),
)

val EarthMagnitudes: Map[String, Double] = Map(
  "temp"     -> 293,
  "N2"       -> 0.78,
  "O2"       -> 0.21,
  "CO2"      -> 0.01,
  "logisticDistance" -> 0
)


val directionStep: Map[String, (Int, Int)] = Map(
  "north" -> (0, 1),
  "east"  -> (1, 0),
  "south" -> (0, -1),
  "west"  -> (-1, 0)
)

object biomeWinningProbabilities:
  val baseValue = 0.05
  val dry = 0.05
  val groundwater = 0.5
  val moltenPermafrost = 0.7 // P(frostPermafrost) = P(rock)
  val soil = 0.4
  val rock = 0.05
  val coast = 1              // P(frostCoast) = P(rock)
  val lake = 0.8             // P(frostLake) = P(rock)


object defaultDescriptions:
  val biome = "Hmm... you have entered a new location. How intriguing is the nature of another planet. \n" +
    "Exploring around would be a good idea. \n"

  val dry = "A dead land void of visible water reaches almost the horizon.\n"
  val groundwater = "Oh! Wait! It's almost as if you could see a glint of wet soil deep inside of the \n" +
    "cracks on the surface. Maybe there's water somewhere. \n"
  val moltenPermafrost = "The soil here is so moist it's almost like a swamp! This must have been a permafrost plain.\n"
  val permafrost = "What is more, the ground feels suspiciously hard beneath your feets. Reminds me of Siberia.\n"
  val soil = "Well, the ground looks moist. This could be a good cell-culturing spot.\n"
  val rock = "Whoops! You see kilometers of rocks. You have never seen anything drier than this place. \n"

  val moltenWaterBiome = "You hear a soohing rhythm against the stillness of the landscape.\n"
  val frostCoast = "You surmount a hill and you're in awe—you see the frontier of a vast ocean of ice. \n" +
    "This'd be the perfect spot if only the ocean wasn't frozen. \n"
  val moltenCoast = "Where before a desert of ice reached beyond the horizon, you now see a shimmering expanse of reddish, \n" +
    "rippling water. Faint traces of melting ice hug the shoreline.\n"
  val frostLake = "A cold, windy savannah opens under your eyes. The ground here is flat and reflects " +
    "the reddish sky. Looks like a lake of ice.\n"
  val moltenLake = "A body of liquid water lays at the bottom of a valley. The view is calming, although \n" +
    "the reddish hue adds an element of danger to it.\n"