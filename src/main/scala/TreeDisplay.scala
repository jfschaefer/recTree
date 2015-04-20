package recTree

import scalafx.scene.canvas._
import scalafx.scene.Node
import scalafx.scene.paint.Color
import scalafx.Includes._

class TreeDisplay {
	val _canvas : Canvas = new Canvas(400, 400)
	val _gc : GraphicsContext = _canvas.graphicsContext2D
	var _subtrees : List[SubTree] = Nil

	//create initial screen
	_gc.fill_=(Color(1.0, 0.0, 0.0, 1.0))
	_gc.fillOval(0, 0, _canvas.width.toDouble, _canvas.height.toDouble)

	def node : Node = _canvas

	def subtrees_=(trees : List[SubTree]) = {
		_subtrees = trees
		redraw()
	}

	def recursiveDraw(remainingDepth : Int, subtreeId : Int, angle : Double, stretch : Double,
						x0 : Double, y0 : Double) : Unit = {
		if (remainingDepth > 0) {
			val tree = _subtrees(subtreeId)
			for (branch <- tree.branches) {
				val branchVec : Vec2D = Vec2D.fromPolar(branch.state.branch.length,
															branch.state.branch.angle + angle)
				val x1 = _canvas.width.toDouble * stretch * branchVec.x + x0
				val y1 = - _canvas.height.toDouble * stretch * branchVec.y + y0
				_gc.strokeLine(x0, y0, x1, y1)
				recursiveDraw(remainingDepth - 1, branch.state.childID,
								angle + branch.state.co.angle, stretch * branch.state.co.length,
								x1, y1)
			}
		}
	}

	def redraw() = {
		_gc.clearRect(0, 0, _canvas.width.toDouble, _canvas.height.toDouble)
		recursiveDraw(Global.treeDepth, 0, 0.0, Global.treeDisplayStretch,
						_canvas.width.toDouble * 0.5, _canvas.height.toDouble * 0.5)
	}
}
