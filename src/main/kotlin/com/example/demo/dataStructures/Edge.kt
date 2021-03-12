package com.example.demo.dataStructures

import javafx.scene.shape.Arc


class Edge(_v1: Vertex, _v2: Vertex) {
    private var v1: Vertex
    private var v2: Vertex
    private lateinit var edgeModel: Arc
    init{
        v1 = _v1
        v2 = _v2
    }

    fun setEdgeModel(arc: Arc){
        edgeModel = arc
    }
    fun getEdgeModel(): Arc{
        return edgeModel
    }
    fun getV1(): Vertex{
        return v1
    }
    fun getV2(): Vertex{
        return v2
    }
    fun getTargetV(vertex: Vertex): Vertex{
        return if (vertex != v1) v1
        else v2
    }
    fun removeEdge(){
        val model = getEdgeModel()
        model.isDisable = true; model.isVisible = false
    }
}