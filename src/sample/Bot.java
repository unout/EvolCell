package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Bot {

    static final byte ALIVE = 1;
    static final byte NOTALIVE = 0;
    static final int RECTANGLE_WIDTH = 10;
    static final int RECTANGLE_HEIGHT = 10;

    private Rectangle rectangle;
    private byte flag;


    Bot(Rectangle rectangle, byte flag) {
        this.rectangle = rectangle;
        setFlag(flag);
    }

    Bot(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    byte getFlag() {
        return flag;
    }

    void setFlag(byte flag) {
        this.flag = flag;

        if (flag == 1)
            getRectangle().setFill(Color.BLUE);
        else
            getRectangle().setFill(Color.WHITESMOKE);
    }

    Rectangle getRectangle() {
        return rectangle;
    }

    void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

}
