package ui.custom.screen;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showConfirmDialog;
import static service.EventEnum.CLEAR_SPACE;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Space;
import service.BoardService;
import service.NotifierService;
import ui.custom.button.CheckGameStatusButton;
import ui.custom.button.FinishGameButton;
import ui.custom.button.ResetButton;
import ui.custom.frame.MainFrame;
import ui.custom.input.NumberText;
import ui.custom.panel.MainPanel;
import ui.custom.panel.SudokoSector;

public class MainScreen {
    private final static Dimension dimension = new Dimension(601, 800);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton finishGameButton;
    private JButton checkedGameStatusButton;
    private JButton resetButton;

    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        for (int r = 0; r < 9; r += 3) {
            var endRow = r + 2;
            for (int c = 0; c < 9; c += 3) {
                var endCol = c + 2;
                List<Space> spaceSector = getSpaceFromSector(boardService.getSpaces(), r, endRow, c, endCol);
                JPanel sector = generateSection(spaceSector);
                mainPanel.add(sector);
            }
        }
        addResetButton(mainPanel);
        addFinishGameButton(mainPanel);
        addCheckedGameStatusButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Space> getSpaceFromSector(final List<List<Space>> spaces, final int startRow, final int endRow,
            final int startCol, final int endCol) {
        List<Space> spaceSector = new ArrayList<>();
        for (int r = startRow; r <= endRow; r++) {
            for (int c = startCol; c <= endCol; c++) {
                spaceSector.add(spaces.get(r).get(c));
            }
        }
        return spaceSector;
    }

    private JPanel generateSection(final List<Space> spaces) {
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscribe(CLEAR_SPACE, t));
        return new SudokoSector(fields);
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
            showMessageDialog(null, message);

        });
        mainPanel.add(checkedGameStatusButton);
    }

    private void addFinishGameButton(final JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e -> {
            if (boardService.isFinished()) {
                showMessageDialog(null, "Parabéns! Você completou o jogo!");
                resetButton.setEnabled(false);
                finishGameButton.setEnabled(false);
                checkedGameStatusButton.setEnabled(false);
            } else if (boardService.hasErrors()) {
                showMessageDialog(null, "O jogo possui erros.");
            } else {
                showMessageDialog(null, "O jogo ainda não foi finalizado.");
            }
        });
        mainPanel.add(finishGameButton);
    }

    private void addResetButton(final JPanel mainPanel) {
        resetButton = new ResetButton(e -> {
            var dialogResult = showConfirmDialog(
                    null,
                    "Deseja realmente resetar o jogo?",
                    "Limpar o Jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE);

            if (dialogResult == 0) {
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }
        });
        mainPanel.add(resetButton);
    }
}
