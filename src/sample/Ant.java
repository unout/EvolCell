package sample;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;

import static sample.Controller.HREC;
import static sample.Controller.WREC;

class Ant {

    private Bot[][] bots;
    private boolean gameActive;
    private Group field;
    private Thread th;

    public void runAnt() {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                int antX = WREC / 2, antY = HREC / 2;    // start in the middle-ish
                int xChange = 0, yChange = -1; // start moving up
                while (isGameActive()) {

                    if (bots[antY][antX].getFlag()) {
                        // turn left
                        if (xChange == 0) { // if moving up or down
                            xChange = yChange;
                            yChange = 0;
                        } else { // if moving left or right
                            yChange = -xChange;
                            xChange = 0;
                        }
                    } else {
                        // turn right
                        if (xChange == 0) { // if moving up or down
                            xChange = -yChange;
                            yChange = 0;
                        } else { // if moving left or right
                            yChange = xChange;
                            xChange = 0;
                        }
                    }

                    bots[antY][antX].setFlag(!bots[antY][antX].getFlag());
                    antX += xChange;
                    antY += yChange;

                    if (antX > WREC - 1) antX = 0;
                    if (antY > HREC - 1) antY = 0;
                    if (antX < 0) antX = WREC - 1;
                    if (antY < 0) antY = HREC - 1;

                    int finalAntY = antY;
                    int finalAntX = antX;
                    Platform.runLater(() -> {
//                            bots[finalAntY][finalAntX].setFlag(!bots[finalAntY][finalAntX].getFlag());
                        field.getChildren().remove(finalAntY + finalAntX * HREC);
                        field.getChildren().add(finalAntY + finalAntX * HREC, bots[finalAntX][finalAntY]);
                    });
                    Thread.sleep(10);
                }
                return null;
            }
        };
        this.th = new Thread(task);
        this.th.setDaemon(true);

    }

    Ant(Bot[][] bots, boolean gameActive, Group field) {
        this.bots = bots;
        this.gameActive = gameActive;
        this.field = field;
    }

    private boolean isGameActive() {

        if (gameActive) {
            this.th.start();
            return gameActive;
        } else {
            this.th.stop();
            return gameActive;
        }
    }

    void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }
}
