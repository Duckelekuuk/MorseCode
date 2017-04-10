package nl.duco.morsecode;

import java.util.Scanner;

/**
 * @AUTHOR Duco.
 * Description
 */
public class MorseCodeTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MorseCode morseCode = new MorseCode("");

        while(true) {

            System.out.println("Please enter your message: ");
            morseCode.setMessage(scanner.nextLine());

            System.out.println("Do you want to decode or encode: ");

            String decodeInput = scanner.nextLine();

            if (!decodeInput.equalsIgnoreCase("decode") && !decodeInput.equalsIgnoreCase("encode")) {
                return;
            }

            boolean decode = decodeInput.equalsIgnoreCase("decode");

            boolean playsound = false;
            if (!decode) {
                System.out.println("With sound: ");
                String soundInput = scanner.nextLine();

                if (!soundInput.equalsIgnoreCase("yes") && !soundInput.equalsIgnoreCase("no")) {
                    return;
                }
                playsound = soundInput.equalsIgnoreCase("yes");
            }


            System.out.println("Result: " + (decode ? morseCode.decode() : morseCode.encode(playsound)));
            scanner.nextLine();
        }
    }
}
