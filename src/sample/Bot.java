package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Bot extends Rectangle {

    private boolean flag;

    Bot(boolean flag) {
        setFlag(flag);
    }

    boolean getFlag() {
        return flag;
    }

    void setFlag(boolean flag) {
        this.flag = flag;

        if (flag)
            setFill(Color.KHAKI);
        else
            setFill(Color.WHITESMOKE);
    }

}
