package com.example.demo.app

import com.example.demo.dataStructures.Edge
import com.example.demo.dataStructures.Vertex
import javafx.scene.control.Button
import tornadofx.Controller
import tornadofx.toProperty
import kotlin.math.pow
import kotlin.math.sqrt


class UIController: Controller() {
    companion object {
        fun setEnableButtons(buttons: Array<Button>){
            for (button in buttons){
                button.setDisable(false)
            }
            return
        }

        fun isOutsideOfArea(width: Double, height: Double, x: Double, y: Double, margin: Double): Boolean{
            return if (x !in margin..width-margin || y !in margin..height-margin){
                println("BORDERS: Out of range")
                true
            }
            else{
                false
            }
        }

        fun isVIntersects(x: Double, y: Double, vertices: Array<Vertex>): Boolean{
            for (vertex in vertices){
                val x2 = vertex.getPosition()[0]; val y2 = vertex.getPosition()[1]
                if (Math.sqrt(Math.pow(x2 - x, 2.0) + Math.pow(y2 - y, 2.0)) <= 2*Vertex.radius){
                    println("V INTERSECTS: Out of range")
                    return true
                }
            }
            return false
        }

        fun vClicked(x: Double, y: Double, vertices: Array<Vertex>): Vertex?{
            for (vertex in vertices){
                val x2 = vertex.getPosition()[0]; val y2 = vertex.getPosition()[1]
                //TODO: FIX BOUNDS IT'S RECTANGLE !
                if (x in x2-Vertex.radius..x2+Vertex.radius && y in y2-Vertex.radius..y2+Vertex.radius){
                    return vertex
                }
            }
            return null
        }

        fun getLabelPosX(x: Double, index: Int): Double{
            val offsetFactor = (index.toString().length - 1).toDouble()
            return x - Vertex.radius / 4 - (offsetFactor * 8)
        }
        fun getLabelPosY(y: Double): Double {
            return y - Vertex.radius / 2
        }
        // TODO: move to math class
        fun isWithinRotatedEllipse(x: Double, y: Double, xc: Double, yc: Double,
                                   angle: Double, radiusX: Double, radiusY: Double): Boolean{
            return (Math.pow(Math.cos(angle)*(x-xc)+Math.sin(angle)*(y-yc),2.0)/Math.pow(radiusX,2.0))+
                    (Math.pow(Math.sin(angle)*(x-xc)-Math.cos(angle)*(y-yc),2.0)/Math.pow(radiusY,2.0)) <= 1
        }
        //actual problem function
        fun eClicked(x: Double, y: Double, edges: Array<Edge>): Edge?{
            var closestEdge: Edge? = null
            if (edges.isNotEmpty()) closestEdge = edges.first()
            for (edge in edges){
                val xc = edge.getEdgeModel().centerX
                val yc = edge.getEdgeModel().centerY + 20.0
                val radiusX = edge.getEdgeModel().radiusX
                var radiusY = edge.getEdgeModel().radiusY

                if (radiusY == 0.0) radiusY = 25.0

                val angle = edge.getEdgeModel().rotate*(Math.PI/180.0)
                // https://stackoverflow.com/questions/7946187/point-and-ellipse-rotated-position-test-algorithm
                val pointWithinEdge: Boolean = isWithinRotatedEllipse(x, y, xc, yc, angle, radiusX, radiusY)

                // Searching for closest edge
                if (pointWithinEdge && closestEdge != null){

                    var savedX = closestEdge.getEdgeModel().centerX
                    var savedY = closestEdge.getEdgeModel().centerY

                    var newX = edge.getEdgeModel().centerX
                    var newY = edge.getEdgeModel().centerY

                    val savedAngle = closestEdge.getEdgeModel().rotate*(Math.PI/180.0)
                    val newAngle = edge.getEdgeModel().rotate*(Math.PI/180.0)
                    // startAngle = 180.0 -x+y -> +x-y (sin x, cos y)
                    var inverseS = 1.0
                    if (closestEdge.getEdgeModel().startAngle == 180.0){
                        inverseS *= -1.0
                    }
                    // savedX -= Math.sin(savedAngle)*inverseS; savedY += Math.cos(savedAngle)*inverseS

                    var inverseO = 1.0
                    if (edge.getEdgeModel().startAngle == 180.0){
                        inverseO *= -1.0
                    }
                    // newX -= Math.sin(newAngle)*inverseO; newY += Math.cos(newAngle)*inverseO
                    if (distanceTwoPoints(x, y, savedX, savedY) > distanceTwoPoints(x, y, newX, newY)){
                        closestEdge = edge
                    }
                    println("Detected edge click ! :)")
                    println("$newX, $newY")
                }
            }
            return closestEdge
        }
        fun calcEdgeCenterX(x: Double, radiusX: Double, startAngle: Double, angleInDeg: Double): Double{
            var inverse: Int = 1
            if (startAngle == 180.0) {
                inverse = -1
            }
            return if (inverse == 1){
                x - radiusX * (1- haversin(angleInDeg))
            } else {
                x + radiusX * (1- haversin(angleInDeg))
            }
        }
        fun calcEdgeCenterY(y: Double, radiusY: Double, startAngle: Double, angleInDeg: Double): Double{
            var inverse: Int = 1
            if (startAngle == 180.0) {
                inverse = -1
            }
            return if (inverse == 1){
                y + radiusY * (1- haversin(angleInDeg))
            } else {
                y - radiusY * (haversin(angleInDeg))
            }
        }
        //tmp function
        fun testCirclePoints(x: Double, y: Double, edges: Array<Edge>): Array<Array<Double>>{
            var xyArray: Array<Array<Double>> = emptyArray()
            var closestEdge: Edge? = null
            if (edges.isNotEmpty()) closestEdge = edges.first()
            for (edge in edges) {
                val xc = edge.getEdgeModel().centerX
                val yc = edge.getEdgeModel().centerY + 20.0
                val radiusX = edge.getEdgeModel().radiusX
                var radiusY = edge.getEdgeModel().radiusY

                if (radiusY == 0.0) radiusY = 25.0

                val angle = edge.getEdgeModel().rotate * (Math.PI / 180.0)
                // https://stackoverflow.com/questions/7946187/point-and-ellipse-rotated-position-test-algorithm
                val pointWithinEdge: Boolean = isWithinRotatedEllipse(x, y, xc, yc, angle, radiusX, radiusY)

                // Searching for closest edge
                if (pointWithinEdge && closestEdge != null) {
                    var savedX = closestEdge.getEdgeModel().centerX
                    var savedY = closestEdge.getEdgeModel().centerY

                    var newX = edge.getEdgeModel().centerX
                    var newY = edge.getEdgeModel().centerY

                    savedX = calcEdgeCenterX(savedX, closestEdge.getEdgeModel().radiusX,
                        closestEdge.getEdgeModel().startAngle, closestEdge.getEdgeModel().rotate)
                    savedY = calcEdgeCenterY(savedY, closestEdge.getEdgeModel().radiusY,
                        closestEdge.getEdgeModel().startAngle, closestEdge.getEdgeModel().rotate)

                    println("--------")
                    println("$newX, $newY")
                    println("angle: ${edge.getEdgeModel().rotate}")
                    newX = calcEdgeCenterX(newX, edge.getEdgeModel().radiusX,
                        edge.getEdgeModel().startAngle, edge.getEdgeModel().rotate)
                    newY = calcEdgeCenterY(newY, edge.getEdgeModel().radiusY,
                        edge.getEdgeModel().startAngle, edge.getEdgeModel().rotate)

                    println("$newX, $newY")
                    println("--------")

                    if (distanceTwoPoints(x, y, savedX, savedY) > distanceTwoPoints(x, y, newX, newY)) {
                        closestEdge = edge
                    }
                    println("Detected edge click ! :)")
                    println("$newX, $newY")
                    xyArray += arrayOf(newX, newY)
                }
            }
            return xyArray
        }
        //TODO move to related Math module(need to create)
        fun distanceTwoPoints(x1: Double, y1: Double, x2: Double, y2: Double): Double{
            return sqrt((x2 - x1).pow(2.0) + (y2 - y1).pow(2.0))
        }

        fun haversin(angleInDeg: Double): Double {
            val radAngle = (angleInDeg/180.0) * Math.PI
            return (1.0-Math.cos(radAngle))/2.0
        }
        fun calculateCenterXoffset(angle: Double, startPoint: Double): Double{
            var offset: Double = 0.0
            // I - sin
            if (angle < 0.0 && angle >= -90.0){
                offset = haversin(angle)
            }
            else if (angle < -90.0 && angle >= -180.0){
                offset = 1-haversin(angle)
            }
            else if (angle < 180.0 && angle >= 90.0){
                offset = -(1-haversin(angle))
            }
            else if (angle >= 0 && angle < 90.0){
                offset = -haversin(angle)
            }
            if (startPoint == 180.0) offset *= -1
            return offset
        }
        fun calculateCenterYoffset(angle: Double, startPoint: Double): Double{
            var offset: Double = 0.0
            // I - sin
            if (angle < 0.0 && angle >= -90.0){
                offset = -haversin(angle)
            }
            else if (angle < -90.0 && angle > -180.0){
                offset = -haversin(angle)
            }
            else if (angle <= 180.0 && angle >= 90.0){
                offset = -haversin(angle)
            }
            else if (angle >= 0 && angle < 90.0){
                offset = -haversin(angle)
            }
            if (startPoint == 180.0) offset *= -1
            return offset
        }


        fun updateEdges(edges: Array<Edge>){
            edges.forEach {
                val x1 = it.getV1().getPosition()[0]; val y1 = it.getV1().getPosition()[1]
                val x2 = it.getV2().getPosition()[0]; val y2 = it.getV2().getPosition()[1]
                val angle = kotlin.math.atan2(y1 - y2, x1 - x2) * (180 / Math.PI)

                val diameterX = distanceTwoPoints(x1, y1, x2, y2)
                it.getEdgeModel().rotate = angle
                it.getEdgeModel().radiusX = diameterX/2.0
                // Middle point on hypotenuse
                it.getEdgeModel().centerX = (x1+x2)/2.0 - it.getEdgeModel().radiusY*
                        calculateCenterXoffset(angle, it.getEdgeModel().startAngle)
                it.getEdgeModel().centerY = (y1+y2)/2.0 - it.getEdgeModel().radiusY*
                        calculateCenterYoffset(angle, it.getEdgeModel().startAngle)
            }
        }
    }
}