package recTree

import scalafx.scene.canvas._
import scalafx.scene.Node
import scalafx.scene.paint.Color
import scalafx.animation.AnimationTimer
import scalafx.Includes._
import scalafx.scene.input.{MouseEvent, MouseButton}
import scala.math

class TreeDisplay {
    val _canvas : Canvas = new Canvas(400, 400)
    val _gc : GraphicsContext = _canvas.graphicsContext2D
    var _subtrees : List[SubTree] = Nil

    def node : Node = _canvas

    def subtrees_=(trees : List[SubTree]) = {
        _subtrees = trees
        redraw()
    }

    def recursiveDraw(remainingDepth : Int, subtreeId : Int, angle : Double, stretch : Double,
        x0 : Double, y0 : Double) : Unit = {
            if (Global.showColorsInDisplay) {
                _gc.fill_=(Global.subTreeColors(subtreeId))
                val dotRadius = math.sqrt(stretch) * Global.displayDotRadius
                _gc.fillOval(x0-dotRadius, y0-dotRadius, 2*dotRadius, 2*dotRadius)
            }

            if (remainingDepth > 0) {
                val tree = _subtrees(subtreeId)
                for (branch <- tree.branches) {
                    val branchVec : Vec2D = Vec2D.fromPolar(branch.state.branch.length,
                        branch.state.branch.angle + angle)
                    val x1 = _canvas.width.toDouble * stretch * branchVec.x + x0
                    val y1 = - _canvas.height.toDouble * stretch * branchVec.y + y0

                    _gc.strokeLine(x0, y0, x1, y1)
                    recursiveDraw(remainingDepth - 1, branch.state.childID,
                        angle + branch.state.co.angle + math.Pi/2, stretch * branch.state.co.length,
                        x1, y1)
                }
            }
    }

    def redraw() = {
        _gc.clearRect(0, 0, _canvas.width.toDouble, _canvas.height.toDouble)
        recursiveDraw(Global.treeDepth, 0, 0.0, Global.treeDisplayStretch,
            _canvas.width.toDouble * 0.5, _canvas.height.toDouble * 0.5)
    }

    val animationProperties_ = new {
        var startTime : Long = 0
        var setStartTime = false
    }

    val animationTimer_ = //new  AnimationTimer
        AnimationTimer.apply(
        (now : Long) => {
            if (!animationProperties_.setStartTime) {
                animationProperties_.startTime = now
                animationProperties_.setStartTime = true
            }
            val sec = 0.2*(now - animationProperties_.startTime) / 1000000000d
            if (sec < 1) {
            _gc.clearRect(0, 0, _canvas.width.toDouble, _canvas.height.toDouble)
            println("Test" + (now - animationProperties_.startTime));
            recursiveDraw(Global.treeDepth, 0, 0.0, Global.treeDisplayStretch * sec,
                _canvas.width.toDouble * 0.5, _canvas.height.toDouble * 0.5)
            }
        }
    )

    _canvas.handleEvent(MouseEvent.MousePressed) {
        (me : MouseEvent) => {
            me.button match {
                case MouseButton.PRIMARY => animationTimer_.start()
                case MouseButton.SECONDARY => {
                    animationTimer_.stop()
                    animationProperties_.setStartTime = false
                }
                case _ => {}
            }
        }
    }

}
