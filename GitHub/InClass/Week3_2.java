public class Week3_2 {
    public static void main(String[] args) {
        Int[] data = {51, 2, 88 , 74, 73, 5 ,18};
    }


    public static void selectionSort(int[] arr){

        for(int i = 0; i < arr.length-1; i++){
            boolean solvedFlag = false;

            for(int k = i+1; k < arr.length-1; k++){
                if(arr[i] < arr[k]){
                    while(arr[i] < arr[k]){
                        arr[i] = arr[k];
                        i--;
                    }
                }
            }}

            for(int k = i; k > 0; k--){
                if(arr[i] > arr[k]){

                }
                if(arr[i] < arr[k])
                    solvedFlag = true;}
                
            
            
        
    
}
}
