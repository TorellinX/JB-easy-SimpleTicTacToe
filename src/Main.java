import java.awt.Point;
import java.util.Scanner;

public class Main {
  final static String NOT_FINISHED = "Game not finished";
  final static String DRAW = "Draw";
  final static String X_WINS = "X wins";
  final static String O_WINS = "O wins";
  final static String Impossible = "Impossible";
  private String gameState = NOT_FINISHED;
  private boolean finished = false;
  char[][] field = new char[3][3];

  public static void main(String[] args) throws IllegalAccessException {
    Main game = new Main();
    game.initializeField();
    Scanner scanner = new Scanner(System.in);
    game.printField();


    char nextSymbol = 'X';
    while (!game.finished) {
      String iInput = scanner.next();
      String jInput = scanner.next();
      while (!game.isTurnCorrect(iInput,jInput)) {
        iInput = scanner.next();
        jInput = scanner.next();
      }
      try {
        int i = Integer.parseInt(iInput) - 1;
        int j = Integer.parseInt(jInput) - 1;
        game.field[i][j] = nextSymbol;
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }

      if (nextSymbol == 'X') {
        nextSymbol = 'O';
      } else {
        nextSymbol = 'X';
      }
      game.gameState = game.analyzeGameState();
      game.printField();
    }
    System.out.println(game.gameState);
  }

  private void initializeField() {
    for (int i = 0; i < field.length; i++) {
      for (int j = 0; j < field[0].length; j++) {
        field[i][j] = '_';
      }
    }
  }

  private void printField() {
    System.out.println("---------");
    for (char[] chars : field) {
      System.out.print("| ");
      for (int j = 0; j < field[0].length; j++) {
        System.out.print(chars[j] + " ");
      }
      System.out.println("|");
    }
    System.out.println("---------");
  }

  private String analyzeGameState() throws IllegalAccessException {
    boolean xHasRow = false;
    boolean oHasRow = false;
    int xs = 0;
    int os = 0;
    boolean hasEmptyCells = false;

    for (char[] row : field) {
      for (char cell : row) {
        if (cell == 'X') {
          xs++;
        } else if (cell == 'O') {
          os++;
        } else if (cell == '_') {
          hasEmptyCells = true;
        } else {
          throw new IllegalAccessException("The cell can't be the " + cell + "symbol.");
        }
      }
    }

    Point[][] rowIndices = {
        {new Point(0,0), new Point(0,1), new Point(0,2)},
        {new Point(1,0), new Point(1,1), new Point(1,2)},
        {new Point(2,0), new Point(2,1), new Point(2,2)},
        {new Point(0,0), new Point(1,0), new Point(2,0)},
        {new Point(0,1), new Point(1,1), new Point(2,1)},
        {new Point(0,2), new Point(1,2), new Point(2,2)},
        {new Point(0,0), new Point(1,1), new Point(2,2)},
        {new Point(0,2), new Point(1,1), new Point(2,0)}};

    int rowXs;
    int rowOs;
    for (Point[] rows : rowIndices) {
      rowXs = 0;
      rowOs = 0;
      for (Point rowIndex : rows) {
        if (field[rowIndex.x][rowIndex.y] == 'X') {
          rowXs++;
        }
        if (field[rowIndex.x][rowIndex.y] == 'O') {
          rowOs++;
        }
      }
      if (rowXs == 3) {
        xHasRow = true;
      }
      if (rowOs == 3) {
        oHasRow = true;
      }
    }

    if (Math.abs(xs - os) > 1 || xHasRow && oHasRow) {
      return Impossible;
    }
    if (!xHasRow && !oHasRow && hasEmptyCells) {
      return NOT_FINISHED;
    }
    if (!xHasRow && !oHasRow && !hasEmptyCells) {
      finished = true;
      return DRAW;
    }
    if (xHasRow) {
      finished = true;
      return X_WINS;
    }
    if (oHasRow) {
      finished = true;
      return O_WINS;
    }
    return "Error";
  }

  private boolean isTurnCorrect(String iInput, String jInput) {
    int iTurn;
    int jTurn;
    try {
      iTurn = Integer.parseInt(iInput);
      jTurn = Integer.parseInt(jInput);
    } catch (NumberFormatException e) {
      System.out.println("You should enter numbers!");
      return false;
    }
    if (iTurn > 3 || jTurn > 3 || iTurn < 1 || jTurn < 1) {
      System.out.println("Coordinates should be from 1 to 3!");
      return false;
    }
    if (field[iTurn - 1][jTurn - 1] == 'X' || field[iTurn - 1][jTurn - 1] == 'O') {
      System.out.println("This cell is occupied! Choose another one!");
      return false;
    }
    return true;
  }
}
