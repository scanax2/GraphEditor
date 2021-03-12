package com.example.demo.app

//import android.app.Activity
import com.example.demo.resources.ApplicationResources
import com.example.demo.view.MainView
import javafx.stage.Stage
import tornadofx.App

class MyApp: App(MainView::class, Styles::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        with(stage){
            width = ApplicationResources.width
            height = ApplicationResources.height
            isResizable = false
        }
    }
}