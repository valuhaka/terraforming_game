import o1.adventure.*

val game = Game()
game.player.planet.params.map( (name, param) => (name, param.isValid) )

game.player.planet
game.player.planet.adjustParam("temp", 2)