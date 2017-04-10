package nl.duco.morsecode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @AUTHOR Duco.
 * Description
 */
public class MorseCode {

    @Getter
    private static final HashMap<Character, String> morseCode;

    static {
        morseCode = new HashMap<Character, String>() {{
            put('a', ".-");
            put('b', "-...");
            put('c', "-.-.");
            put('d', "-..");
            put('e', ".");
            put('f', "..-.");
            put('g', "--.");
            put('h', "....");
            put('i', "..");
            put('j', ".---");
            put('k', "-.-");
            put('l', ".-..");
            put('m', "--");
            put('n', "-.");
            put('o', "---");
            put('p', ".--.");
            put('q', "--.-");
            put('r', ".-.");
            put('s', "...");
            put('t', "-");
            put('u', "..-");
            put('v', "...-");
            put('w', ".--");
            put('x', "-..-");
            put('y', "-.--");
            put('z', "--..");
        }};
    }


    @Getter
    @Setter
    private String message;

    public MorseCode(String message) {
        this.message = message;
    }

    public String encode(boolean playSound) {
        String text = message;
        StringBuilder stringBuilder = new StringBuilder();

        text = text.toLowerCase();
        String[] words = text.split("");

        if (!(words.length > 0)) {
            return "";
        }

        Arrays.stream(words).forEach(letter -> {
            if (letter.equals(" ")) {
                stringBuilder.append("/ ");
                if (playSound) playSound(MorseType.SPACE);
                return;
            }

            char letterChar = letter.toCharArray()[0];
            if (getMorseCode().containsKey(letterChar)) {
                stringBuilder.append(getMorseCode().get(letterChar)).append(" ");
                if (!playSound) {
                    return;
                }


                Arrays.stream(getMorseCode().get(letterChar).split("")).forEach(morse -> {
                    switch (morse) {
                        case ".":
                            playSound(MorseType.DOT);
                            break;
                        case "-":
                            playSound(MorseType.STRIPE);
                            break;
                    }
                });
            }
        });

        return stringBuilder.toString().trim();
    }

    public String decode() {
        String morseCode = message;
        StringBuilder stringBuilder = new StringBuilder();

        morseCode = morseCode.toLowerCase();
        String[] words = morseCode.split("/");

        if (!(words.length > 0)) {
            return "";
        }

        Arrays.stream(words).forEach(word -> {

            Arrays.stream(word.split(" ")).forEach(morse -> {

                if (!getMorseCode().values().contains(morse)) {
                    return;
                }

                stringBuilder.append(getKeyFromValue(morse));
            });

            stringBuilder.append(" ");
        });

        return stringBuilder.toString().trim();
    }

    private char getKeyFromValue(String value) {
        return getMorseCode().entrySet().stream().filter(entry -> entry.getValue().equalsIgnoreCase(value)).findFirst().get().getKey();
    }

    private void playSound(MorseType morseType) {
        int sampleRate = 8000;
        try {
            byte[] buf = new byte[1];

            AudioFormat af = new AudioFormat((float) sampleRate, 8, 1, true, false);
            SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
            sdl.open();
            sdl.start();

            for (int i = 0; i < morseType.getDuration() * (float) sampleRate / 1000; i++) {
                if (morseType.equals(MorseType.SPACE)) {
                    sdl.write(buf, 0, 1);
                    continue;
                }

                double angle = i / ((float) sampleRate / 440) * 3.5 * Math.PI;
                buf[0] = (byte) (Math.sin(angle) * 100);
                sdl.write(buf, 0, 1);
            }

            sdl.drain();
            sdl.stop();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Getter
    @AllArgsConstructor
    enum MorseType {
        DOT(150),
        STRIPE(350),
        SPACE(300);

        int duration;
    }
}
