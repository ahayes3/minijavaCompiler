//Returning wrong type and mismatched arguement
class TypeTest3 {
    public static void main(String[] args) {
        System.out.println(new Test3().test());
    }
}
class Test3 {
    public Test3 test() {
        int a;
        a = 15;
        return a;
    }
}
