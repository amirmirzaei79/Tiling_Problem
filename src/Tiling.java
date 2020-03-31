import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Tiling extends Application {
    private final int SCREEN_WIDTH = 850, SCREEN_HEIGHT = 950;
    private final int SOLUTION_SIZE = 750;

    private enum Direction {
        topLeft,
        /*
         *   *
         *  **
         */
        topRight,
        /*
         *  *
         *  **
         */
        bottomLeft,
        /*
         *  **
         *   *
         */
        bottomRight
        /*
         *  **
         *  *
         */
    }

    private static class Tile {
        int left, top;
        Direction direction;
    }

    private static ArrayList<Tile> tiles = null;

    /**
     * Solves The Tiling Problem recursively and stores it in tiles ArrayList
     *
     * @param n    size of board
     * @param mx   missing tile x
     * @param my   missing tile y
     * @param left left of current board
     * @param top  top of current board
     * @return true if a solution is possible, false if there is no possible solution
     */
    private static boolean solve(int n, int mx, int my, int left, int top) {
        if (tiles == null)
            tiles = new ArrayList<>();

        if (n < 2 || left < 0 || top < 0 || mx - left >= n || mx < left || my - top >= n || my < top)
            return false;

        if (n == 2) {
            Tile t = new Tile();
            t.left = left;
            t.top = top;

            if (mx == left && my == top)
                t.direction = Direction.topLeft;
            else if (mx == left + 1 && my == top)
                t.direction = Direction.topRight;
            else if (mx == left + 1 && my == top + 1)
                t.direction = Direction.bottomRight;
            else if (mx == left && my == top + 1)
                t.direction = Direction.bottomLeft;
            else
                return false;

            tiles.add(t);
            return true;
        } else {
            boolean f;
            Tile t = new Tile();
            t.left = left + n / 2 - 1;
            t.top = top + n / 2 - 1;
            if ((mx - left) < n / 2 && (my - top) < n / 2) {
                t.direction = Direction.topLeft;
                f = solve(n / 2, mx, my, left, top);
                f = f & solve(n / 2, left + n / 2, top + n / 2 - 1, left + n / 2, top);
                f = f & solve(n / 2, left + n / 2, top + n / 2, left + n / 2, top + n / 2);
                f = f & solve(n / 2, left + n / 2 - 1, top + n / 2, left, top + n / 2);
            } else if ((mx - left) >= n / 2 && (my - top) < n / 2) {
                t.direction = Direction.topRight;
                f = solve(n / 2, left + n / 2 - 1, top + n / 2 - 1, left, top);
                f = f & solve(n / 2, mx, my, left + n / 2, top);
                f = f & solve(n / 2, left + n / 2, top + n / 2, left + n / 2, top + n / 2);
                f = f & solve(n / 2, left + n / 2 - 1, top + n / 2, left, top + n / 2);
            } else if ((mx - left) >= n / 2 && (my - top) >= n / 2) {
                t.direction = Direction.bottomRight;
                f = solve(n / 2, left + n / 2 - 1, top + n / 2 - 1, left, top);
                f = f & solve(n / 2, left + n / 2, top + n / 2 - 1, left + n / 2, top);
                f = f & solve(n / 2, mx, my, left + n / 2, top + n / 2);
                f = f & solve(n / 2, left + n / 2 - 1, top + n / 2, left, top + n / 2);
            } else // (mx - left) < n / 2 && (my - top) >= n / 2
            {
                t.direction = Direction.bottomLeft;
                f = solve(n / 2, left + n / 2 - 1, top + n / 2 - 1, left, top);
                f = f & solve(n / 2, left + n / 2, top + n / 2 - 1, left + n / 2, top);
                f = f & solve(n / 2, left + n / 2, top + n / 2, left + n / 2, top + n / 2);
                f = f & solve(n / 2, mx, my, left, top + n / 2);
            }

            tiles.add(t);
            return f;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        firstScreen(stage);
    }

    /**
     * first screen to get size and missing tile x & y
     *
     * @param stage stage to set screen to
     */
    private void firstScreen(Stage stage) {
        Text invalidInput = new Text("");
        invalidInput.setFont(new Font(25));

        Text sizeText = new Text("Size: ");
        sizeText.setFont(new Font(12));

        TextField sizeField = new TextField();
        sizeField.setPrefWidth(0.5 * SCREEN_WIDTH);
        sizeField.setPrefHeight(20);
        sizeField.setFont(new Font(12));

        HBox sizeBox = new HBox(sizeText, sizeField);
        sizeBox.setSpacing(20);
        sizeBox.setAlignment(Pos.CENTER);

        Text xText = new Text("Missing X: ");
        xText.setFont(new Font(12));

        TextField xField = new TextField();
        xField.setPrefWidth(0.2 * SCREEN_WIDTH);
        xField.setPrefHeight(20);
        xField.setFont(new Font(12));

        Text yText = new Text("Missing Y: ");
        yText.setFont(new Font(12));

        TextField yField = new TextField();
        xField.setPrefWidth(0.2 * SCREEN_WIDTH);
        xField.setPrefHeight(20);
        xField.setFont(new Font(12));

        HBox missingBox = new HBox(xText, xField, yText, yField);
        missingBox.setSpacing(20);
        missingBox.setAlignment(Pos.CENTER);

        Button solveButton = new Button("Solve");
        solveButton.setPrefWidth(0.25 * SCREEN_WIDTH);
        solveButton.setPrefHeight(0.15 * SCREEN_HEIGHT);
        solveButton.setOnMouseClicked(e -> {
            int n, x, y;

            try {
                n = Integer.parseInt(sizeField.getCharacters().toString());
                x = Integer.parseInt(xField.getCharacters().toString());
                y = Integer.parseInt(yField.getCharacters().toString());

                if (!solve(n, x, y, 0, 0)) {
                    invalidInput.setText("Invalid Input!");
                    tiles.clear();
                } else {
                    printSolution();
                    solutionScreen(stage, n, x, y);
                }
            } catch (NumberFormatException exp) {
                invalidInput.setText("Invalid Input!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox vBox = new VBox(invalidInput, sizeBox, missingBox, solveButton);
        vBox.setPrefWidth(SCREEN_WIDTH);
        vBox.setPrefHeight(SCREEN_HEIGHT);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(0.1 * SCREEN_HEIGHT);

        Scene scene = new Scene(vBox);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method to print solution to standard output
     * should only be called after solve method
     */
    private void printSolution() {
        for (Tile t : tiles)
            System.out.println(t.left + " _ " + t.top + " _ " + t.direction);

        System.out.println();
        System.out.println("Number of tiles: " + tiles.size());
        System.out.println();
    }

    /**
     * solution screen to show the solution and number of used tiles
     *
     * @param stage stage to set screen to
     * @param n     size of board
     * @param mx    missing tile x
     * @param my    missing tile y
     */
    private void solutionScreen(Stage stage, int n, int mx, int my) {
        int blockSize = SOLUTION_SIZE / n;

        Pane solution = new Pane();

        Image missing = new Image("file:Pics/missing.png");
        ImageView missingView = new ImageView(missing);
        missingView.setPreserveRatio(true);
        missingView.setFitWidth(blockSize);
        missingView.setFitHeight(blockSize);
        missingView.setX(mx * blockSize);
        missingView.setY(my * blockSize);
        solution.getChildren().add(missingView);

        Image tile;
        ImageView tileView;
        for (Tile T : tiles) {
            switch (T.direction) {
                case topLeft:
                    tile = new Image("file:Pics/topLeft.png");
                    break;
                case topRight:
                    tile = new Image("file:Pics/topRight.png");
                    break;
                case bottomLeft:
                    tile = new Image("file:Pics/bottomLeft.png");
                    break;
                case bottomRight:
                    tile = new Image("file:Pics/bottomRight.png");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + T.direction);
            }
            tileView = new ImageView(tile);
            tileView.setPreserveRatio(true);
            tileView.setFitHeight(blockSize * 2);
            tileView.setFitWidth(blockSize * 2);
            tileView.setX(T.left * blockSize);
            tileView.setY(T.top * blockSize);
            solution.getChildren().add(tileView);
        }

        Text tileCount = new Text("Number of tiles: " + tiles.size());
        tileCount.setFont(new Font(20));

        VBox vBox = new VBox(solution, tileCount);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(0.01 * SCREEN_HEIGHT);
        vBox.setPadding(new Insets(0.01 * SCREEN_HEIGHT));

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
