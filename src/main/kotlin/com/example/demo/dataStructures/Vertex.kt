package com.example.demo.dataStructures

import com.example.demo.app.UIController
import com.example.demo.resources.ApplicationResources
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.Label
import javafx.scene.shape.Circle
import kotlin.properties.Delegates

class Vertex(_posX: Double, _posY: Double) {
    companion object {
        const val radius: Double = ApplicationResources.vertexRadius
    }
    private var posX: Double
    private var posY: Double

    private var index by Delegates.notNull<Int>()
    private lateinit var labelIndex: Label
    /* Optional labels - checkboxes -> Right side
       labelDegree (if P - green color, if NP - red), optional labels
       labelNeighbours  */
    private lateinit var vertexModel: Circle

    private var edges: Array<Edge> = emptyArray()
    private var degree: Int = 0
    init{
        posX = _posX
        posY = _posY
    }

    fun setLabel(newLabel: Label){
        labelIndex = newLabel
    }
    fun getLabel(): Label{
        return labelIndex
    }
    fun setModel(newModel: Circle){
        vertexModel = newModel
    }
    fun getModel(): Circle{
        return vertexModel
    }
    fun updateLabelIndex(){
        labelIndex.text = index.toString()
    }
    @JvmName("setIndex1")
    fun setIndex(int: Int){
        index = int
    }
    @JvmName("getIndex1")
    fun getIndex(): Int{
        return index
    }
    fun updatePosition(newX: Double, newY: Double, vertices: Array<Vertex>){
        if (UIController.isVIntersects(newX, newY, (vertices.filter { it != this }).toTypedArray())
            || UIController.isOutsideOfArea(ApplicationResources.width, ApplicationResources.height,
                newX, newY, Vertex.radius)){
            return // if intersects with V or borders -> stop drag
        }
        // Update vertex model pos
        posX = newX; posY = newY
        vertexModel.centerX = newX
        vertexModel.centerY = newY
        // Update label index pos
        labelIndex.layoutX = UIController.getLabelPosX(newX, index)
        labelIndex.layoutY = UIController.getLabelPosY(newY)
        // Update edges orientation
        UIController.updateEdges(this.getEdges())
    }
    fun getPosition(): List<Double>{
        return listOf(posX,posY)
    }
    fun addEdge(edge: Edge){
        edges += edge
        degree += 1
    }
    fun getEdges(): Array<Edge>{
        return edges
    }
    fun getDegree(): Int{
        return degree
    }
    fun getDegreeWithV(vertex: Vertex): Int{
        var deg = 0
        edges.forEach {
            if (it.getV1() == vertex && it.getV2() == this
                || it.getV1() == this && it.getV2() == vertex){
                deg += 1
            }
        }
        return deg
    }
    fun getDegreeToV(vertex: Vertex): Int{
        var deg = 0
        edges.forEach {
            if (it.getV1() == this && it.getV2() == vertex){
                deg += 1
            }
        }
        return deg
    }
    fun unlinkEdge(edge: Edge){
        edges = (edges.filter { it != edge }).toTypedArray()
        degree -= 1
    }
}