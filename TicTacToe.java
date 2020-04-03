import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Comparator;

/**
 * TicTacToe (Using DFS for Draw Conditions)
 */
public class TicTacToe {
    private final static int PUZZLE_SIZE = 3;
    private final static int PLAYER_X = 1;
    private final static int PLAYER_O = -1;
    private static boolean HUMAN_VS_HUMAN = false;
    private static boolean HUMAN_VS_COMPUTER = false;
    private static boolean COMPUTER_VS_COMPUTER = false;
    private static boolean LEVEL_HARD = false;
    private int CURRENTLY_PLAYING_PLAYER = 0;
    private int NEXT_PLAYING_PLAYER = 0;
    private int latest_move_x, latest_move_y;
    private int tiles[][];

    TicTacToe() {
        tiles = new int[PUZZLE_SIZE][PUZZLE_SIZE];
    }

    TicTacToe(TicTacToe toClone) {
        this();
        for (int i = 0; i < PUZZLE_SIZE; i++) {
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                tiles[i][j] = toClone.tiles[i][j];
            }
        }
        latest_move_x = toClone.latest_move_x;
        latest_move_y = toClone.latest_move_y;
        CURRENTLY_PLAYING_PLAYER = toClone.CURRENTLY_PLAYING_PLAYER;
        NEXT_PLAYING_PLAYER = toClone.NEXT_PLAYING_PLAYER;
    }

    private class TilePosition {
        public int x;
        public int y;

        TilePosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public class DecendingOrder implements Comparator<Integer> {

        @Override
        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }

    private void movePlayer(int x, int y) {
        int player = CURRENTLY_PLAYING_PLAYER;
        latest_move_x = x;
        latest_move_y = y;
        tiles[latest_move_x][latest_move_y] = player;
        CURRENTLY_PLAYING_PLAYER = NEXT_PLAYING_PLAYER;
        NEXT_PLAYING_PLAYER = player;
    }

    // return all vacant position of tiles
    private List<TilePosition> getAllUnOccupiedTilePosition() {
        ArrayList<TilePosition> tilePositions = new ArrayList<>();
        for (int i = 0; i < PUZZLE_SIZE; i++) {
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                if (tiles[i][j] != PLAYER_O && tiles[i][j] != PLAYER_X) {
                    tilePositions.add(new TilePosition(i, j));
                }
            }
        }
        return tilePositions;
    }

    private TilePosition computerPlaying() {
        HashMap<TilePosition, Integer> moveMapToScore = new HashMap<>();
        TilePosition moveToReturnedByThisMethod = new TilePosition(-1, -1);
        List<TilePosition> allVacantTilePosition = this.getAllUnOccupiedTilePosition();

        // If only one move is left
        if (allVacantTilePosition.size() == 1) {
            return allVacantTilePosition.get(0);
        }

        // If Your enemy is winning in next move then cover him position first
        for (TilePosition tp : allVacantTilePosition) {
            TicTacToe ticTacToe = new TicTacToe(this);
            int temp = ticTacToe.CURRENTLY_PLAYING_PLAYER;
            ticTacToe.CURRENTLY_PLAYING_PLAYER = ticTacToe.NEXT_PLAYING_PLAYER;
            ticTacToe.NEXT_PLAYING_PLAYER = temp;
            ticTacToe.movePlayer(tp.x, tp.y);
            if (ticTacToe.whichPlayersWon() == NEXT_PLAYING_PLAYER) {
                return tp;
            }
        }

        // If Your enemy is NOT winning in next move then play best position For
        // Difficulty Hard, Return any random for Easy
        int bestScore = Integer.MAX_VALUE;
        int randomLoop = (int) Math.random() * allVacantTilePosition.size();
        for (TilePosition move : allVacantTilePosition) {
            TicTacToe ticTacToe = new TicTacToe(this);
            ticTacToe.movePlayer(move.x, move.y);

            int arr[] = ticTacToe.checkUsingBFS();
            // Difference in win count
            int differenceInWinCount_OR_Score = CURRENTLY_PLAYING_PLAYER == PLAYER_O ? arr[0] - arr[1]
                    : arr[1] - arr[0];

            if (!LEVEL_HARD) {
                randomLoop--;
                if (randomLoop == 0) {
                    return move;
                }
            } else {
                if (differenceInWinCount_OR_Score < bestScore) {
                    bestScore = differenceInWinCount_OR_Score;
                }
                moveMapToScore.put(move, differenceInWinCount_OR_Score);
            }
        }

        List<TilePosition> bestMoves = new ArrayList<>();
        for (TilePosition move : allVacantTilePosition) {
            // minimum 0f arr[i]-arr[j] is the best score
            if (moveMapToScore.get(move) == bestScore) {
                bestMoves.add(move);
            }
        }
        moveToReturnedByThisMethod = bestMoves.get((int) (Math.random() * bestMoves.size()));

        return moveToReturnedByThisMethod;
    }

    // Check for the win cases using BFS, Return an array
    // arr[0] is Player_X win count
    // arr[1] is Player_O win count
    // arr[2] is Draw count
    private int[] checkUsingBFS() {
        int player = 0;
        int[] arr = new int[3];
        Queue<TicTacToe> queueTicTacToes = new LinkedList<>();

        queueTicTacToes.add(this);

        while (!queueTicTacToes.isEmpty()) {
            TicTacToe ticTacToe = queueTicTacToes.poll();
            List<TilePosition> allTilePositions = ticTacToe.getAllUnOccupiedTilePosition();

            for (TilePosition tilePosition : allTilePositions) {
                TicTacToe ticTacToeClone = new TicTacToe(ticTacToe);
                ticTacToeClone.movePlayer(tilePosition.x, tilePosition.y);

                player = ticTacToeClone.whichPlayersWon();

                if (player != 0) {
                    if (player == PLAYER_O) {
                        arr[1]++;
                    }
                    if (player == PLAYER_X) {
                        arr[0]++;
                    }
                } else {
                    queueTicTacToes.add(ticTacToeClone);
                }
            }
        }

        arr[2] = (int) Math.pow(2, this.getAllUnOccupiedTilePosition().size()) - arr[0] - arr[1];
        return arr;
    }

    // Check for the win cases using DFS, Return false if game Draw comfirmed
    private boolean checkUsingDFS() {
        boolean toReturn = false;
        List<TilePosition> allTilePositions = this.getAllUnOccupiedTilePosition();

        if (this.whichPlayersWon() != 0) {
            return true;
        }

        for (TilePosition tilePosition : allTilePositions) {
            TicTacToe ticTacToe = new TicTacToe(this);
            ticTacToe.movePlayer(tilePosition.x, tilePosition.y);
            toReturn = ticTacToe.checkUsingDFS();
            if (toReturn) {
                return toReturn;
            }
        }
        return toReturn;
    }

    private int whichPlayersWon() {
        int score[] = new int[4];

        for (int j, i = 0; i < PUZZLE_SIZE; i++) {
            for (j = 0; j < PUZZLE_SIZE; j++) {
                score[0] += tiles[i][j];
                score[1] += tiles[j][i];
            }
            score[2] += tiles[i][i];
            score[3] += tiles[i][j - i - 1];

            if (score[0] == 3 || score[1] == 3) {
                return PLAYER_X;
            }
            if (score[0] == -3 || score[1] == -3) {
                return PLAYER_O;
            }

            score[0] = 0;
            score[1] = 0;
        }

        if (score[2] == 3 || score[3] == 3) {
            return PLAYER_X;
        }
        if (score[2] == -3 || score[3] == -3) {
            return PLAYER_O;
        }

        return 0;
    }

    private boolean isValidMove(int x, int y) {
        if (x >= PUZZLE_SIZE || x < 0) {
            System.out.println("Invalid Move!");
            return false;
        }
        if (y >= PUZZLE_SIZE || y < 0) {
            System.out.println("Invalid Move!");
            return false;
        }
        if (tiles[x][y] == PLAYER_X || tiles[x][y] == PLAYER_O) {
            System.out.println("Place Occupied!");
            return false;
        }
        return true;
    }

    private int playGame() {
        int player = 0, x = 0, y = 0;
        Scanner s = new Scanner(System.in);
        this.makeBoard();

        CURRENTLY_PLAYING_PLAYER = PLAYER_X;
        NEXT_PLAYING_PLAYER = PLAYER_O;

        for (int i = 0; i < PUZZLE_SIZE * PUZZLE_SIZE; i++) {
            if (i % 2 == 0) {
                System.out.print("Player X Turn, Move Now: ");
                player = PLAYER_X;
            } else {
                System.out.print("Player O Turn, Move Now: ");
                player = PLAYER_O;
            }

            if (HUMAN_VS_HUMAN) {
                x = s.nextInt();
                y = s.nextInt();
            } else if (HUMAN_VS_COMPUTER) {
                if (i % 2 == 0) {
                    x = s.nextInt();
                    y = s.nextInt();
                } else {
                    TilePosition tp = this.computerPlaying();
                    x = tp.x;
                    y = tp.y;
                    System.out.print(x + " " + y);
                }
            } else if (COMPUTER_VS_COMPUTER) {
                TilePosition tp = this.computerPlaying();
                x = tp.x;
                y = tp.y;
                System.out.print(x + " " + y);
            }

            if (!isValidMove(x, y)) {
                this.makeBoard();
                i--;
                continue;
            }

            this.movePlayer(x, y);
            this.makeBoard();

            if (i >= 4 && this.whichPlayersWon() != 0) {
                break;
            }

            if (i >= 5) {
                System.out.println("Checking for draw!");
                if (this.checkUsingDFS()) {
                    System.out.println("Still one of them cam win......\n");
                } else {
                    player = 0;
                    break;
                }
            }
        }
        s.close();
        return player;
    }

    // O represented by -1 and X by 1 in tiles matrix
    private void makeBoard() {
        StringBuilder s = new StringBuilder();
        s.append("\n-------------\n");
        for (int i = 0; i < PUZZLE_SIZE; i++) {
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                if (tiles[i][j] == PLAYER_X) {
                    s.append("| X ");
                } else if (tiles[i][j] == PLAYER_O) {
                    s.append("| O ");
                } else {
                    s.append("|   ");
                }
            }
            s.append("|\n-------------\n");
        }
        System.out.println(s.toString());
    }

    public static void main(String[] args) {
        int option = 0;
        int player = 0;

        TicTacToe ticTacToe = new TicTacToe();

        Scanner s = new Scanner(System.in);
        System.out.print(
                "Menu............\n1>Human Vs Human\n2>Human VS Computer\n3>Computer Vs Cpmputer\nChoose Your Option: ");
        option = s.nextInt();

        switch (option) {
            case 1:
                HUMAN_VS_HUMAN = true;
                COMPUTER_VS_COMPUTER = false;
                HUMAN_VS_COMPUTER = false;
                break;
            case 2:
                System.out.print("\nMenu............\n1>Easy\n2>Hard\nChoose Your Option: ");
                LEVEL_HARD = s.nextInt() == 1 ? false : true;
                HUMAN_VS_COMPUTER = true;
                HUMAN_VS_HUMAN = false;
                COMPUTER_VS_COMPUTER = false;
                break;
            default:
                System.out.print("\nMenu............\n1>Easy\n2>Hard\nChoose Your Option: ");
                LEVEL_HARD = s.nextInt() == 1 ? false : true;
                COMPUTER_VS_COMPUTER = true;
                HUMAN_VS_COMPUTER = false;
                HUMAN_VS_HUMAN = false;
        }

        player = ticTacToe.playGame();
        s.close();

        if (player == PLAYER_O) {
            System.out.println("Player O wins!");
            System.out.println("Player O wins!");
        } else if (player == PLAYER_X) {
            System.out.println("Player X wins!");
        } else {
            System.out.println("Game Draw!");
        }
    }
}