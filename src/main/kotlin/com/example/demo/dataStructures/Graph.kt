package com.example.demo.dataStructures

//TODO: FIX MVC, Model have to include API which allows to interact with user in different ways

class Graph {
    private var vertices: Array<Vertex> = emptyArray()
    private var edges: Array<Edge> = emptyArray()

    fun addVertex(posX: Double, posY: Double): Vertex{
        val vertex: Vertex = Vertex(posX, posY)
        vertices += vertex
        vertex.setIndex(vertices.size) // set vertex index
        return vertex
    }
    fun removeVertex(vertex: Vertex){
        // Disable vertex
        val model = vertex.getModel(); val label = vertex.getLabel()
        model.isDisable = true; model.isVisible = false
        label.isDisable = true; label.isVisible = false
        vertices = (vertices.filter { it != vertex }).toTypedArray()
        // Disable related edges
        vertex.getEdges().forEach { removeEdge(it) }
    }
    fun removeEdge(edge: Edge){
        val vertex1 = edge.getV1(); val vertex2 = edge.getV2()
        edge.removeEdge()
        vertex1.unlinkEdge(edge); vertex2.unlinkEdge(edge)
        edges = (edges.filter { it != edge }).toTypedArray()

    }
    fun decreaseVerticesIndex(fromVertex: Vertex){
        val fromIndex = fromVertex.getIndex()-1
        for (i in fromIndex until vertices.size){
            val newIndex = vertices[i].getIndex()-1
            vertices[i].setIndex(newIndex)
            vertices[i].updateLabelIndex()
        }
    }
    fun addEdge(v1: Vertex, v2: Vertex): Edge{
        val edge: Edge = Edge(v1, v2)
        edges += edge
        v1.addEdge(edge)
        v2.addEdge(edge)
        return edge
    }

    fun getVerticesCount(): Int{
        return vertices.size
    }
    fun getEdgesCount(): Int{
        return edges.size
    }
    fun getVertices(): Array<Vertex>{
        return vertices
    }
    fun getEdges(): Array<Edge>{
        return edges
    }
    fun dragVertex(vertex: Vertex){

    }
}