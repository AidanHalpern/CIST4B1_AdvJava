/*
 * Author: Aidan Halpern
 * Date: 2026-2-25
 * Course: CIST004B1 - Java Data Structures
 * Homework: #Week 4
 * Description: Merge Sort
 */

public class Halpern_Aidan_HW_Week4_Merge_Sort {
    public static void main(String[] args) {
        long start = System.nanoTime();
        int n = 5;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        int[] sortedArr = mergeRecurion(arr);

        for (int temp : sortedArr) {
            System.out.print(temp + " ");
        }
        long end = System.nanoTime();
        double seconds = (end - start) / 1000000000.0;
        System.out.println("\nTime in seconds: " + seconds);
        // TODO:
    }

    public static int[] mergeRecurion(int[] arr) {
        int[] leftArr;
        int[] rightArr;
        /* Done Spilting logic */
        if (arr.length <= 1) {
            return arr;
        }
        /* Spilting logic */
        else {
            int breakPoint = arr.length / 2;
            leftArr = new int[breakPoint];
            rightArr = new int[arr.length - breakPoint];
            for (int i = 0; i < breakPoint; i++) {
                leftArr[i] = arr[i];
            }
            for (int i = breakPoint; i < arr.length; i++) {
                rightArr[i - breakPoint] = arr[i];
            }
            leftArr = mergeRecurion(leftArr);
            rightArr = mergeRecurion(rightArr);
        }
        /* Merging logic */
        int[] sortedArr = new int[leftArr.length + rightArr.length];

        int rightCounter = 0;
        int leftCounter = 0;

        while (rightCounter < rightArr.length && leftCounter < leftArr.length) {
            if (leftArr[leftCounter] <= rightArr[rightCounter]) {
                sortedArr[rightCounter + leftCounter] = leftArr[leftCounter];
                leftCounter++;
            } else {
                sortedArr[rightCounter + leftCounter] = rightArr[rightCounter];
                rightCounter++;
            }
        }

        while (rightCounter < rightArr.length) {
            sortedArr[rightCounter + leftCounter] = rightArr[rightCounter];
            rightCounter++;
        }

        while (leftCounter < leftArr.length) {
            sortedArr[rightCounter + leftCounter] = leftArr[leftCounter];
            leftCounter++;
        }

        return sortedArr;
    }

}

/*
 * Format:(Number of index, Time in Seconds)
 * https://www.desmos.com/calculator/ilcl6ummjy
 */

/*
 * Sample Runs:
 * ------------
 * Case 1:
 * 15 15 15 75 97
 * Time in seconds: 0.0079061
 * 
 * Case 2 (edge case):
 * [n/a for now]
 */