package recTree

import scalafx.scene.canvas._
import scalafx.scene.Node
import scalafx.scene.paint.Color

class TreeDisplay {
	val _canvas : Canvas = new Canvas(400, 400)
	val _gc : GraphicsContext = _canvas.graphicsContext2D

	//create initial screen
	_gc.fill_=(Color(1.0, 0.0, 0.0, 1.0))
	_gc.fillOval(0, 0, _canvas.width.toDouble, _canvas.height.toDouble)

	def node : Node = _canvas
}
