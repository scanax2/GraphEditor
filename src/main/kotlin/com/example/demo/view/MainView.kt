package com.example.demo.view

import com.example.demo.app.GraphController
import com.example.demo.app.MainController
import com.example.demo.app.Styles
import com.example.demo.app.UIController
import com.example.demo.fragments.EditorFragment
import com.example.demo.resources.ApplicationResources
import javafx.event.EventTarget
import javafx.scene.paint.Color
import tornadofx.*
import javafx.scene.control.Button;
import java.util.*


class MainView : View("GraphEditor") {

    private val controller: MainController by inject()

    override val root = hbox {
        paddingAll = 10.0
        // Main page with graph prefabs, choose one -> load, switch to edit mode
        flowpane{
            prefWidth = ApplicationResources.width
            prefHeight = ApplicationResources.height
            hgap = 5.0
            vgap = 5.0
            paddingAll = 10.0

            label(title) {
                addClass(Styles.heading)
            }

            for (i in 1..15) {
                button {
                    text("Click me !")
                    action {
                        //this@flowpane.setDisable(true)
                        //this@flowpane.setVisible(false)
                        controller.graphType = MainController.GraphTypes.Undirected
                        replaceWith<EditorView>()
                    }
                    addClass(Styles.prefab)
                }
            }
        }
    }
}


//TODO Change EditV -> addVE, EditE -> EditV

class EditorView : View("GraphEditor") {
    private val graphController: GraphController by inject()
    private var topButtons: Array<Button> = emptyArray()

    override val root = borderpane {
        // Settings
        prefWidth = ApplicationResources.width
        prefHeight = ApplicationResources.height

        // Body
        top = hbox {
            // Settings
            prefHeight = 80.0
            useMaxWidth = true
            useMaxHeight = true
            //spacing = 5.0
            style {
                backgroundColor += Color.PURPLE
            }

            //Back button
            button {
                text = "<-"
                useMaxHeight = true
                action {
                    UIController.setEnableButtons(topButtons)
                    graphController.setEditorMode(GraphController.EditorMode.None)
                    replaceWith<MainView>()
                }
                addClass(Styles.actionButton)
            }

            //Edit V button
            topButtons += button {
                text = "Edit V"
                useMaxHeight = true
                useMaxWidth = true

                action {
                    UIController.setEnableButtons(topButtons)
                    this.setDisable(true)
                    graphController.setEditorMode(GraphController.EditorMode.EditV)
                }
                addClass(Styles.editButton)
            }

            //Edit E button
            topButtons += button {
                text = "Edit E"
                useMaxHeight = true
                useMaxWidth = true
                action {
                    UIController.setEnableButtons(topButtons)
                    this.setDisable(true)
                    graphController.setEditorMode(GraphController.EditorMode.EditE)
                }
                addClass(Styles.editButton)
            }

            //Remove V button
            topButtons += button {
                text = "Remove V"
                useMaxHeight = true
                useMaxWidth = true
                action {
                    UIController.setEnableButtons(topButtons)
                    this.setDisable(true)
                    graphController.setEditorMode(GraphController.EditorMode.RemoveV)
                }
                addClass(Styles.removeButton)
            }

            //Remove E button
            topButtons += button {
                text = "Remove E"
                useMaxHeight = true
                useMaxWidth = true
                action {
                    UIController.setEnableButtons(topButtons)
                    this.setDisable(true)
                    graphController.setEditorMode(GraphController.EditorMode.RemoveE)
                }
                addClass(Styles.removeButton)
            }
            //label("TOP")

        }
        bottom = hbox {
            // Settings
            useMaxWidth = true
            useMaxHeight = true
            style {
                backgroundColor += Color.BLUE
            }

            label("BOTTOM")
        }
        left = vbox {
            // Settings
            useMaxWidth = true
            useMaxHeight = true
            style {
                backgroundColor += Color.GREEN
            }

            label("LEFT")
        }
        right = vbox {
            // Settings
            useMaxWidth = true
            useMaxHeight = true
            style {
                backgroundColor += Color.YELLOW
            }

            label("RIGHT")
        }
        center = stackpane {
            //Settings
            useMaxWidth = true
            useMaxHeight = true
            style {
                backgroundColor += Color.ANTIQUEWHITE
            }
            // Editor - work area
            this += find<EditorFragment>()
        }
    }
}


