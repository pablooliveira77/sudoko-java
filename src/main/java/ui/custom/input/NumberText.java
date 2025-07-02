package ui.custom.input;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import static java.awt.Font.PLAIN;

import model.Space;

public class NumberText extends JTextField {
    private final Space space;

    public NumberText(final Space space) {
        this.space = space;
        var dimension = new Dimension(50, 50);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setFont(new Font("Arial", PLAIN, 20));
        this.setHorizontalAlignment(JTextField.CENTER);
        this.setDocument(new NumberTextLimit());
        this.setEnabled(!space.isFixed());
        if (space.isFixed()) {
            this.setText(space.getActual().toString());
        }
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                changeSpace();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                changeSpace();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                changeSpace();
            }

            private void changeSpace() {
                if (getText().isEmpty()) {
                    space.clearSpace();
                    return;
                }
                space.setActual(Integer.parseInt(getText()));
            }

        });
    }
}
