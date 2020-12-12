class Test1 {
    public static void main(String[]args) {
        System.out.println(1);
    }
}
class TestClass1 {

    int[] number;
    int size;

    public int Init(int sz){
        int j ;
        int k ;
        int aux01 ;
        int aux02 ;

        size = sz ;
        number = new int[sz] ;

        j = 1 ;
        k = size + 1 ;
        while (j < (size)) {
            aux01 = 2 * j ;
            aux02 = k - 3 ;
            number[j] = aux01 + aux02 ;
            j = j + 1 ;
            k = k - 1 ;
        }
        return 0 ;
    }
}
