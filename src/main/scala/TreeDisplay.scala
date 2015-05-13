package recTree

import scalafx.scene.canvas._
import scalafx.scene.Node
import scalafx.scene.paint.Color
import scalafx.animation.AnimationTimer
import scalafx.Includes._
import scalafx.scene.input.{MouseEvent, MouseButton}
import scalafx.scene.layout.Pane
import scala.math

class TreeDisplay {
    val _container : Pane = new Pane()
    val _canvas : Canvas = new Canvas(500, 500)
    _container.content_=(_canvas)
    _container.getStyleClass().add("treeDisplay")
    val _gc : GraphicsContext = _canvas.graphicsContext2D
    var _subtrees : List[SubTree] = Nil

    def node : Node = _container

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

            if (remainingDepth > 0 && stretch > Global.stopRecursionStretch) {
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
        _gc.setStroke(Color(1.0, 1.0, 1.0, 1.0))
        recursiveDraw(Global.treeDepth, 0, 0.0, Global.treeDisplayStretch,
            _canvas.width.toDouble * 0.5, _canvas.height.toDouble * 0.5)
    }

    def animationDraw(remainingDepth : Int, subtreeId : Int, angle : Double, stretch : Double,
        x0 : Double, y0 : Double, time : Double) : Unit = {
            val growth = (1 - math.pow(0.5, time)) * (1 - math.pow(0.5, Global.treeDepth)) / (1 - math.pow(0.5, remainingDepth))
            val old_growth = (1 - math.pow(0.5, 1+time)) * (1 - math.pow(0.5, Global.treeDepth)) / (1 - math.pow(0.5, remainingDepth+1))
            if (Global.showColorsInDisplay) {
                _gc.fill_=(Global.subTreeColors(subtreeId))
                val dotRadius = math.sqrt(stretch * old_growth) * Global.displayDotRadius
                _gc.fillOval(x0-dotRadius, y0-dotRadius, 2*dotRadius, 2*dotRadius)
            }

            if (time > 0 && stretch > Global.stopRecursionStretch) {
                val tree = _subtrees(subtreeId)
                for (branch <- tree.branches) {
                    val branchVec : Vec2D = Vec2D.fromPolar(branch.state.branch.length,
                        branch.state.branch.angle + angle)
                    val x1 = _canvas.width.toDouble * stretch * branchVec.x * growth + x0
                    val y1 = - _canvas.height.toDouble * stretch * branchVec.y * growth + y0

                    _gc.strokeLine(x0, y0, x1, y1)
                    animationDraw(remainingDepth - 1, branch.state.childID,
                        angle + branch.state.co.angle + math.Pi/2, stretch * branch.state.co.length,
                        x1, y1, time - 1)
                }
            }
    }


    val animationProperties_ = new {
        var startTime : Long = 0
        var setStartTime = false
    }

    val animationTimer_ : AnimationTimer =
        AnimationTimer.apply(
        (now : Long) => {
            if (!animationProperties_.setStartTime) {
                animationProperties_.startTime = now
                animationProperties_.setStartTime = true
            }
            val sec = (now - animationProperties_.startTime) / 1000000000d
            if (sec <= Global.treeDepth) {
                _gc.clearRect(0, 0, _canvas.width.toDouble, _canvas.height.toDouble)
                val animStretch = Global.treeDisplayStretch /
                            (1 - math.pow(0.5, Global.treeDepth))  //has to scale to 100%
                animationDraw(Global.treeDepth, 0, 0.0, animStretch,
                   _canvas.width.toDouble * 0.5, _canvas.height.toDouble * 0.5, sec)
            } else {
                animationTimer_.stop()
                animationProperties_.setStartTime = false
                redraw()
            }
        }
    )

    _canvas.handleEvent(MouseEvent.MousePressed) {
        (me : MouseEvent) => {
            me.button match {
                case MouseButton.PRIMARY => {
                    if (animationProperties_.setStartTime) {  //is running already
                        animationTimer_.stop()
                        animationProperties_.setStartTime = false //reset
                        animationTimer_.start()
                    } else {
                        animationTimer_.start()
                    }
                }
                case MouseButton.SECONDARY => {
                    animationTimer_.stop()
                    animationProperties_.setStartTime = false
                    redraw()
                }
                case _ => {}
            }
        }
    }

}
