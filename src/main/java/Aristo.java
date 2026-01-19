public class Aristo {
    public static void main(String[] args) {
        greet();
        exit();
    }

    public static void greet() {
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Hello, human!");
        System.out.println("Aristo here and ready to assist.");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public static void exit() {
        System.out.println();
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Goodbye!");
        System.out.println("Aristo awaits your return...");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
    }
}
