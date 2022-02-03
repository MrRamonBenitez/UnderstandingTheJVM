public class JvmComprehension {

    public static void main(String[] args) {
        int i = 1;
        Object o = new Object();
        Integer ii = 2;
        printAll(o, i, ii);
        System.out.println("finished");
    }

    private static void printAll(Object o, int i, Integer ii) {
        Integer uselessVar = 700;
        System.out.println(o.toString() + i + ii);
    }

}