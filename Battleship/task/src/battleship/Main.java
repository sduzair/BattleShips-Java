package battleship;

import java.io.IOException;
import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.Scanner;

class Battleship {
    public final static String[] BattleShips = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
    private final static int GRID_SIZE = 10;
    char[][] grid = new char[GRID_SIZE][GRID_SIZE];
    char[][] coveredGrid = new char[GRID_SIZE][GRID_SIZE];
    char[] rowCoordinates = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    HashMap<Character, Integer> yCoordinateMap;
    boolean win = false;
    Battleship() {
        yCoordinateMap = new HashMap<>();
        yCoordinateMap.put('A', 0);
        yCoordinateMap.put('B', 1);
        yCoordinateMap.put('C', 2);
        yCoordinateMap.put('D', 3);
        yCoordinateMap.put('E', 4);
        yCoordinateMap.put('F', 5);
        yCoordinateMap.put('G', 6);
        yCoordinateMap.put('H', 7);
        yCoordinateMap.put('I', 8);
        yCoordinateMap.put('J', 9);
        initCoveredGrid();
    }

    private void initCoveredGrid() {
        for(int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                coveredGrid[i][j] = '~';
            }
        }
    }

    public void initGrid() {
        for(int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = '~';
            }
        }
    }
    public void printGrid() {
        System.out.println("- 1 2 3 4 5 6 7 8 9 10");
        for(int i = 0; i < GRID_SIZE; i++) {
            System.out.printf("%s ", rowCoordinates[i]);
            for(int j = 0; j < GRID_SIZE; j++) {
                System.out.printf("%s ",grid[i][j]);
            }
            System.out.print("\n");
        }
    }

    public int[] getShipCoordinates() {

        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("([ \\n])");
        String start = sc.next();
        String end = sc.next();
        int startPosY = yCoordinateMap.get(start.charAt(0));
        int startPosX = Integer.parseInt(start.replaceAll("\\D", "")) - 1;
        int endPosY = yCoordinateMap.get(end.charAt(0));
        int endPosX = Integer.parseInt(end.replaceAll("\\D", "")) - 1;
        return new int[]{startPosX, startPosY, endPosX, endPosY};
    }

    public int[] getHitCoordinates() {

        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("([ \\n])");
        String coordinate = sc.next();
        int coordinatePosY = yCoordinateMap.get(coordinate.charAt(0)) != null ? yCoordinateMap.get(coordinate.charAt(0)) : -1;
        int coordinatePosX = Integer.parseInt(coordinate.replaceAll("\\D", "")) - 1;
//        System.out.printf("%d, %d\n",coordinatePosX, coordinatePosY);
        return new int[]{coordinatePosX, coordinatePosY};
    }

    public void hitCoordinate(int[] coordinate) {
        if(grid[coordinate[1]][coordinate[0]] == 'O' || grid[coordinate[1]][coordinate[0]] == 'X') {
            coveredGrid[coordinate[1]][coordinate[0]] = 'X';
            grid[coordinate[1]][coordinate[0]] = 'X';
            printCoveredGrid();
            if (isGameComplete()) {
                win = true;
                System.out.println("You sank the last ship. You won. Congratulations!");
            }
            else if (isShipSunk(coordinate)) System.out.println("You sank a ship! Specify a new target:");
            else System.out.println("You hit a ship! Try again:");
        } else {
            coveredGrid[coordinate[1]][coordinate[0]] = 'M';
            grid[coordinate[1]][coordinate[0]] = 'M';
            printCoveredGrid();
            System.out.println("You missed! Try again:");
        }
//        printGrid();
    }

    private boolean isGameComplete() {
        boolean isComplete = true;
        for(int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == 'O') isComplete = false;
            }
        }
        return isComplete;
    }

    //checks surrounding cells if they are != '0' which is true if the ship has sunk
    private boolean isShipSunk(int[] coordinate) {
        boolean isSunk = true;
        if (coordinate[0] + 1 <= GRID_SIZE - 1){
            if(grid[coordinate[0] + 1][coordinate[1]] == 'O') isSunk = false;
        }
        if (coordinate[0] - 1 >= 0){
            if(grid[coordinate[0] - 1][coordinate[1]] == 'O') isSunk = false;
        }
        if (coordinate[1] + 1 <= GRID_SIZE - 1){
            if(grid[coordinate[0]][coordinate[1] + 1] == 'O') isSunk = false;
        }
        if (coordinate[1] - 1 >= 0){
            if(grid[coordinate[0]][coordinate[1] - 1] == 'O') isSunk = false;
        }
        return isSunk;
    }

    public void printCoveredGrid() {
        System.out.println("- 1 2 3 4 5 6 7 8 9 10");
        for(int i = 0; i < GRID_SIZE; i++) {
            System.out.printf("%s ", rowCoordinates[i]);
            for(int j = 0; j < GRID_SIZE; j++) {
                System.out.printf("%s ",coveredGrid[i][j]);
            }
            System.out.print("\n");
        }
    }

    public boolean isValidHitCoordinate(int[] coordinate) {
//        System.out.println(Arrays.toString(coordinate));
        if( coordinate[1] == -1 || coordinate[0] < 0 || coordinate[0] >= GRID_SIZE){
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        }
        return true;
    }

    public void placeShip(int[] shipCoordinates) {
        int startPosX = shipCoordinates[0];
        int startPosY = shipCoordinates[1];
        int endPosX = shipCoordinates[2];
        int endPosY = shipCoordinates[3];
        ValueRange xRange;
        ValueRange yRange;

        if (startPosX < endPosX) {
            xRange = ValueRange.of(startPosX, endPosX);
        } else {
            xRange = ValueRange.of(endPosX, startPosX);
        }
        if (startPosY < endPosY) {
            yRange = ValueRange.of(startPosY, endPosY);
        } else {
            yRange = ValueRange.of(endPosY, startPosY);
        }
        for(int y = 0; y < GRID_SIZE; y++) {
            for(int x = 0; x < GRID_SIZE; x++) {
                if (xRange.isValidIntValue(x) && yRange.isValidIntValue(y)) grid[y][x] = 'O';
            }
        }
    }

    public boolean isValidCoordinates(int[] shipCoordinates, int length, int battleShipIndex) {
        int max, min;
        if( shipCoordinates[0] != shipCoordinates[2] && shipCoordinates[1] != shipCoordinates[3]) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        } else if (shipCoordinates[0] == shipCoordinates[2]) {
            if (shipCoordinates[1] < shipCoordinates[3]) {
                min = shipCoordinates[1];
                max = shipCoordinates[3];
            } else {
                min = shipCoordinates[3];
                max = shipCoordinates[1];
            }
            if (Math.abs(shipCoordinates[1] - shipCoordinates[3]) + 1 != length) {
                System.out.printf("Error! Wrong length of the %s! Try again:\n", BattleShips[battleShipIndex]);
                return false;
            } else {
                return !isTooCloseToOtherShipX(max, min, shipCoordinates[0]);
            }
//            shipCoordinates[1] == shipCoordinates[3]
        } else {
            System.out.printf("%d - %d\n", shipCoordinates[0], shipCoordinates[2]);
            if (shipCoordinates[0] < shipCoordinates[2]) {
                min = shipCoordinates[0];
                max = shipCoordinates[2];
            } else {
                min = shipCoordinates[2];
                max = shipCoordinates[0];
            }
            if (Math.abs(shipCoordinates[0] - shipCoordinates[2]) + 1 != length) {
                System.out.printf("Error! Wrong length of the %s! Try again:\n", BattleShips[battleShipIndex]);
                return false;
            } else {
                return !isTooCloseToOtherShipY(max, min, shipCoordinates[1]);
            }
        }
    }

    private boolean isTooCloseToOtherShipX(int max, int min, int constCoordinate) {
        if (min - 1 >= 0){
            if (grid[min - 1][constCoordinate] == 'O') {
                System.out.println("Error! You placed it too close to another one. Try again:");
                return true;
            }
        }
        if (max + 1 < GRID_SIZE){
            if (grid[max + 1][constCoordinate] == 'O') {
                System.out.println("Error! You placed it too close to another one. Try again:");
                return true;
            }
        }
        return false;
    }
    private boolean isTooCloseToOtherShipY(int max, int min, int constCoordinate) {
        if (min - 1 >= 0){
            if (grid[constCoordinate][min - 1] == 'O') {
                System.out.println("Error! You placed it too close to another one. Try again:");
                return true;
            }
        }
        if (max + 1 < GRID_SIZE){
            if (grid[constCoordinate][max + 1] == 'O') {
                System.out.println("Error! You placed it too close to another one. Try again:");
                return true;
            }
        }
        return false;
    }

    public void printEmptyGrid() {
        System.out.println("- 1 2 3 4 5 6 7 8 9 10");
        for(int i = 0; i < GRID_SIZE; i++) {
            System.out.printf("%s ", rowCoordinates[i]);
            for(int j = 0; j < GRID_SIZE; j++) {
                System.out.printf("%s ",'~');
            }
            System.out.print("\n");
        }
    }
}

public class Main {

    public static void main(String[] args) throws IOException {
        // Write your code here
        Battleship battleshipPlayer1 = new Battleship();
        Battleship battleshipPlayer2 = new Battleship();

//        Player 1 grid
        preparePlayer1Grid(battleshipPlayer1);
        System.out.println("Press Enter and pass the move to another player");
        System.in.read();
//        Player 2 grid
        preparePlayer2Grid(battleshipPlayer2);

        //        Game Start
        boolean endGame = false;
        while(!endGame){
            System.out.println("Press Enter and pass the move to another player");
            System.in.read();
            battleshipPlayer2.printCoveredGrid();
            System.out.println("----------------------");
            battleshipPlayer1.printGrid();
            System.out.println("Player 1, it's your turn:");
            int[] hitCoordinate = new int[2];
            while (true) {
                hitCoordinate = battleshipPlayer2.getHitCoordinates();
                if (battleshipPlayer2.isValidHitCoordinate(hitCoordinate)) break;
            }
            battleshipPlayer2.hitCoordinate(hitCoordinate);
            if (battleshipPlayer2.win) break;

            System.out.println("Press Enter and pass the move to another player");
            System.in.read();
            battleshipPlayer1.printCoveredGrid();
            System.out.println("----------------------");
            battleshipPlayer2.printGrid();
            System.out.println("Player 2, it's your turn:");
            while (true) {
                hitCoordinate = battleshipPlayer1.getHitCoordinates();
                if (battleshipPlayer1.isValidHitCoordinate(hitCoordinate)) break;
            }
            battleshipPlayer1.hitCoordinate(hitCoordinate);
            if (battleshipPlayer1.win) break;
        }


    }

    private static void preparePlayer1Grid(Battleship battleshipPlayer1) {
        battleshipPlayer1.initGrid();
//        battleshipPlayer1.printGrid();
        int[] shipCoordinates;
        System.out.println("Player 1, place your ships on the game field");
        battleshipPlayer1.printEmptyGrid();
        //      "Aircraft Carrier"
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", Battleship.BattleShips[0], 5);
        while(true){
            shipCoordinates = battleshipPlayer1.getShipCoordinates();
            if (battleshipPlayer1.isValidCoordinates(shipCoordinates, 5, 0)) break;
        }
        battleshipPlayer1.placeShip(shipCoordinates);
        battleshipPlayer1.printGrid();

        //      "Battleship"
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", Battleship.BattleShips[1], 4);
        while(true){
            shipCoordinates = battleshipPlayer1.getShipCoordinates();
            if (battleshipPlayer1.isValidCoordinates(shipCoordinates, 4, 1)) break;
        }
        battleshipPlayer1.placeShip(shipCoordinates);
        battleshipPlayer1.printGrid();

        //        "Submarine"
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", Battleship.BattleShips[2], 3);
        while(true){
            shipCoordinates = battleshipPlayer1.getShipCoordinates();
            if (battleshipPlayer1.isValidCoordinates(shipCoordinates, 3, 2)) break;
        }
        battleshipPlayer1.placeShip(shipCoordinates);
        battleshipPlayer1.printGrid();

        //        "Cruiser"
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", Battleship.BattleShips[3], 3);
        while(true){
            shipCoordinates = battleshipPlayer1.getShipCoordinates();
            if (battleshipPlayer1.isValidCoordinates(shipCoordinates, 3, 3)) break;
        }
        battleshipPlayer1.placeShip(shipCoordinates);
        battleshipPlayer1.printGrid();

        //        "Destroyer"
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", Battleship.BattleShips[4], 2);
        while(true){
            shipCoordinates = battleshipPlayer1.getShipCoordinates();
            if (battleshipPlayer1.isValidCoordinates(shipCoordinates, 2, 4)) break;
        }
        battleshipPlayer1.placeShip(shipCoordinates);
        battleshipPlayer1.printGrid();
    }

    private static void preparePlayer2Grid(Battleship battleshipPlayer2) {
        int[] shipCoordinates;
        battleshipPlayer2.initGrid();
//        battleshipPlayer2.printGrid();
        System.out.println("Player 2, place your ships on the game field");
        battleshipPlayer2.printEmptyGrid();
        //      "Aircraft Carrier"
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", Battleship.BattleShips[0], 5);
        while(true){
            shipCoordinates = battleshipPlayer2.getShipCoordinates();
            if (battleshipPlayer2.isValidCoordinates(shipCoordinates, 5, 0)) break;
        }
        battleshipPlayer2.placeShip(shipCoordinates);
        battleshipPlayer2.printGrid();

        //      "Battleship"
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", Battleship.BattleShips[1], 4);
        while(true){
            shipCoordinates = battleshipPlayer2.getShipCoordinates();
            if (battleshipPlayer2.isValidCoordinates(shipCoordinates, 4, 1)) break;
        }
        battleshipPlayer2.placeShip(shipCoordinates);
        battleshipPlayer2.printGrid();

        //        "Submarine"
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", Battleship.BattleShips[2], 3);
        while(true){
            shipCoordinates = battleshipPlayer2.getShipCoordinates();
            if (battleshipPlayer2.isValidCoordinates(shipCoordinates, 3, 2)) break;
        }
        battleshipPlayer2.placeShip(shipCoordinates);
        battleshipPlayer2.printGrid();

        //        "Cruiser"
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", Battleship.BattleShips[3], 3);
        while(true){
            shipCoordinates = battleshipPlayer2.getShipCoordinates();
            if (battleshipPlayer2.isValidCoordinates(shipCoordinates, 3, 3)) break;
        }
        battleshipPlayer2.placeShip(shipCoordinates);
        battleshipPlayer2.printGrid();

        //        "Destroyer"
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", Battleship.BattleShips[4], 2);
        while(true){
            shipCoordinates = battleshipPlayer2.getShipCoordinates();
            if (battleshipPlayer2.isValidCoordinates(shipCoordinates, 2, 4)) break;
        }
        battleshipPlayer2.placeShip(shipCoordinates);
        battleshipPlayer2.printGrid();
    }
}
