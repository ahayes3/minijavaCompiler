//Assigning wrong types to variables
class TypeTest2 {
    public static void main(String[] args) {
        System.out.println(new Test2().test());
    }
}
class Test2 {
    public int test() {
        Test2 a;
        int b;
        a = 15;
        b = new Test2();
        a = b;
        return 15;
    }
}
