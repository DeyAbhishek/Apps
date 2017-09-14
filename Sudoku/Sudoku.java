package Sudoku;

import java.util.Arrays;

/**
 * Created by abhishek on 9/12/17.
 */
public class Sudoku {
    
    private static Sudoku instance = null; // lazy loading

    private static final int BASE = 3;
    private static final int DIMENSION = 9;
    private static final int NCELLS = 81;

    private boolean finished = false;

    private int[][] board = new int[DIMENSION][DIMENSION];

    private Sudoku() {}

    public static Sudoku getInstance() {
        if (instance == null) {
            instance = new Sudoku();
        }
        return instance;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void solve() {
        backtrack();
    }

    private void backtrack() {
        if (finished) {
            processsolution();
            return;
        }
        Cell nextCell = getNextMostConstrainedCell();
        int x = nextCell.x;
        int y = nextCell.y;
        boolean[] possibleValues = nextCell.posibleValues;
        for (int i = 1; i <= DIMENSION; i++) {
            if(possibleValues[i]) {
            fillBoard(x, y, i);
            backtrack();
            undoFillBoard(x, y);
            if (finished) return;  // early termination since we need only one solution
            }
        }

    }

    private void processsolution() {
        printBoard();
    }

    private void printBoard() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
    }

    private void fillBoard(int x, int y, int value) {
        board[x][y] = value;
    }

    private void undoFillBoard(int x, int y) {
        board[x][y] = 0;
    }

    private Cell getNextMostConstrainedCell() {
        Cell nextCell = new Cell();
        int min = Integer.MAX_VALUE;
        int count = 0;

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                if (board[i][j] == 0) {count++;
                    boolean[] possibleValues = new boolean[DIMENSION + 1];
                    int noOfPossibleValues = possibleValuesForCell(i, j, possibleValues);
                    if (noOfPossibleValues < min) {
                        min = noOfPossibleValues;
                        nextCell.x = i;
                        nextCell.y = j;
                        nextCell.posibleValues = possibleValues;
                    }
                }

            }
        }
        if (count == 1) {
            finished = true;
        }
        return nextCell;
    }

    private int possibleValuesForCell(int x, int y, boolean[] possibleValues) {
        Arrays.fill(possibleValues, true);
        int alreadyFilled = 0;
        for (int i = 0; i < DIMENSION; i++) {
            if (board[i][y] != 0) {
                possibleValues[ board[i][y] ] = false;
                alreadyFilled++;
            }
        }
        for (int j = 0; j < DIMENSION; j++) {
            if (board[x][j] != 0) {
                possibleValues[ board[x][j] ] = false;
                alreadyFilled++;
            }
        }
        int sectorX = BASE * (x / BASE);
        int sectorY = BASE * (y / BASE);

        for (int i = sectorX; i < sectorX + BASE - 1; i++) {
            for (int j = sectorY; j < sectorY + BASE - 1; j++) {
                if (board[i][j] != 0) {
                    alreadyFilled++;
                    possibleValues[board[i][j]] = false;
                }
            }
        }
        return DIMENSION - alreadyFilled;
    }

}
