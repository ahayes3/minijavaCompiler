class LambdaTest {
    public static void main(String[] args) {
        System.out.println(new Lambdas().test());
    }
}
class Lambdas {
    public int test() {
        Lambda a;
        Lambda b;
        a = (int b) -> b+56;
        b = () -> {
            System.out.println(51);
            return 1;
        };
        return 15;
    }
}
