package o1.adventure


// our intention originally was to change the description at certain locations according to whether they have molten or not (which
// depends on the planet's temperature) but we could not make our code work and we can't understand why.
// The code now just returns the descriptions of the frozen versions of the biomes.

sealed trait Biome(val name: String, world: World):
  override def toString = s"${this.name.toUpperCase}: ${this.getDescription}"

  def winningProbability = biomeWinningProbabilities.baseValue

  def getDescription: String

end Biome

/**
  * Biomes that appear dry on the ground level.
  */
sealed trait Dry extends Biome:
  protected var desription: String = defaultDescriptions.dry

  def isFavorable = false

  override def winningProbability = biomeWinningProbabilities.dry
  override def getDescription = defaultDescriptions.dry

end Dry

/**
  * In groundwater biome weak bonuses are readily available
  *
  */
class Groundwater(world: World)
  extends Dry, Biome("dry, groundwater", world):

  override def isFavorable = true
  override def winningProbability = biomeWinningProbabilities.groundwater

  override def toString = s"${super.getDescription}${this.getDescription}"

  override def getDescription =
    defaultDescriptions.groundwater

end Groundwater

/**
  * Permafrost biome does not provide bonuses unless avg(T) > 280 K has held for at least 100 years.
  *
  */
class Permafrost(world: World)
  extends Biome("dry, permafrost", world), Dry:

  private var isFrost = true

  def frozen = this.isFrost

  def melt() =
    println("MELTING BIOME")
    println(s"Biome state: ${this.frozen}")
    this.isFrost = false
    println(s"After melt: ${this.frozen}")
  def freeze() = this.isFrost = true

  // our intention was originally to change the winning probability at this location according to whether it has molten or not
  // but we could not make our code work and we can't understand why. The original code here is commented, but now the winning
  // probability is just the probability of a molten biome, regardless of the temperature.
  override def winningProbability = biomeWinningProbabilities.moltenPermafrost
    //if this.frozen then super.winningProbability
    //else biomeWinningProbabilities.moltenPermafrost  // if molten then provide a good probability

  override def toString = s"${super.getDescription}${this.getDescription}"

  override def getDescription =
    if this.frozen then defaultDescriptions.permafrost
    else defaultDescriptions.moltenPermafrost

end Permafrost


/**
  * In soil biome bonuses are similar to lake or ocean biome as far as moisture is optimal
  *
  */
class Soil(world: World)
  extends Biome("dry, soil", world), Dry:

  override def isFavorable = true
  override def winningProbability = biomeWinningProbabilities.soil
  override def toString = s"${super.getDescription}${this.getDescription}"

  override def getDescription =
    defaultDescriptions.soil

end Soil


/**
  * In rock biome no bonuses
  *
  */
class Rock(world: World)
  extends Biome("dry, rock", world), Dry:

  override def toString = s"${super.getDescription}${this.getDescription}"

  override def getDescription =
    defaultDescriptions.rock

end Rock


sealed trait WaterBiome extends Biome:
  protected var isLiquid = false

  val meltingThreshold: Int
  val maxCapsuleCapacity: Option[Int] // allow infinite capacity

  override def winningProbability = biomeWinningProbabilities.baseValue

  def melt() = this.isLiquid = true
  def freeze() = this.isLiquid = false

  def frozen = !isLiquid

  override def getDescription =
    if this.frozen then
      ""
    else
      defaultDescriptions.moltenWaterBiome

end WaterBiome


/**
  * Increased cell growth probabilities; moisture parameter is irrelevant
  *
  */
class Coast(world: World)
  extends Biome("coast", world), WaterBiome:
  // melts after 120 months
  val meltingThreshold = 120
  // can hold infinitely many capsules
  val maxCapsuleCapacity = None

  override def toString = s"${super.getDescription}${this.getDescription}"

  // our intention was originally to change the winning probability at this location according to whether it has molten or not
  // but we could not make our code work and we can't understand why. The original code here is commented, but now the winning
  // probability is just the probability of a molten biome, regardless of the temperature.
  override def winningProbability = biomeWinningProbabilities.coast
    //if this.frozen then
    //  biomeWinningProbabilities.baseValue
    //else
    //  biomeWinningProbabilities.coast

  override def getDescription =
    if this.frozen then defaultDescriptions.frostCoast
    else defaultDescriptions.moltenCoast

end Coast


/**
  * Increased cell growth probabilities; moisture parameter is irrelevant
  *
  */
class Lake(world: World)
  extends Biome("lake", world), WaterBiome:
  //melts after 12 months
  val meltingThreshold = 12
  //can only hold 5 capsules; allows more to be deployed, but they won't be of extra benefit
  val maxCapsuleCapacity = Some(5)

  override def toString = s"${super.getDescription}${this.getDescription}"

  // our intention was originally to change the winning probability at this location according to whether it has molten or not
  // but we could not make our code work and we can't understand why. The original code here is commented, but now the winning
  // probability is just the probability of a molten biome, regardless of the temperature.
  override def winningProbability = biomeWinningProbabilities.lake
    //if this.frozen then
    //  biomeWinningProbabilities.baseValue
    //else
    //  biomeWinningProbabilities.lake

  override def getDescription =
    if this.frozen then defaultDescriptions.frostLake
    else defaultDescriptions.moltenLake

end Lake
