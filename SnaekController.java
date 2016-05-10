import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Controller for playing the Snaek game
 *
 * @author Shashank Singh
 * @author Nikhil Auradkar
 * @author Severus Snaek
 * @version 1.0
 */
public class SnaekController {
    /**
     * 4 movement directions (UP, DOWN, LEFT, RIGHT)
     */
    private enum Direction {
        U, D, L, R
    }

    private SinglyLinkedList snaek = new SnaekLinkedList();
    private Direction currentDir = Direction.R;

    /**
     * Should move the Snaek by 1 cell either UP, DOWN, LEFT, or RIGHT
     * based on the value of currentDir
     *
     * Should also reset the game if the movement causes the Snaek to
     * overlap itself by calling resetGame()
     *
     * See the HW PDF for details on how this should be implemented
     */
    public void move() {
        Position tailPos = snaek.getEnd();

        //seeing if new coordinate overlaps snaek
        int x = tailPos.getX();
        int y = tailPos.getY();

        if(currentDir == Direction.R) {
            x++;
        } else if(currentDir == Direction.L) {
            x--;
        } else if(currentDir == Direction.U) {
            y++;
        } else {
            y--;
        }

        x = wrapLocation(x);
        y = wrapLocation(y);

        Position newPos = new Position(x, y);
        if(snaek.contains(newPos)) {
            resetGame();
        } else {
            snaek.removeFront();
            snaek.addEnd(newPos);

            return;
            /*SnaekNode tmpNode = snaek.getHead();

            while(tmpNode.getNext() != null) {
                Position nodePos = tmpNode.getPosition();
                System.out.println(nodePos.getX() + " " + nodePos.getY());

                tmpNode = tmpNode.getNext();
            }
            Position nodePos = tmpNode.getPosition();
            System.out.println(nodePos.getX() + " " + nodePos.getY() + "\n");*/

        }
    }

    ////////////////////////////////////////////////////////
    // YOU MAY USE BUT NOT EDIT ANYTHING BELOW THIS POINT //
    ////////////////////////////////////////////////////////

    /**
     * Helper method for rounding a coordinate around either 0 or
     * SnaekFX.GAME_SIZE when a Snaek cell goes out of bounds
     *
     * @param val coordinate to wrap around
     * @return new coordinate that is within the bounds of the screen
     */
    public int wrapLocation(int val) {
        return ((val % SnaekFX.GAME_SIZE) + SnaekFX.GAME_SIZE)
                % SnaekFX.GAME_SIZE;
    }

    /**
     * Resets the game and flashes the screen red
     */
    public void resetGame() {
        snaek.clear();
        pane.getChildren().clear();
        currentDir = Direction.R;

        for (int i = -START_LENGTH / 2; i <= START_LENGTH / 2; ++i) {
            snaek.addEnd(new Position(CENTER + i, CENTER));
        }
        snaek.forEach(p -> pane.getChildren().add(p.getRectangle()));

        if (flashReset != null) {
            flashReset.interrupt();
        }
        flashReset = new Thread(() -> {
                try {
                    for (int i = 0x44; i <= 0xFF; ++i) {
                        final int j = i;
                        Platform.runLater(() -> pane.setBackground(
                                new Background(new BackgroundFill(
                                        Color.web(String
                                            .format("#FF%x%x", j, j)),
                                        CornerRadii.EMPTY, Insets.EMPTY))));
                        Thread.sleep(5);
                    }
                    Platform.runLater(() -> pane.setBackground(
                        Background.EMPTY));
                } catch (InterruptedException e) {
                    Platform.runLater(() -> pane.setBackground(
                        Background.EMPTY));
                }
            });
        flashReset.start();
    }

    private boolean running = true;
    private Pane pane;
    private static final int CENTER = SnaekFX.GAME_SIZE / 2;
    private static final int START_LENGTH = SnaekFX.GAME_SIZE / 2;
    private Thread flashReset;

    /**
     * Constructor for initializing the game controls
     * @param scene scene to attach keyboard listener to
     * @param pane pane to add Snaek cells (Rectangles) to
     */
    public SnaekController(Scene scene, Pane pane) {
        this.pane = pane;

        resetGame();

        (new Thread(() -> {
                while (running) {
                    try {
                        Thread.sleep(SnaekFX.GAME_DELAY);
                        Platform.runLater(this::move);
                    } catch (InterruptedException e) {
                        running = false;
                    }
                }
                Platform.exit();
            })).start();

        scene.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                case UP:
                    currentDir = Direction.U;
                    break;
                case DOWN:
                    currentDir = Direction.D;
                    break;
                case LEFT:
                    currentDir = Direction.L;
                    break;
                case RIGHT:
                    currentDir = Direction.R;
                    break;
                case Q:
                    running = false;
                    break;
                default:
                    break;
                }
            });
    }
}
