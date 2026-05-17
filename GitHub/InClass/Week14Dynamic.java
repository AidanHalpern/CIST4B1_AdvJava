public class Week14Dynamic {
    public static void main(String[] args) {
        String one = "abcde";// N
        String two = "ace";// M
        //Time complex would be O(N * M)

        int[][] matrix = new int[one.length() + 1][two.length() + 1];

        for (int i = 1; i <= one.length(); i++) {

            for (int j = 1; j <= two.length(); j++) {
                if (one.charAt(i - 1) == (two.charAt(j - 1))) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                } else {
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i][j - 1]);
                }
            }
        }
        System.out.println(matrix[one.length()][two.length()]);
    }
}
