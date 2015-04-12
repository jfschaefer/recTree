package recTree

import scalafx.scene.layout.Pane
import scalafx.scene.Node

class SubTreePane (val id : Int) {
	val _paneSmall = new Pane()    //display
	val _paneBig = new Pane()      //for actual user interaction

	_paneSmall.setStyle("-fx-background-color: green;")
	_paneSmall.setPrefSize(100, 100)

	_paneBig.setStyle("-fx-background-color: yellow;")
	_paneBig.setPrefSize(400, 400)

	def nodeSmall : Node = _paneSmall
	def nodeBig : Node = _paneBig
}
