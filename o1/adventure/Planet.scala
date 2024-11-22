package o1.adventure

import scala.collection.mutable.Map

class Planet(paramMagnitudes: Map[String, Double], biomeProbabilities: Map[String, Double]):
  // time in days
  val days = 0
  val world = World(biomeProbabilities)

  private val params: Map[String, Parameter] = Map(
    "temp"             -> Temperature(paramMagnitudes("temp")),
    "moisture"         -> Moisture(paramMagnitudes("moisture")),
    "pressure"         -> Pressure(paramMagnitudes("pressure")),
    "density"          -> Density(paramMagnitudes("density")),
    "N2"               -> N2(paramMagnitudes("N2")),
    "O2"               -> O2(paramMagnitudes("O2")),
    "CO2"              -> CO2(paramMagnitudes("CO2")),
    "logisticDistance" -> logisticDistance(paramMagnitudes("logisticDistance")),
    "H2O"              -> H2O(paramMagnitudes("H2O").toInt)
  )

  def adjustParams(item: Item) =
    item.paramDeltas.foreach( param =>
      params(param._1).increase(param._2)
    )

  def adjustParam(param: String, delta: Double = 0, scalar: Double = 1) =
    if delta != 0 then params(param).increase(delta)
    else params(param).multiply(scalar)

  def overrideParamMagnitude(param: String, newMagnitude: Double) = params(param).setMagnitude(newMagnitude)

  override def toString =
    params.map( (param, value) => s"$param: ${value.getMagnitude}" ).mkString("\n")