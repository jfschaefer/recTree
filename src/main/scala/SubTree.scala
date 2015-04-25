package recTree

import scalafx.Includes._
import scalafx.scene.layout.Pane
import scalafx.scene.Node
import scalafx.scene.shape._
import scalafx.scene.paint.Color
import scalafx.scene.control.ContextMenu
import scalafx.scene.control.MenuItem
import scalafx.event.ActionEvent
import scalafx.scene.input.{MouseEvent, MouseButton}

class SubTree(val partId : Int, val updateTreeDisplay : () => Unit) {
    val _paneSmall = new Pane()    //display
    val _paneBig = new Pane()      //for actual user interaction

    val _smallSizeInit : Double = 100
    val _bigSizeInit : Double = 400

    var _branches : List[Branch] = Nil

    setUnselectedAppearance()
    _paneSmall.setPrefSize(_smallSizeInit, _smallSizeInit)
    _paneBig.setPrefSize(_bigSizeInit, _bigSizeInit)

    //Draw root nodes
    val _rootSmall : Circle = new Circle {
        stroke = Color.Black
        strokeWidth = 1
        fill = Global.subTreeColors(partId)
        radius = Global.smallDotSize
        centerX = _smallSizeInit * 0.5
        centerY = _smallSizeInit * 0.5
    }
    _paneSmall.children += _rootSmall

    val _rootBig : Circle = new Circle {
        stroke = Color.Black
        strokeWidth = 1
        fill = Global.subTreeColors(partId)
        radius = Global.bigDotSize
        centerX = _bigSizeInit * 0.5
        centerY = _bigSizeInit * 0.5
    }
    _paneBig.children += _rootBig

    //define Branch class
    class Branch (var _branchState : BranchState) {
        //main line
        val _thisPtr = this

        val _bigLineMain : Line = new Line {}
        _paneBig.children += _bigLineMain

        val _smallLineMain : Line = new Line {}
        _paneSmall.children += _smallLineMain

        //main dot
        val _bigDotMain : Circle = new Circle {
            stroke = Color.Black
            strokeWidth = 1
            radius = Global.bigDotSize
        }
        _paneBig.children += _bigDotMain

        val _smallDotMain : Circle = new Circle {
            stroke = Color.Black
            strokeWidth = 1
            radius = Global.smallDotSize
        }
        _paneSmall.children += _smallDotMain

        val dotMainDragS = new DragInitState();


        var _branchCMItems : List[MenuItem] = Nil
        val x = new MenuItem("Delete") {
            onAction = {e : ActionEvent => {_branches = _branches diff (_thisPtr :: Nil); removeAll() } }
        }
        _branchCMItems = x :: _branchCMItems
        for (i <- 0 to Global.numberOfSubtrees - 1) {
            val x = new MenuItem("Change color") {
                onAction = {e : ActionEvent => {_branchState.childID_=(i); updateAll() } }
            }
            x.setStyle("-fx-background-color: #%02X%02X%02X".format(
                                       (Global.subTreeColors(i).red * 255).toInt,
                                       (Global.subTreeColors(i).green * 255).toInt,
                                       (Global.subTreeColors(i).blue * 255).toInt))
            _branchCMItems = _branchCMItems ::: x :: Nil
        }

        val _branchCM = new ContextMenu (_branchCMItems : _*)

        _bigDotMain.handleEvent(MouseEvent.MousePressed) {
            (me : MouseEvent) => {
                me.button match {
                    case MouseButton.SECONDARY =>
                        _branchCM.show(_bigDotMain, me.x, me.y)
                    case _ =>
                }
            }
        }

        _bigDotMain.handleEvent(MouseEvent.Any) {
            (me : MouseEvent) => {
                me.eventType match {
                    case MouseEvent.MousePressed =>
                        dotMainDragS.objX = _bigDotMain.centerX.toDouble
                        dotMainDragS.objY = _bigDotMain.centerY.toDouble
                        dotMainDragS.mouseX = me.x
                        dotMainDragS.mouseY = me.y
                    case MouseEvent.MouseDragged =>
                        val newX : Double = dotMainDragS.objX + me.x - dotMainDragS.mouseX
                        val newY : Double = dotMainDragS.objY + me.y - dotMainDragS.mouseY
                        if ((0 < newX)  && (newX < _paneBig.width.toDouble) &&
                                            (0 < newY) && (newY < _paneBig.height.toDouble)) {
                            //it looks nice, if you can leave the box, but you can move it to 
                            //a later unreachable position
                            val newVec = new Vec2D((newX/_paneBig.width.toDouble - 0.5)/Global.subtreeMainStretch,
                                                        -(newY/_paneBig.height.toDouble - 0.5)/Global.subtreeMainStretch)
                            if (newVec.length > (7d/_paneBig.width.toDouble)) {   //otherwise it's getting too crowded in the center, and we
                                                       //will have issues like division by zero
                                _branchState.moveBranch(newVec)
                                updateAll()
                            }
                        }
                    case _ =>
                }
            }
        }


        //co line
        val _bigLineCo : Line = new Line() {
            strokeDashArray ++= Global.subTreeBigDashed
            stroke = Global.subTreeDarkish
        }
        _paneBig.children += _bigLineCo

        val _smallLineCo : Line = new Line() {
            strokeDashArray ++= Global.subTreeSmallDashed
            stroke = Global.subTreeDarkish
        }
        _paneSmall.children += _smallLineCo

        //co dot
        val _bigDotCo : Circle = new Circle() {
            strokeWidth = 2
            radius = Global.bigDotSize
            fill = Global.subTreeDarkish
        }
        _paneBig.children += _bigDotCo

        val _smallDotCo : Circle = new Circle() {
            strokeWidth = 1
            radius = Global.smallDotSize
            fill = Global.subTreeDarkish
        }
        _paneSmall.children += _smallDotCo

        val dotCoDragS = new DragInitState();

        _bigDotCo.handleEvent(MouseEvent.Any) {
            (me : MouseEvent) => {
                me.eventType match {
                    case MouseEvent.MousePressed =>
                        dotCoDragS.objX = _bigDotCo.centerX.toDouble
                        dotCoDragS.objY = _bigDotCo.centerY.toDouble
                        dotCoDragS.mouseX = me.x
                        dotCoDragS.mouseY = me.y
                    case MouseEvent.MouseDragged =>
                        val newX : Double = dotCoDragS.objX + me.x - dotCoDragS.mouseX
                        val newY : Double = dotCoDragS.objY + me.y - dotCoDragS.mouseY
                        if ((0 < newX)  && (newX < _paneBig.width.toDouble) &&
                                            (0 < newY) && (newY < _paneBig.height.toDouble)) {
                            //it looks nice, if you can leave the box, but you can move it to 
                            //a later unreachable position
                            val newVec = new Vec2D(
                                (newX/_paneBig.width.toDouble - 0.5)/Global.subtreeMainStretch - _branchState.branch.x,
                                -(newY/_paneBig.height.toDouble - 0.5)/Global.subtreeMainStretch - _branchState.branch.y)
                            if (newVec.length > (5d/_paneBig.width.toDouble)) {   //otherwise you'd have problems accessing the node later
                                _branchState.co_=(newVec)
                                updateAll()
                            }
                        }
                    case _ =>
                }
            }

        }



        updateAll()

        def updateAll() = {
            val bw = _paneBig.width.toDouble
            val bh = _paneBig.height.toDouble
            val sw = _paneSmall.width.toDouble
            val sh = _paneSmall.height.toDouble
            val bx = _branchState.branch.x
            val by = _branchState.branch.y
            val cx = _branchState.co.x
            val cy = _branchState.co.y

            val ms = Global.subtreeMainStretch
            val cs = Global.subtreeCoStretch
            //main
            _bigLineMain.startX_=(bw * 0.5)
            _bigLineMain.startY_=(bh * 0.5)
            _bigLineMain.endX_=(bw * (0.5 + ms * bx))
            _bigLineMain.endY_=(bh * (0.5 - ms * by))
            _bigDotMain.centerX_=(bw * (0.5 + ms * bx))
            _bigDotMain.centerY_=(bh * (0.5 - ms * by))

            _smallLineMain.startX_=(sw * 0.5)
            _smallLineMain.startY_=(sh * 0.5)
            _smallLineMain.endX_=(sw * (0.5 + ms * bx))
            _smallLineMain.endY_=(sh * (0.5 - ms * by))
            _smallDotMain.centerX_=(sw * (0.5 + ms * bx))
            _smallDotMain.centerY_=(sh * (0.5 - ms * by))

            //co
            _bigLineCo.startX_=(bw * (0.5 + cs * bx))
            _bigLineCo.startY_=(bh * (0.5 - cs * by))
            _bigLineCo.endX_=(bw * (0.5 + cs * (bx + cx)))
            _bigLineCo.endY_=(bh * (0.5 - cs * (by + cy)))

            _bigDotCo.centerX_=(bw * (0.5 + cs * (bx + cx)))
            _bigDotCo.centerY_=(bh * (0.5 - cs * (by + cy)))

            _smallLineCo.startX_=(sw * (0.5 + cs * bx))
            _smallLineCo.startY_=(sh * (0.5 - cs * by))
            _smallLineCo.endX_=(sw * (0.5 + cs * (bx + cx)))
            _smallLineCo.endY_=(sh * (0.5 - cs * (by + cy)))

            _smallDotCo.centerX_=(sw * (0.5 + cs * (bx + cx)))
            _smallDotCo.centerY_=(sh * (0.5 - cs * (by + cy)))

            _bigDotMain.fill_=(Global.subTreeColors(_branchState.childID))
            _smallDotMain.fill_=(Global.subTreeColors(_branchState.childID))

            _bigDotCo.stroke_=(Global.subTreeColors(_branchState.childID))
            _smallDotCo.stroke_=(Global.subTreeColors(_branchState.childID))

            updateTreeDisplay()
        }

        def removeAll() = {
            for (e <- List(_bigDotMain, _bigDotCo, _bigLineMain, _bigLineCo)) {
                _paneBig.children.remove(e)
            }
            for (e <- List(_smallDotMain, _smallDotCo, _smallLineMain, _smallLineCo)) {
                 _paneSmall.children.remove(e)
            }
            updateTreeDisplay()
        }

        def state : BranchState = _branchState
    }

    //create contex menu for root node
    var _rootCMItems : List[MenuItem] = Nil
    for (i <- 0 to Global.numberOfSubtrees - 1) {
        val x = new MenuItem("New subtree") {
                onAction = {e : ActionEvent => {
                        val newBranch = new Branch(
                            new BranchState(new Vec2D(0, 1), new Vec2D(0, 0.5), i))
                        _branches = newBranch :: _branches
                        updateAll()
                    }}
        }
        x.setStyle("-fx-background-color: #%02X%02X%02X".format(
                                       (Global.subTreeColors(i).red * 255).toInt,
                                       (Global.subTreeColors(i).green * 255).toInt,
                                       (Global.subTreeColors(i).blue * 255).toInt))
        _rootCMItems = _rootCMItems ::: x :: Nil
    }
    val _rootBigCM = new ContextMenu (_rootCMItems : _*)

    _rootBig.handleEvent(MouseEvent.MousePressed) {
            (me : MouseEvent) => {
                me.button match {
                    case MouseButton.SECONDARY =>
                        _rootBigCM.show(_rootBig, me.x, me.y)
                    case _ =>
                }
            }
        }


    def nodeSmall : Node = _paneSmall
    def nodeBig : Node = _paneBig

    def setSelectedAppearance() = {
        _paneSmall.setStyle("-fx-background-color: #ffffff;")
    }

    def setUnselectedAppearance() = {
        _paneSmall.setStyle("-fx-background-color: #cccccc;")
    }

    def updateAll() {
        println(_paneSmall.width.toDouble)
        _rootSmall.centerX_=(_paneSmall.width.toDouble * 0.5)
        _rootSmall.centerY_=(_paneSmall.height.toDouble * 0.5)
        _rootBig.centerX_=(_paneBig.width.toDouble * 0.5)
        _rootBig.centerY_=(_paneBig.width.toDouble * 0.5)
        for (branch <- _branches) branch.updateAll()
    }

    def branches : List[Branch] = _branches
}

