package o1.adventure

import scala.annotation.targetName


sealed trait Parameter(val name: String) extends Ordered[Parameter]:

  protected var magnitude: Double

  override def toString: String = this.name
  def compare(that: Parameter): Int = this.magnitude.compare(that.magnitude)

  @targetName("add")
  def +(that: Parameter): Double = this.magnitude + that.magnitude
  @targetName("subtract")
  def -(that: Parameter): Double = this.magnitude - that.magnitude
  @targetName("equals")
  def ==(that: Parameter): Boolean = this.magnitude == that.magnitude
  @targetName("unequalTo")
  def !=(that: Parameter): Boolean = this.magnitude != that.magnitude

  def diff(that: Parameter): Double = this.magnitude - that.magnitude

  def isValid: Boolean =
    if this.name != "" then    // only for parameters relevant to bacteria
      this.magnitude > 0.95 * referenceValues(this.name).head &&  // check whether within a 5 % error margin
      this.magnitude < 1.05 * referenceValues(this.name).last     // of reference magnitudes
    else
      false

  // make it possible to adjust parameters
  def increase(delta: Double) = this.magnitude += delta
  def multiply(scalar: Double) = this.magnitude *= scalar
  def setMagnitude(newMagnitude: Double) = this.magnitude = newMagnitude

  // give access to parameter value
  def getMagnitude: Double = this.magnitude

// here magnitude is a constant
class logisticDistance(val dist: Double) extends Parameter(""):

  protected var magnitude: Double = dist

  override def toString = "logisticDistance"
  override def isValid = true
  override def increase(delta: Double) =
    throw new UnsupportedOperationException("Failed to change the magnitude of logisticDistance, because logisticDistance is a constant.")
  override def multiply(scalar: Double) =
    throw new UnsupportedOperationException("Failed to change the magnitude of logisticDistance, because logisticDistance is a constant.")

// magnitude is a constant here as well
class H2O(val amount: Int) extends Parameter(""):

  protected var magnitude = this.amount.toDouble
  override def toString = "H2O"
  override def isValid = true

  override def increase(delta: Double) =
    throw new UnsupportedOperationException("Failed to change the amount (magnitude) of H20, because the amount is a constant.")
  override def multiply(scalar: Double) =
    throw new UnsupportedOperationException("Failed to change the amount (magnitude) of H2O, because the amount is a constant.")

// to increase: nukes
// to decrease: seed white dust to atmosphere
class Temperature(protected var magnitude: Double) extends Parameter("temp")

// to increase: redirect an icy comet & increase temp > 0
// to decrease: deploy chemical agents to absorb water & decrease temp < 0
class Moisture(protected var magnitude: Double) extends Parameter("moisture")

// to increase: import gas from nearby gas cloud
// to decrease: use electromagnetic launchers to eject atmospheric gases into space
class Pressure(protected var magnitude: Double) extends Parameter("pressure")


sealed trait Gas(molecule: String, percent: Double) extends Parameter:
  def concentration: Double = this.percent

// to increase: split water into hydrogen and oxygen with catalysts
// to decrease: burn oxygen or react it with nitrogen
class O2(protected var magnitude: Double) extends Gas("O2", magnitude), Parameter("O2")

// to increase: import coal-rich comets & burn coal
// to decrease: sequester co2 into solid carbonates
class CO2(protected var magnitude: Double) extends Gas("CO2", magnitude), Parameter("CO2")

// to increase: import nitrogen-rich comets
// to decrease: vent nitrogen into space with targeted plasma jets
class N2(protected var magnitude: Double) extends Gas("N2", magnitude), Parameter("N2")


// to decrease: seed the atmosphere with catalysts and chemical agents
class ToxicGases(protected var magnitude: Double)
  extends Gas("toxic gases", magnitude), Parameter("toxic gases")