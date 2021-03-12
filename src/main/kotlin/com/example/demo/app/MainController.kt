package com.example.demo.app

import tornadofx.Controller

class MainController : Controller() {
    enum class GraphTypes { Undirected, Directed, Tree }
    lateinit var graphType: GraphTypes
}