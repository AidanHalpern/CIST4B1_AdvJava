public class quickSort_Week3 {
        public static void main(String[] args) {
        
    }
    
    public static void quickSort(int[] arr, int low, int high){
        //base case, if array is 1 or smaller, return
        if (low >= high){
            return;
        }
        int pivot = arr[high];
        int i = low - 1;
        for(int j = low; j < high; j++){
            if(arr[j] <= pivot){
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;
        int pIndex = i + 1;
        
        quickSort(arr, low, pIndex-1);
        quickSort(arr, pIndex+1, high);
    }
}