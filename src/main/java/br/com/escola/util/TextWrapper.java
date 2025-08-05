package br.com.escola.util;

import org.apache.pdfbox.pdmodel.font.PDFont;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextWrapper {
    private final String text;
    private final PDFont font;
    private final int fontSize;
    private final float pageWidth;

    public TextWrapper(String text, PDFont font, int fontSize, float pageWidth) {
        this.text = text;
        this.font = font;
        this.fontSize = fontSize;
        this.pageWidth = pageWidth;
    }

    public List<String> wrap() throws IOException {
        List<String> result = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() == 0) {
                currentLine.append(word);
            } else {
                String testLine = currentLine + " " + word;
                float size = font.getStringWidth(testLine) / 1000 * fontSize;
                if (size <= pageWidth) {
                    currentLine.append(" ").append(word);
                } else {
                    result.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                }
            }
        }
        if (currentLine.length() > 0) {
            result.add(currentLine.toString());
        }
        return result;
    }
}