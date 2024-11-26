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

val referenceValues: Map[String, Vector[Double]] = Map(
  "temp"     -> Vector(288.0, 298.0),
  "moisture" -> Vector(0.3,   0.7),
  "pressure" -> Vector(0.5,  2.0),
  "N2"       -> Vector(0.70,  0.86),
  "O2"       -> Vector(0.15,  0.27),
  "CO2"      -> Vector(0.005,  0.02),
  "toxic gases" -> Vector(0, 0.01),
  ""         -> Vector()
)

val testParamMagnitudes: Map[String, Double] = Map(
  "temp"     -> 293,
  "moisture" -> 0.5,
  "pressure" -> 1,
  "N2"       -> 0.78,
  "O2"       -> 0.21,
  "CO2"      -> 0.01,
  "toxic gases" -> 0,
  "logisticDistance" -> 10,
  "H2O" -> 100,
  ""         -> 0
)

//val Earth = new Planet(testParamMagnitudes, testBiomeProbs)
  /*referenceValues.map { case (param, data) =>
    param -> (data.head + data.last) / 2
  }*/

  //( (str1, Vector(d1, d2)) => (str1, d1+d2/2) )

val directionStep: Map[String, (Int, Int)] = Map(
  "north" -> (0, 1),
  "east"  -> (1, 0),
  "south" -> (0, -1),
  "west"  -> (-1, 0)
)

object biomeProbCoeffs:
  val baseValue = 0.95
  val biome = 1.0
  val dry = 0.9
  val groundwater = 1.05
  val meltedPermafrost = 1.02
  val soil = 1.02
  val rock = 0.9
  val coast = 1.15
  val lake = 1.10

object defaultDescriptions:
  val biome = "Hmm... you have entered a new location. How intriguing is the nature of another planet. \n" +
    "Exploring around would be a good idea. \n"

  val dry = "A dead land void of visible water reaches almost the horizon. Is it really a good idea \n" +
    "to start biocolonization here? \n"
  val groundwater = "Oh! Wait! It's almost as if you could see a glint of wet soil deep inside of the bottomless \n" +
    "dry cracks on the surface. I wonder whether there's water somewhere. "
  val moltenPermafrost = "The soil here is so moist it's almost like a swamp! I can see a few craters, this \n" +
    "must have been a permafrost plain. "
  val permafrost = "What is more, the ground feels suspiciously hard beneath your feets. It's almost like \n" +
    "it's frozen. This resembles Siberia, where the frost continues meters beneath the surface. "
  val soil = "Well, it seems like with getting all the parameters close enough to those of our Earth, settling \n" +
    "here wouldn't be the worst idea. "
  val rock = "Whoops! Caution, your 100 M€ space rover almost fell into one of those meters-wide drought cracks. \n" +
    "Maybe better find an easier place. "

  val moltenWaterBiome = "A gentle sigh of waves reaches your ears. A soohing rhythm against the stillness of the landscape; \n" +
    "with your eyes you begin to search for the source of the sound. \n"
  val frostCoast = "You surmount a hill and your entire expedition crew is in awe—you've arrived at the frontier of a vast \n" +
    "ocean of ice extending far into the horizon. This'd be the perfect spot if only the ocean wasn't frozen. "
  val moltenCoast = "Where before a desert of ice reached beyond the horizon, you now see a shimmering expanse of reddish, \n" +
    "rippling water. Faint traces of melting ice hug the shoreline and you feel excitement growing in your chest. "
  val frostLake = "A cold, windy and harsh savannah opens under your eyes. The ground here is flat and seems to be reflecting \n" +
    "the reddish clouds of the sky. Looks like a lake of ice. "
  val moltenLake = "A considerable body of water in its liquid form lays at the bottom of a valley. The view is calming, although \n" +
    "the reddish hue adds an element of danger tto it. This surely would be a sweet start for cell culturing!"