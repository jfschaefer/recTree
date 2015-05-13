package recTree

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.layout._
import scalafx.scene.input.{MouseEvent, MouseButton}
import scalafx.scene.{Scene, Group, Node}


object Main extends JFXApp {
	val _treeDisplay = new TreeDisplay()


	//INITIALIZE SUBTREES
	var _subtrees : List[SubTree] = Nil
	var selectedSubTree : Int = 0

	for (i <- 0 to Global.numberOfSubtrees-1) {
		val newOne = new SubTree(i, () => _treeDisplay.redraw())
		_subtrees = _subtrees ::: newOne :: Nil

		//User can select subtree by clicking on it
		val displayPane = newOne.nodeSmall
		displayPane.handleEvent(MouseEvent.MousePressed) {
			(me : MouseEvent) => {
				me.button match {
					case MouseButton.PRIMARY =>
						//onResize()
						_subtrees(selectedSubTree).setUnselectedAppearance()
						selectedSubTree = i
						_drawPane.content_=(newOne.nodeBig)
						newOne.setSelectedAppearance()
					case _ =>
				}
			}
		}
	}

	_treeDisplay.subtrees_=(_subtrees)

	val _drawPane = new Pane()
	_drawPane.content_=(_subtrees(selectedSubTree).nodeBig)
	_subtrees(selectedSubTree).setSelectedAppearance()

	val _gridPane = new GridPane()
    _gridPane.getStyleClass().add("background")

	stage = new JFXApp.PrimaryStage() {
		title="recTree"
		scene = new Scene() {
			root = _gridPane
			_gridPane.add(_drawPane, 0, 0, Global.numberOfSubtrees, 1)
			for (i <- 0 to Global.numberOfSubtrees-1) {
				_gridPane.add(_subtrees(i).nodeSmall, i, 1)
			}
			_gridPane.add(_treeDisplay.node, Global.numberOfSubtrees, 0, 1, 2)
		}
        scene().stylesheets += this.getClass.getResource("/css/dark.css").toExternalForm
	}

	def onResize() = {
		//resize subtrees
		for (i <- 0 to Global.numberOfSubtrees - 1) {
			_subtrees(i).updateAll()
		}
	}
}
