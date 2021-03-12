package com.example.demo.app

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val prefab by cssclass()
        val textColor = c("#FFFFFF")
        val textColorBlack = c("#000000")
        val editButton by cssclass()
        val buttonEditBackgroundColor = c("0AEA00")
        val buttonEditHoverBackgroundColor = c("0AFF00")
        val removeButton by cssclass()
        val buttonRemoveBackgroundColor = c("E10016")
        val buttonRemoveHoverBackgroundColor = c("FF0017")
        val actionButton by cssclass()
        val buttonActionBackgroundColor = c("FFFFFB")
        val buttonActionHoverBackgroundColor = c("FF000C")
        val vertex by cssclass()
        val edge by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }

        prefab{
            padding = box(10.px)
            prefWidth = 150.px
            prefHeight = 150.px
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += buttonEditBackgroundColor
            textFill = textColor

            and(hover){
                backgroundColor += buttonEditHoverBackgroundColor
            }
        }

        editButton{
            fontSize = 25.px
            fontWeight = FontWeight.LIGHT
            backgroundColor += buttonEditBackgroundColor
            textFill = textColor

            prefWidth = 225.px
            backgroundRadius = multi(box(0.percent))
            borderColor += box(c("FFFFFF"))

            and(hover){
                backgroundColor += buttonEditHoverBackgroundColor
            }
        }

        removeButton{
            fontSize = 25.px
            fontWeight = FontWeight.LIGHT
            backgroundColor += buttonRemoveBackgroundColor
            textFill = textColor

            prefWidth = 225.px
            backgroundRadius = multi(box(0.percent))
            borderColor += box(c("FFFFFF"))

            and(hover){
                backgroundColor += buttonRemoveHoverBackgroundColor
            }
        }

        actionButton{
            prefWidth = 80.px
            prefHeight = 80.px
            fontSize = 30.px
            fontWeight = FontWeight.LIGHT
            backgroundColor += buttonActionBackgroundColor
            textFill = textColorBlack

            backgroundRadius = multi(box(0.percent))

            and(hover){
                textFill = textColor
                backgroundColor += buttonActionHoverBackgroundColor
            }
        }

        vertex{
            // Text
            fontSize = 35.px
            fontWeight = FontWeight.BOLD
            textFill = textColorBlack
            // Circle
            fill = Color.WHITE
            stroke = Color.BLACK
            strokeWidth = 5.px
        }

        edge{

        }
    }
}