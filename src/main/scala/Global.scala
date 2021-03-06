package recTree

import scalafx.scene.paint.Color
import scalafx.Includes._

object Global {
	val subTreeColors : List[Color] =
		Color(1, 0, 0, 0.8) :: Color(0, 0.5, 0, 0.8) :: Color(0, 0, 1, 0.8) :: Color(1, 1, 0, 0.8) :: Nil
	         //"#ff0000" :: "#008800" :: "#0000ff" :: "#ffff00" :: Nil;
	val subTreeDarkish : Color = Color(0, 0, 0, 0.5)
	val subTreeLightish : Color = Color(1d, 1d, 1d, 0.5)

	val numberOfSubtrees : Int = 4

	//val subTreeBigDashed = Array(Double.box(2d), Double.box(3d))

	//val subTreeSmallDashed = Array(Double.box(1d), Double.box(1d))

	val subtreeMainStretch : Double = 0.2
	val subtreeCoStretch : Double = 0.2
    
    val stopRecursionStretch : Double = 0.001  //Stop recursion if stretch is lower

	val treeDisplayStretch : Double = 0.2  //More precisely, the initial stretch

	val treeDepth : Int = 8

    val showColorsInDisplay : Boolean = true
    val displayDotRadius : Double = 30d

	val smallDotSize : Int = 3
	val bigDotSize : Int = 6
}
