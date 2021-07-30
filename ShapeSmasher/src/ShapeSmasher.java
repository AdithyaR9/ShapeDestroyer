

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class ShapeSmasher extends Application {

    private int X;
    private int Y;

    private Image skull = new Image("Skull.png");
    public static final int PLAYING = 1;
    public static final int GAMEOVER = 2;
    private int status;

    private int updates_per_second = 10;
    private boolean running = true;
    private int squareWidth = 40;
    private int squareHeight = 40;
    private int circleWidth = 20;
    private int circleHeight = 20;
    private int lives = 3;
    private int points = 0;
    Canvas canvas;
    long updatesDone = 0;
    long timeBetweenUpdates = 1000 / updates_per_second;
    long startTime = System.nanoTime();

    Rectangle square = new Rectangle();
    private long squareMaxTime = 3000;
    private long squareSpawnTime = 0;

    Ellipse circle = new Ellipse();
    private long circleMaxTime = 2500;
    private long circleSpawnTime = 0;

    Polygon triangle = new Polygon();
    private double[] triXs = new double[3];
    private double[] triYs = new double[3];
    private long triangleMaxTime = 2000;
    private long triangleSpawnTime = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Shape Smasher");

        Group group = new Group();

        canvas = new Canvas(500, 500);

        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                X = (int) event.getX();
                Y = (int) event.getY();

                if (status == GAMEOVER) {
                    if (event.isSecondaryButtonDown()) {
                        reset();
                    }
                }

            }
        });

        new AnimationTimer() {

            @Override
            public void handle(long currentTime) {
                long updatesNeeded = ((currentTime - startTime) / 1000000) / timeBetweenUpdates;
                for (; updatesDone < updatesNeeded; updatesDone++) {
                    update();
                }
                paint(canvas.getGraphicsContext2D());
            }

        }.start();


        group.getChildren().add(canvas);

        Scene scene = new Scene(group);

        primaryStage.setScene(scene);

        reset();
        paint(canvas.getGraphicsContext2D());

        canvas.requestFocus();

        primaryStage.show();


    }

    public void reset() {

        lives = 3;
        points = 0;
        status = PLAYING;

        moveSquare();
        moveCircle();
        moveTriangle();

    }

    public void paint(GraphicsContext gc) {

        //background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 500, 500);

        //info
        gc.setFill(Color.WHITE);
        gc.fillText("Deaths: ", 20, 20);
        gc.fillText("Score: " + points, 400, 20);


        //skulls
        if (lives == 2) {
            gc.drawImage(skull, 100, 10, 40, 40);
        } else if (lives == 1) {
            gc.drawImage(skull, 100, 10, 40, 40);
            gc.drawImage(skull, 150, 10, 40, 40);
        } else if (lives == 0) {
            gc.drawImage(skull, 100, 10, 40, 40);
            gc.drawImage(skull, 150, 10, 40, 40);
            gc.drawImage(skull, 200, 10, 40, 40);
        }


        //draw shapes

        //square
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(square.getX(), square.getY(), squareWidth, squareHeight);

        //circle
        gc.setFill(Color.LIGHTGREEN);
        gc.fillOval(circle.getCenterX()-20, circle.getCenterY()-20, circleWidth*2, circleHeight*2);


        //triangle
        gc.setFill(Color.WHITE);
        gc.fillPolygon(triXs, triYs, 3);

        if (status == GAMEOVER) {
            gc.setFill(Color.RED);
            gc.fillText("Game Over\nRight Click to Restart", 200, 200);

        }
    }

    public void update() {

        if (lives <= 0) {
            status = GAMEOVER;
            running = false;
        }

        if (status == PLAYING) {
            //square
            if ((System.nanoTime() - squareSpawnTime) / 1000000 >= squareMaxTime) {

                moveSquare();
                lives--;
            }

            if (square.contains(X, Y)) {
                moveSquare();
                points++;
            }

            //circle
            if ((System.nanoTime() - circleSpawnTime) / 1000000 >= circleMaxTime) {

                moveCircle();
                lives--;
            }

            if (circle.contains(X, Y)) {

                moveCircle();
                points += 3;
            }

            //triangle
            if ((System.nanoTime() - triangleSpawnTime) / 1000000 >= triangleMaxTime) {

                moveTriangle();
                lives--;
            }

            if (triangle.contains(X, Y)) {

                moveTriangle();
                points += 5;
            }
        }
    }

    public void moveSquare() {

        double x = (Math.random() * 400) + 50;
        double y = (Math.random() * 400) + 50;
        square = new Rectangle(x, y, squareWidth, squareHeight);
        squareSpawnTime = System.nanoTime();

    }

    public void moveCircle() {

        double x = (Math.random() * 400) + 50;
        double y = (Math.random() * 400) + 50;
        circle = new Ellipse(x, y, circleWidth, circleHeight);
        circleSpawnTime = System.nanoTime();
    }

    public void moveTriangle() {

        double x1 = (Math.random() * 400) + 50;
        double y1 = (Math.random() * 400) + 50;
        double x2 = x1 + 20;
        double y2 = y1 + 40;
        double x3 = x1 - 20;
        double y3 = y1 + 40;
        triangleSpawnTime = System.nanoTime();
        triangle.getPoints().clear();
        triangle.getPoints().addAll(new Double[]{x1, y1, x2, y2, x3, y3});
        triXs[0] = x1;
        triXs[1] = x2;
        triXs[2] = x3;
        triYs[0] = y1;
        triYs[1] = y2;
        triYs[2] = y3;

    }


    @Override
    public void stop() throws Exception {
        running = false;
        super.stop();
    }

}










