class LambdaTest {
    public static void main(String[] args) {
        System.out.println(new Lambdas().test());
    }
}
class Lambdas {
    public int test() {
        DoThing a;
        DoThing b;
        a = () -> 56;
        b = () -> {
            System.out.println(51);
        };
        return 15;
    }
}

lambda DoThing {
    void run();
}
