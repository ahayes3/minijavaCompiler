//assigning to undeclared variable
class TypeTest1 {
    public static void main(String[] args) {
        System.out.println(new Test1().test());
    }
}
class Test1 {
    public int test() {
        a = 15;
        return 15;
    }
}
