package com.example.demo.app

import com.example.demo.dataStructures.Graph
import com.example.demo.dataStructures.Vertex
import javafx.scene.shape.Line
import tornadofx.Controller

class GraphController : Controller() {

    enum class EditorMode { None, EditV, EditE, RemoveV, RemoveE }

    private var mode: EditorMode = EditorMode.None

    private var graph: Graph = Graph()

    private var initVertex: Vertex? = null // for creating edges (init -> target)
    private var targetVertex: Vertex? = null
    private var temporaryEdgeModel: Line? = null

    // Editor zone
    fun setEditorMode(newMode: EditorMode){
        mode = newMode
    }
    fun getEditorMode(): EditorMode{
        return mode
    }
    fun getGraph(): Graph{
        return graph
    }
    // Edge zone
    fun setInitVertex(vertex: Vertex?){
        initVertex = vertex
    }
    fun getInitVertex(): Vertex?{
        return initVertex
    }
    fun setTargetVertex(vertex: Vertex?){
        targetVertex = vertex
    }
    fun getTargetVertex(): Vertex?{
        return targetVertex
    }
    fun setTemporaryEdgeModel(line: Line?){
        temporaryEdgeModel = line
    }
    fun getTemporaryEdgeModel(): Line?{
        return temporaryEdgeModel
    }

}