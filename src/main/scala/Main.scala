package recTree

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.layout._
import scalafx.scene.control.TitledPane
import scalafx.scene.{Scene, Group, Node}


object Main extends JFXApp {
	val NUMBER_OF_SUBTREES = 4
	val _treeDisplay = new TreeDisplay()
	var _subtreePanes : List[SubTreePane] = Nil

	for (i <- 0 to NUMBER_OF_SUBTREES-1) {
		_subtreePanes = _subtreePanes ::: new SubTreePane(i) :: Nil
	}

	var _drawPane = new Pane()
	_drawPane.content_=(_subtreePanes(0).nodeBig)

	val _gridPane = new GridPane()

	stage = new JFXApp.PrimaryStage() {
		title="recTree"
		scene = new Scene() {
			root = _gridPane
			_gridPane.add(_drawPane, 0, 0, NUMBER_OF_SUBTREES, 1)
			for (i <- 0 to NUMBER_OF_SUBTREES-1) {
				_gridPane.add(_subtreePanes(i).nodeSmall, i, 1)
			}
			_gridPane.add(_treeDisplay.node, NUMBER_OF_SUBTREES, 0, 1, 2)
		}
	}
}
