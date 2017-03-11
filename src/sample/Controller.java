package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    private static final boolean ALIVE = true;
    private static final boolean NOTALIVE = false;
    private static final int RECTANGLE_WIDTH = 5;
    private static final int RECTANGLE_HEIGHT = 5;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    private static final int THREE = 3;
    private static final int TWO = 2;
    public static final int WREC = WIDTH / RECTANGLE_WIDTH;
    public static final int HREC = HEIGHT / RECTANGLE_HEIGHT;

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
    }

    private void drawField() {

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setSpecularColor(Color.ORANGE);
        redMaterial.setDiffuseColor(Color.RED);

        final Box red = new Box(400, 400, 400);
        red.setMaterial(redMaterial);

        for (int i = 0; i < WIDTH; i += RECTANGLE_WIDTH) {
            for (int j = 0; j < HEIGHT; j += RECTANGLE_HEIGHT) {

                Bot bot;
                if (Math.random() > 0.9) {
                    bot = new Bot(ALIVE);
                    bot.setFill(Color.WHITESMOKE);
                    bot.setStroke(Color.WHITE);
                    bots[i / RECTANGLE_WIDTH][j / RECTANGLE_HEIGHT] = bot;
                } else {
                    bot = new Bot(NOTALIVE);
                    bot.setFill(Color.LIGHTBLUE);
                    bots[i / RECTANGLE_WIDTH][j / RECTANGLE_HEIGHT] = bot;
                }

                field.getChildren().add((j + i * HEIGHT / RECTANGLE_WIDTH) / RECTANGLE_HEIGHT, bot);
            }
        }
    }

    @FXML
    public void startFinish() {

        if (!gameActive) {

            this.gameActive = true;

            Task task = new Task<Void>() {
                @Override
                public Void call() throws Exception {
                    int antX = WREC / 2, antY = HREC / 2;    // start in the middle-ish
                    int xChange = 0, yChange = -1; // start moving up
                    while (gameActive) {
                        if (Math.random() > 0.1) {
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
                        Thread.sleep(8);
                    }
                    return null;
                }
            };

            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();
            this.actionButton.setText("Stop ");
        } else {
            // the camera is not active at this point
            this.gameActive = false;
            // update again the button content
            this.actionButton.setText("Start");
            // stop the timer
            this.stopAcquisition();
        }
    }

    private static boolean[][] runAnt(int height, int width) {
        boolean[][] plane = new boolean[height][width];
        int antX = width / 2, antY = height / 2;    // start in the middle-ish
        int xChange = 0, yChange = -1; // start moving up
        while (antX < width && antY < height && antX >= 0 && antY >= 0) {
            if (plane[antY][antX]) {
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
            plane[antY][antX] = !plane[antY][antX];
            antX += xChange;
            antY += yChange;
        }
        return plane;
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

