package com.example.demo.fragments

import com.example.demo.app.Styles
import com.example.demo.app.UIController
import com.example.demo.dataStructures.Graph
import com.example.demo.dataStructures.Vertex
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.shape.Circle
import tornadofx.*

/// Reusable graphics (like constructors :/)
fun Parent.vertex(x: Double, y: Double, graph: Graph, op1: Circle.() -> Unit = {}, op2: Label.() -> Unit = {}): Vertex {
    val vertex = graph.addVertex(x, y)

    val circleTmp = Circle(x, y, Vertex.radius).attachTo(this, op1)
    circleTmp.addClass(Styles.vertex)
    circleTmp.toFront()

    vertex.setModel(circleTmp)

    val labelTmp = Label(graph.getVerticesCount().toString()).attachTo(this, op2)
    //TODO make UIController function & fix offset after decrease e.g. 10 -> 9
    labelTmp.layoutX = UIController.getLabelPosX(x, vertex.getIndex())
    labelTmp.layoutY = UIController.getLabelPosY(y)
    labelTmp.addClass(Styles.vertex)
    labelTmp.toFront()

    vertex.setLabel(labelTmp)

    return vertex
}
//TODO: add function for edge