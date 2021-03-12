package com.example.demo.fragments

import com.example.demo.app.GraphController
import com.example.demo.app.MainController
import com.example.demo.app.Styles
import com.example.demo.app.UIController
import com.example.demo.dataStructures.Edge
import com.example.demo.dataStructures.Graph
import com.example.demo.dataStructures.Vertex
import com.example.demo.resources.ApplicationResources
import javafx.event.EventTarget
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import javafx.scene.shape.Circle
import tornadofx.*
import kotlin.math.ceil


class EditorFragment() : Fragment(){

    val controller: GraphController by inject()
    override val root = anchorpane{

        // Editor area (shortpress/longpress)
        val graph = controller.getGraph()
        // Add/Remove vertex by click (Add V/E mode || Remove V/E mode)
        setOnMouseClicked { event ->
            println(event.x.toString() + " " + event.y.toString())
            when(controller.getEditorMode()) {
                // 1. Case of Add V/E mode (V add)
                GraphController.EditorMode.EditV -> {
                    val outsideOfArea: Boolean = UIController.isOutsideOfArea(
                                                    ApplicationResources.width,
                                                    ApplicationResources.height,
                                                    event.x, event.y, Vertex.radius)

                    val collisionV: Boolean = UIController.isVIntersects(event.x, event.y, graph.getVertices())

                    val tmpEdgeAlreadyDisabled: Boolean = controller.getTemporaryEdgeModel()?.isDisable == true
                    val definitelyNotTryingToAddEdge: Boolean = controller.getTemporaryEdgeModel() == null
                    // is Correct position ? yes -> create V, no -> go out
                    if (outsideOfArea || collisionV) {
                        controller.getTemporaryEdgeModel()?.isDisable = true
                        controller.getTemporaryEdgeModel()?.isVisible = false
                        return@setOnMouseClicked
                    }
                    else if (tmpEdgeAlreadyDisabled || definitelyNotTryingToAddEdge) {
                        val vertex: Vertex = vertex(event.x, event.y, graph)
                    }
                    else {
                        // Disabling tmp edge
                        controller.getTemporaryEdgeModel()?.isDisable = true
                        controller.getTemporaryEdgeModel()?.isVisible = false
                    }
                }

                // 2. Case of Remove V/E mode (V remove)
                GraphController.EditorMode.RemoveV -> {
                    val clickedVertex = UIController.vClicked(event.x, event.y, graph.getVertices())
                        ?: return@setOnMouseClicked // if null -> go out
                    graph.removeVertex(clickedVertex)
                    graph.decreaseVerticesIndex(clickedVertex)
                }
                else -> {}
            }
        }
        // Create E (Edit V mode)
        setOnMousePressed { event ->
            println("Drag started !")
            when(controller.getEditorMode()){
                GraphController.EditorMode.EditV -> {
                    val initVertex = UIController.vClicked(event.x, event.y, graph.getVertices())
                    if (initVertex != null) {
                        controller.setInitVertex(initVertex)
                    }
                    else{
                        return@setOnMousePressed
                    }
                    val lineTmp = line {
                        startX = initVertex.getPosition()[0]
                        startY = initVertex.getPosition()[1]
                        endX = event.x
                        endY = event.y
                        toBack()
                    }
                    controller.setTemporaryEdgeModel(lineTmp)
                }
                GraphController.EditorMode.RemoveE -> {
                    val clickedEdge: Edge? = UIController.eClicked(event.x, event.y, graph.getEdges())
                    for (edge in graph.getEdges()){
                        var reverseFactor = 1
                        if (edge.getEdgeModel().startAngle == 180.0){
                            reverseFactor = -1
                        }
                        circle{
                            centerX = edge.getEdgeModel().centerX
                            centerY = edge.getEdgeModel().centerY
                            radius = 4.0
                        }
                    }
                    //tmp if state
                    if (clickedEdge != null){
                        line{
                            startX = event.x; startY = event.y
                            endX = clickedEdge.getEdgeModel().centerX
                            endY = clickedEdge.getEdgeModel().centerY
                        }
                    }
                    //tmp points state
                    val points = UIController.testCirclePoints(event.x, event.y, graph.getEdges())
                    for (point in points){
                        circle {
                            centerX = point[0]
                            centerY = point[1]
                            radius = 4.0}
                    }
                    //clickedEdge?.getEdgeModel()?.stroke = Color.WHITE
                }
                else -> {}
            }
        }
        // Drag V or Add E (Edit V mode || Add V/E mode)
        setOnMouseDragged { event ->
            println("Dragging ...")
            when(controller.getEditorMode()){
                // 1. Case of Edit V mode (drag V)
                //TODO rename EditE -> EditV
                GraphController.EditorMode.EditE -> {
                    val draggingVertex = UIController.vClicked(event.x, event.y, graph.getVertices())
                    draggingVertex?.updatePosition(event.x, event.y, graph.getVertices())
                }
                // 2. Case of Add V/E mode (Add E)
                GraphController.EditorMode.EditV -> {
                    if (controller.getInitVertex() == null){
                        return@setOnMouseDragged // if no initVertex -> go out
                    }
                    controller.getTemporaryEdgeModel()?.endX = event.x
                    controller.getTemporaryEdgeModel()?.endY = event.y

                }
                else -> {}
            }

        }
        // Delete initVertex (End of drag), create E or not
        setOnMouseReleased { event ->
            println("Drag ended !")
            when(controller.getEditorMode()){  // 1. Case of Add V/E mode (Add E by hold)
                GraphController.EditorMode.EditV -> {
                    val targetVertex = UIController.vClicked(event.x, event.y, graph.getVertices())
                    if (targetVertex == null){ // Delete tmp line if no targetVertex
                        return@setOnMouseReleased
                    }
                    else{ // Create edge otherwise
                        val edge: Edge
                        controller.setTargetVertex(targetVertex)

                        val initVertex = controller.getInitVertex()
                        val x1 = initVertex?.getPosition()?.get(0); val y1 = initVertex?.getPosition()?.get(1)
                        val x2 = targetVertex.getPosition()[0]; val y2 = targetVertex.getPosition()[1]

                        if (x1 == null || y1 == null) return@setOnMouseReleased

                        val degree = initVertex.getDegreeWithV(targetVertex)
                        val angle = kotlin.math.atan2(y1 - y2, x1 - x2) * (180 / Math.PI)
                        val radY = ApplicationResources.multipleEdgesStep*ceil(degree/2.0)
                        var startPointAngle = 0.0

                        //TODO: add custom Edit E radiusY by mouse hold
                        if (degree > 0 && degree % 2 != 0){
                            startPointAngle = 180.0
                        }
                        if (angle < 0 && angle >= -180){ // III, IV
                            startPointAngle = if (startPointAngle == 180.0) 0.0
                            else 180.0
                        }

                        val arcTmp = arc{
                            centerX = (x1+x2)/2.0
                            centerY = (y1+y2)/2.0
                            radiusX = UIController.distanceTwoPoints(x1,y1,x2,y2)/2
                            radiusY = radY  // Multiple edges (0.0 - straight line)
                            length = 180.0
                            startAngle = startPointAngle // Multiple edges (up/down curves)
                            stroke = Color.BLACK
                            strokeWidth = 3.0
                            fill = null
                            type = ArcType.OPEN
                            rotate = angle
                            toBack()
                        }
                        arcTmp.centerX -= arcTmp.radiusY* UIController.calculateCenterXoffset(
                            angle,
                            arcTmp.startAngle)
                        arcTmp.centerY -= arcTmp.radiusY* UIController.calculateCenterYoffset(
                            angle,
                            arcTmp.startAngle)

                        edge = graph.addEdge(initVertex, targetVertex)
                        controller.getTemporaryEdgeModel()?.let { edge.setEdgeModel(arcTmp) }

                        // Delete init line (reset tmp models)
                        controller.getTemporaryEdgeModel()?.isDisable = true
                        controller.getTemporaryEdgeModel()?.isVisible = false
                        controller.setTemporaryEdgeModel(null)
                        controller.setInitVertex(null)
                        controller.setTargetVertex(null)

                    }
                }
                else -> {}

            }
        }
    }
}