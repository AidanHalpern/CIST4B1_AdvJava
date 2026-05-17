/*
 * Author: Aidan Halpern
 * Date: 2026-2-8
 * Course: CIST004B1 - Java Data Structures
 * Homework: #Lab Week 2
 * Description: Creates a 2d maze and, if possible, solves it.
 */

public class Halpern_Aidan_HWLabWeek2Version3_2 {
    public static void main(String[] args) {
        boolean useRandom2DMaze = true;
        int n = 11;
        int[][] maze = new int[n][n];
        if (useRandom2DMaze) {
            do {
                int[][] temp = new int[n][n];
                for (int i = 0; i < n; i++)
                    for (int k = 0; k < n; k++)
                        temp[i][k] = (int) (Math.random() + .5);
                maze = temp;
            } while (!mazeRecurion(maze, 0, 0));
        } else {
            int[][] temp = {
                    { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0 },
                    { 0, 1, 1, 1, 0, 0, 1, 1, 1, 1 },
                    { 0, 0, 1, 1, 1, 1, 1, 0, 0, 1 },
                    { 1, 0, 0, 1, 1, 1, 1, 1, 1, 1 },
                    { 0, 0, 0, 0, 0, 0, 1, 1, 0, 0 },
                    { 0, 1, 1, 0, 0, 0, 1, 0, 0, 0 },
                    { 1, 0, 1, 0, 1, 0, 0, 0, 1, 0 },
                    { 0, 1, 0, 0, 1, 1, 0, 1, 0, 1 },
                    { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
                    { 1, 0, 0, 1, 1, 0, 1, 1, 0, 0 }

            };
            maze = temp;
        }

        System.out.println("Orignal Maze");
        for (int i = 0; i < n; i++) {
            System.out.println();
            for (int k = 0; k < n; k++) {
                if (maze[i][k] == -1 || maze[i][k] == 3)
                    maze[i][k] = 0;
                System.out.print(maze[i][k] + " ");
            }
        }

        System.out.println("\n");
        if (mazeRecurion(maze)) {
            System.out.println("Solution found");
            for (int i = 0; i < n; i++) {
                System.out.println();
                for (int data : maze[i]) {
                    if (data == -1)
                        data = 0;
                    System.out.print(data + " ");
                }
            }
        } else
            System.out.println("No Solution Found");

        // TODO:
    }

    /*
     * Base Cases in Helper Methood:
     * Entrance blocked
     * Exit blocked
     * 
     * Base Cases:
     * Index out of bounds
     * Found exit
     * Dead end
     * 
     * BreadCrumb dispenser
     * 
     * 
     * Recursive Cases:
     * Up
     * Down
     * Right
     * Left
     */

    public static boolean mazeRecurion(int[][] maze) {
        if (maze[0][0] == 1) {
            System.out.println("Entrance is blocked");
            return false;
        }

        if (maze[maze.length - 1][maze[maze.length - 1].length - 1] == 1) {
            System.out.println("Exit is blocked");
            return false;
        }

        return mazeRecurion(maze, 0, 0);
    }

    public static boolean mazeRecurion(int[][] maze, int xPos, int yPos) {

        /* Base Cases */
        if (yPos < 0 || xPos < 0 || yPos > maze.length - 1 || xPos > maze[maze.length - 1].length - 1)
            return false;

        if (xPos == maze[maze.length - 1].length - 1 && yPos == maze.length - 1) {
            maze[yPos][xPos] = 3;
            return true;
        }

        if (maze[yPos][xPos] != 0)
            return false;

        /* BreadCrum dispenser */
        maze[yPos][xPos] = -1;

        /* Recursive Cases */
        if (mazeRecurion(maze, xPos, yPos - 1)) {
            maze[yPos][xPos] = 3;
            return true;
        }

        if (mazeRecurion(maze, xPos, yPos + 1)) {
            maze[yPos][xPos] = 3;
            return true;
        }

        if (mazeRecurion(maze, xPos + 1, yPos)) {
            maze[yPos][xPos] = 3;
            return true;
        }

        if (mazeRecurion(maze, xPos - 1, yPos)) {
            maze[yPos][xPos] = 3;
            return true;
        }

        return false;

    }
}
/*
 * Sample Runs:
 * ------------
 * Case 1:
 * Orignal Maze
 * 
 * 0 0 0 0 1 1 1 1 0 0
 * 0 1 1 1 0 0 1 1 1 1
 * 0 0 1 1 1 1 1 0 0 1
 * 1 0 0 1 1 1 1 1 1 1
 * 0 0 0 0 0 0 1 1 0 0
 * 0 1 1 0 0 0 1 0 0 0
 * 1 0 1 0 1 0 0 0 1 0
 * 0 1 0 0 1 1 0 1 0 1
 * 0 0 0 0 0 1 0 0 0 0
 * 1 0 0 1 1 0 1 1 0 0
 * 
 * Solution found
 * 
 * 3 0 0 0 1 1 1 1 0 0
 * 3 1 1 1 0 0 1 1 1 1
 * 3 3 1 1 1 1 1 0 0 1
 * 1 3 0 1 1 1 1 1 1 1
 * 0 3 3 3 3 3 1 1 0 0
 * 0 1 1 3 3 3 1 0 0 0
 * 1 0 1 0 1 3 3 0 1 0
 * 0 1 0 0 1 1 3 1 0 1
 * 0 0 0 0 0 1 3 3 3 0
 * 1 0 0 1 1 0 1 1 3 3
 * 
 * Case 2 (edge case):
 * {n/a for now}
 */