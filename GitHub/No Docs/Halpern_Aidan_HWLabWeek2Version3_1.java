/*
 * Author: Aidan Halpern
 * Date: 2026-2-8
 * Course: CIST004B1 - Java Data Structures
 * Homework: #Lab Week 2
 * Description: Creates a 2d maze and, if possible, solves it.
 */

public class Halpern_Aidan_HWLabWeek2Version3_1 {
    public static void main(String[] args) {
        boolean useRandom2DMaze = false;
        int n = 10;
        int[][] maze = new int[n][n];
        if (useRandom2DMaze) {
            for (int i = 0; i < n; i++)
                for (int k = 0; k < n; k++)
                    maze[i][k] = (int) (Math.random() + .5);
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
                    { 0, 0, 0, 0, 0, 1, 0, 0, 1, 0 },
                    { 1, 0, 0, 1, 1, 0, 1, 1, 1, 1 }

            };
            maze = temp;
        }

        System.out.println("Orignal Maze");
        for (int i = 0; i < maze.length; i++) {
            System.out.println();
            for (int data : maze[i])
                System.out.print(data + " ");
        }

        System.out.println("\n");
        if (mazeRecurion(maze)) {
            System.out.println("Solution found");
            for (int i = 0; i < maze.length; i++) {
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

    public static boolean mazeRecurion(int[][] maze) {
        return mazeRecurion(maze, 0, 0);
    }

    /*
     * Base Cases:
     * Index out of bounds
     * Found exit
     * Entrance blocked
     * Exit blocked
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
    public static boolean mazeRecurion(int[][] maze, int xPos, int yPos) {

        /* Base Cases */
        if (yPos < 0 || xPos < 0 || yPos > maze.length - 1 || xPos > maze[maze.length - 1].length - 1)
            return false;

        if (xPos == maze[maze.length - 1].length - 1 && yPos == maze.length - 1) {
            maze[yPos][xPos] = 3;
            return true;
        }

        if (maze[0][0] == 1) {
            System.out.println("Entrance is blocked");
            return false;
        }

        if (maze[maze.length - 1][maze[maze.length - 1].length - 1] == 1) {
            System.out.println("Exit is blocked");
            return false;
        }

        if (maze[yPos][xPos] != 0)
            return false;

        /* BreadCrumb dispenser */
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