package o1.adventure

import scala.collection.mutable.Map


val referenceValues: Map[String, Vector[Double]] = Map(
  "temp"     -> Vector(288.0, 298.0),
  "moisture" -> Vector(0.3,   0.7),
  "pressure" -> Vector(0.33,  1.67),
  "density"  -> Vector(1.1,   1.3),
  "N2"       -> Vector(0.78,  0.78),
  "O2"       -> Vector(0.21,  0.21),
  "CO2"      -> Vector(0.01,  0.01),
  ""         -> Vector()
)

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
  val biome = "Hmm... you have entered a new location. How intriguing is the nature on another planet. Exploring around would be a good idea. "

  val dry = "A dead land void of visible water reaches almost the horizon. Would it really be a good idea to start biocolonization here? "
  val groundwater = "Oh! Wait! It's almost as if you could see a glint of wet soil deep inside of the bottomless dry cracks on the surface. I wonder whether there's water somewhere. "
  val meltedPermafrost = "The soil here is so moist it's almost like a swamp! I can see a few craters, this must have been a permafrost plain. "
  val permafrost = "What is more, the ground feels suspiciously hard beneath your feets. It's almost like it's frozen—and that the frost continues meters beneath the surface. "
  val soil = "Well, it seems like with getting all the parameters close enough to those of our Earth, settling here wouldn't be the worst idea. "
  val rock = "Whoops! Caution, your 100 M€ space rover almost fell into one of those meters-wide drought cracks. Maybe better find an easier place. "

  val frostCoast = "You surmount a hill and your entire expedition crew is in awe—you've arrived at the frontier of a vast ocean of ice extending well into the horizon. "
  val meltedCoast = ""
  val frostLake = ""
  val meltedLake = ""