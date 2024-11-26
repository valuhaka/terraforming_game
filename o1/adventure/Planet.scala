package o1.adventure

import scala.collection.mutable.Map

class Planet(paramMagnitudes: Map[String, Double], biomeProbabilities: Map[String, Double]):

  // time in days
  val days = 0
  val world = World(biomeProbabilities)

  var isHabited: Boolean = false // This must be changed to true to win the game.

  val params: Map[String, Parameter] = Map(
    "temp"             -> Temperature(paramMagnitudes("temp")),
    "moisture"         -> Moisture(paramMagnitudes("moisture")),
    "pressure"         -> Pressure(paramMagnitudes("pressure")),
    "N2"               -> N2(paramMagnitudes("N2")),
    "O2"               -> O2(paramMagnitudes("O2")),
    "CO2"              -> CO2(paramMagnitudes("CO2")),
    "toxic gases"      -> ToxicGases(paramMagnitudes("toxic gases")),
    "logisticDistance" -> logisticDistance(paramMagnitudes("logisticDistance")),
    "H2O"              -> H2O(paramMagnitudes("H2O").toInt)
  )

  def isLivable =
    this.params.forall { case (name, param) =>
      param.isValid
    }

  def updatePlanetBiomes() =
    if this.params("temp") >= Temperature(0) then
      this.world.locations.foreach { case ((_, _), loc) =>
        loc.getBiome match
          case Some(biome: Coast) => biome.melt()
          case Some(biome: Lake) => biome.melt()
          case Some(biome: Permafrost) => biome.melt()
          case _ => true
      }
    else
      this.world.locations.foreach { case ((_, _), loc) =>
        loc.getBiome match
          case Some(biome: Coast) => biome.freeze()
          case Some(biome: Lake) => biome.freeze()
          case Some(biome: Permafrost) => biome.freeze()
          case _ => true
      }

  def adjustParams(item: Item) =
    item.paramDeltas.foreach( param =>
      this.params(param._1).increase(param._2)
    )

  def adjustParam(param: String, delta: Double = 0, scalar: Double = 1) =
    if delta != 0 then params(param).increase(delta)
    else params(param).multiply(scalar)

  def overrideParamMagnitude(param: String, newMagnitude: Double) = params(param).setMagnitude(newMagnitude)

  override def toString =
    params.map( (param, value) => s"$param: ${value.getMagnitude}" ).mkString("\n")