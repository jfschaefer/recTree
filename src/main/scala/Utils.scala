package recTree


object Vec2D {
	//def Vec2D(x : Double, y : Double) : Vec2D = new Vec2D(x, y)
	def fromPolar(length : Double, angle : Double) : Vec2D =
	             new Vec2D(length * math.cos(angle), length * math.sin(angle))
}

class Vec2D (val x : Double, val y : Double) {
	def scale(l : Double) = new Vec2D(x*l, y*l)
	def add(vec : Vec2D) = new Vec2D(x + vec.x, y + vec.y)
	def length : Double = math.sqrt(x*x + y*y)
	def angle : Double = (if (y < 0) -1 else 1)  * math.acos(x/(math.sqrt(x*x+y*y)))  //math.atan(y / x)
}

class BranchState(var _branch : Vec2D, var _co : Vec2D, var _childID : Int) {

	def moveBranch(new_ : Vec2D) = {
		val dilation = new_.length / _branch.length
		val rotation = new_.angle - _branch.angle
		_co = Vec2D.fromPolar(_co.length * dilation, _co.angle + rotation)
		_branch = new_
	}

	def co_=(vec : Vec2D) = {
		_co = vec
	}

	def co : Vec2D = _co

	def branch : Vec2D = _branch

	def childID : Int = _childID

	def childID_=(id : Int) = {
		_childID = id
	}
}

class DragInitState {
	var objX : Double = 0.0
	var objY : Double = 0.0
	var mouseX : Double = 0.0
	var mouseY : Double = 0.0
}
