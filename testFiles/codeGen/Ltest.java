class Ltest {
    public static void main(String[] args) {
        System.out.println(1);
    }
}

class TestClass1 {
    public int test() {
        A a;
        a = () -> {
            System.out.println(666);
        };
        a.run();
    }
}
    lambda A {
        void run();
        }
