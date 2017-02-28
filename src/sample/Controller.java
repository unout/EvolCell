package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static sample.Bot.*;

public class Controller {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int THREE = 3;
    private static final int TWO = 2;
    private static final int WREC = WIDTH / RECTANGLE_WIDTH;
    private static final int HREC = HEIGHT / RECTANGLE_HEIGHT;

    private ScheduledExecutorService timer;
    private Bot[][] bots;
    @FXML
    public Button actionButton;
    @FXML
    private Group field;

    private boolean gameActive = false;

    void init() {

        bots = new Bot[WREC][HREC];
        drawField();
//        createImageData();
//        drawImageData();
    }

    private void drawField() {
        for (int i = 0; i < WIDTH; i += RECTANGLE_WIDTH) {
            for (int j = 0; j < HEIGHT; j += RECTANGLE_HEIGHT) {

                Rectangle rectangle = new Rectangle(i, j, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
                rectangle.setStroke(Color.LIGHTGRAY);

                if (Math.random() > 0.5) {
                    rectangle.setFill(Color.WHITESMOKE);
                    Bot bot = new Bot(rectangle, ALIVE);
                    bots[i / RECTANGLE_WIDTH][j / RECTANGLE_HEIGHT] = bot;
                } else {
                    rectangle.setFill(Color.BLUE);
                    Bot bot = new Bot(rectangle, NOTALIVE);
                    bots[i / RECTANGLE_WIDTH][j / RECTANGLE_HEIGHT] = bot;
                }

                bots[i / RECTANGLE_WIDTH][j / RECTANGLE_HEIGHT].setRectangle(rectangle);

                field.getChildren().add((j + i * HEIGHT / RECTANGLE_WIDTH) / RECTANGLE_HEIGHT, rectangle);
            }
        }
    }


    private void life() {
        while (gameActive) {
            for (int i = 1; i < WREC - 1; i++) {
                for (int j = 1; j < HREC - 1; j++) {
                    byte neighborSum = (byte) (bots[i + 1][j + 1].getFlag() + bots[i][j + 1].getFlag() +
                            bots[i + 1][j].getFlag() + bots[i - 1][j + 1].getFlag() + bots[i + 1][j - 1].getFlag() +
                            bots[i - 1][j - 1].getFlag() + bots[i - 1][j].getFlag() + bots[i][j - 1].getFlag());
                    if (neighborSum == THREE)
                        bots[i][j].setFlag(ALIVE);
                    if (neighborSum > THREE)
                        bots[i][j].setFlag(NOTALIVE);
                    if (neighborSum < TWO)
                        bots[i][j].setFlag(NOTALIVE);
                    field.getChildren().remove(j + i * HREC);
                    field.getChildren().add(j + i * HREC, bots[i][j].getRectangle());

                }
            }
        }
    }

//    private void redraw() {
//        field.getChildren().addAll(bots);
//    }

    @FXML
    public void startFinish() {
        if (!gameActive) {

            this.gameActive = true;

            Task task = new Task<Void>() {
                @Override
                public Void call() throws Exception {
                    while (gameActive) {
                        for (int i = 1; i < WREC - 1; i++) {
                            for (int j = 1; j < HREC - 1; j++) {
                                byte neighborSum = (byte) (bots[i + 1][j + 1].getFlag() + bots[i][j + 1].getFlag() +
                                        bots[i + 1][j].getFlag() + bots[i - 1][j + 1].getFlag() + bots[i + 1][j - 1].getFlag() +
                                        bots[i - 1][j - 1].getFlag() + bots[i - 1][j].getFlag() + bots[i][j - 1].getFlag());
                                if (neighborSum == 3)
                                    bots[i][j].setFlag(ALIVE);
                                if (neighborSum > 3)
                                    bots[i][j].setFlag(NOTALIVE);
                                if (neighborSum < 2)
                                    bots[i][j].setFlag(NOTALIVE);
                                int finalJ = j;
                                int finalI = i;
                                Platform.runLater(() -> {
                                    field.getChildren().remove(finalJ + finalI * HREC);
                                    field.getChildren().add(finalJ + finalI * HREC, bots[finalI][finalJ].getRectangle());
                                });

                            }
                        }
                        Thread.sleep(100);
                    }
                    return null;
                }
            };

            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();
            this.actionButton.setText("Stop ");

            } else{
                // the camera is not active at this point
                this.gameActive = false;
                // update again the button content
                this.actionButton.setText("Start");

                // stop the timer
                this.stopAcquisition();
            }
        }

    private void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("Exception in stopping timer... " + e);
            }
        }
    }

    void setClosed() {
        this.stopAcquisition();
    }
}

