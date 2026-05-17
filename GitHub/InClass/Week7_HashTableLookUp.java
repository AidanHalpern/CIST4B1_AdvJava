public class Week7_HashTableLookUp {
     public static Object[] table = new Object[10];
    public static void main(String[] args) {
        table[0] = new Object();
    }

    public Object lookUp(String key){
        int hashKey = (key % table.length);
            while(table[hashKey]!= null){
                if(table[hashKey] == key){
                    return object;
                }
                hashKey++;
            }
            return null;
        }

        public void resize(){
            Object[] newTable = new Object[table.lengh * 2];
            for(int i = 0; i < table.length; i++){
                
            }

            table = newTable;
        }
}
