package o1.adventure

import scala.collection.mutable.Map


sealed trait Biome(val name: String):
  protected var description: String
  override def toString = s"${this.name.toUpperCase}: ${this.getDescription}"

  def probabilityCoefficient = biomeProbCoeffs.baseValue

  def setDescription(newDescription: String) =
    this.description = newDescription

  def getDescription = this.description

end Biome

/**
  * Biomes that appear dry on the ground level.
  */
sealed trait Dry extends Biome:
  protected var desription: String = defaultDescriptions.dry

  def isFrost = false
  def isFavorable = false

  override def probabilityCoefficient = biomeProbCoeffs.dry
  override def getDescription = defaultDescriptions.dry

end Dry

/**
  * In groundwater biome weak bonuses are readily available
  *
  * @param description The description of the biome.
  */
class Groundwater(protected var description: String = "") extends Dry, Biome("dry, groundwater"):

  override def isFavorable = true
  override def probabilityCoefficient = biomeProbCoeffs.groundwater

  override def toString = s"${super.getDescription}\n${this.getDescription}"

  override def getDescription =
    if this.description == "" then
      defaultDescriptions.groundwater
    else
      this.description

end Groundwater

/**
  * Permafrost biome does not provide bonuses unless avg(T) > 280 K has held for at least 100 years.
  *
  * @param description The description of the biome.
  */
class Permafrost(protected var description: String = "") extends Biome("dry, permafrost"), Dry:

  def frozen = true

  override def probabilityCoefficient =
    if this.frozen then super.probabilityCoefficient
    else biomeProbCoeffs.meltedPermafrost  // if frozen provide a good bonus

  override def toString = s"${super.getDescription}\n${this.getDescription}"

  override def getDescription =
    if this.description == "" then
      if this.frozen then defaultDescriptions.permafrost
      else defaultDescriptions.moltenPermafrost
    else
      this.description

end Permafrost


/**
  * In soil biome bonuses are similar to lake or ocean biome as far as moisture is optimal
  *
  * @param description The description of the biome.
  */
class Soil(protected var description: String = "") extends Biome("dry, soil"), Dry:

  override def isFavorable = true
  override def probabilityCoefficient = biomeProbCoeffs.soil
  override def toString = s"${super.getDescription}\n${this.getDescription}"

  override def getDescription =
    if this.description == "" then
      defaultDescriptions.soil
    else
      this.description

end Soil


/**
  * In rock biome no bonuses
  *
  * @param description The description of the biome.
  */
class Rock(protected var description: String = "") extends Biome("dry, rock"), Dry:

  override def toString = s"${super.getDescription}\n${this.getDescription}"

  override def getDescription =
    if this.description == "" then
      defaultDescriptions.rock
    else
      this.description

end Rock


sealed trait WaterBiome extends Biome:
  protected var isLiquid = false
  protected var desription: String = defaultDescriptions.dry

  val meltingThreshold: Int
  val maxCapsuleCapacity: Option[Int] // allow infinite capacity

  override def probabilityCoefficient = biomeProbCoeffs.baseValue

  def melt() = this.isLiquid = false
  def freeze() = this.isLiquid = true

  def ignoreMoisture = true
  def frozen = !isLiquid

end WaterBiome


/**
  * Increased cell growth probabilities; moisture parameter is irrelevant
  *
  * @param description The description of the biome.
  */
class Coast(protected var description: String) extends Biome("coast"), WaterBiome:
  // melts after 120 months
  val meltingThreshold = 120
  // can hold infinitely many capsules
  val maxCapsuleCapacity = None

  override def toString = s"${super.getDescription}\n${this.getDescription}"

  // big risk; big reward
  override def probabilityCoefficient =
    if this.frozen then
      biomeProbCoeffs.baseValue
    else
      biomeProbCoeffs.coast

  override def getDescription =
    if this.description == "" then
      if this.isLiquid then defaultDescriptions.moltenCoast
      else defaultDescriptions.frostCoast
    else
      this.description

end Coast


/**
  * Increased cell growth probabilities; moisture parameter is irrelevant
  *
  * @param description The description of the biome.
  */
class Lake(protected var description: String) extends Biome("lake"), WaterBiome:
  //melts after 12 months
  val meltingThreshold = 12
  //can only hold 5 capsules; allows more to be deployed, but they won't be of extra benefit
  val maxCapsuleCapacity = Some(5)

  override def toString = s"${super.getDescription}\n${this.getDescription}"

  // good growth bonus
  override def probabilityCoefficient =
    if this.frozen then
      biomeProbCoeffs.baseValue
    else
      biomeProbCoeffs.lake

  override def getDescription =
    if this.description == "" then
      if this.isLiquid then defaultDescriptions.moltenLake
      else defaultDescriptions.frostLake
    else
      this.description

end Lake
