package sample

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

internal class KotBot {

    var rectangle: Rectangle? = null
    var flag: Byte = 0
        set(flag) {
            field = flag

            if (flag.toInt() == 1)
                rectangle?.fill = Color.KHAKI
            else
                rectangle?.fill = Color.WHITESMOKE
        }


//    constructor(rectangle: Rectangle, flag: Byte) {
//        this.rectangle = rectangle
//        flag = flag
//    }

    constructor(rectangle: Rectangle) {
        this.rectangle = rectangle
    }

    companion object {

        val ALIVE: Byte = 1
        val NOTALIVE: Byte = 0
        val RECTANGLE_WIDTH = 10
        val RECTANGLE_HEIGHT = 10
    }

}
