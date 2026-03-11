import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String input;

        System.out.println("\t=====================");
        System.out.println("\t|\\  Python Parser  /|");
        System.out.println("\t=====================");

        while(true) {
            System.out.print("Enter a file to scan (Enter nothing to quit program): ");
            input = scan.nextLine();
            System.out.println();

            if(input.isBlank()) { break; }

            System.out.println(PythonParser.parse(input));
            System.out.println();
        }

        System.out.print("Quitting program...");
    }
}