public class Week4 {
    public static void main(String[] args) {
        int[] a = {4, 5, 6, 1};
        System.out.println(partition(a));
    }
    public static int partition(int[] arr){
        int i = -1;
        for(int k  = 0; k <= arr.length-1; k++){
            if(arr[k]>= i){
                i++;
                int  temp = i;
                arr[i] = arr[k];
                arr[k] = temp;
  
            }
        }
        return i;
    } 
}
