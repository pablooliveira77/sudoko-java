package ui.custom.input;

import static java.util.Objects.isNull;

import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;
import javax.swing.text.BadLocationException;

public class NumberTextLimit extends PlainDocument {
    private final List<String> NUMBERS = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    @Override
    public void insertString(int offs, String str, AttributeSet a) {
        if (isNull(str) || NUMBERS.contains(str)) return;

        if (getLength() + str.length() <= 1) {
            try {
                super.insertString(offs, str, a);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
            
    }
}
