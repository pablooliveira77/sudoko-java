package ui.custom.screen;

import java.awt.Dimension;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import service.BoardService;
import ui.custom.button.CheckGameStatusButton;
import ui.custom.button.FinishGameButton;
import ui.custom.button.ResetButton;
import ui.custom.frame.MainFrame;
import ui.custom.panel.MainPanel;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class MainScreen {
    private final static Dimension dimension = new Dimension(600, 600);

    private BoardService boardService;

    private JButton finishGameButton;
    private JButton checkedGameStatusButton;
    private JButton resetButton;

    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
    }

    public void buildMainScreen() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        addResetButton(mainPanel);
        addFinishGameButton(mainPanel);
        addCheckedGameStatusButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void addCheckedGameStatusButton(final JPanel mainPanel) {
        checkedGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getGameStatus();
            String message;
            switch (gameStatus) {
                case NON_STARTED:
                    message = "O jogo ainda não foi iniciado.";
                    break;
                case COMPLETED:
                    message = "O jogo foi completado com sucesso!";
                    break;
                case INCOMPLETE:
                    message = "O jogo está incompleto.";
                    break;
                default:
                    message = "Status do jogo desconhecido.";
                    break;
            }

            message += hasErrors ? " E contém errors." : " E não contém erros.";
            JOptionPane.showMessageDialog(null, message);

        });
        mainPanel.add(checkedGameStatusButton);
    }

    private void addFinishGameButton(final JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e -> {
            if (boardService.isFinished()) {
                JOptionPane.showMessageDialog(null, "Parabéns! Você completou o jogo!");
                resetButton.setEnabled(false);
                finishGameButton.setEnabled(false);
                checkedGameStatusButton.setEnabled(false);
            } else if (boardService.hasErrors()) {
                JOptionPane.showMessageDialog(null, "O jogo possui erros.");
            } else {
                JOptionPane.showMessageDialog(null, "O jogo ainda não foi finalizado.");
            }
        });
        mainPanel.add(finishGameButton);
    }

    private void addResetButton(final JPanel mainPanel) {
        resetButton = new ResetButton(e -> {
            var dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "Deseja realmente resetar o jogo?",
                    "Limpar o Jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE);

            if (dialogResult == 0) {
                boardService.reset();
            }
        });
        mainPanel.add(resetButton);
    }
}
