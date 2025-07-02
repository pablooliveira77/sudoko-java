package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Board;
import model.GameStatusEnum;
import model.Space;

public class BoardService {
    private final static int BOARD_LIMIT = 9;
    private final Board board;

    public BoardService(final Map<String, String> gameConfig) {
        this.board = new Board(initBoard(gameConfig));
    }

    public List<List<Space>> getSpaces() {
        return board.getSpaces();
    }

    public void reset() {
        board.reset();
    }

    public boolean hasErrors() {
        return board.hasErrors();
    }

    public GameStatusEnum getGameStatus() {
        return board.getStatus();
    }

    public boolean isFinished() {
        return board.gameIsFinish();
    }

    private List<List<Space>> initBoard(final Map<String, String> gameConfig) {
        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var positionKey = "%s,%s".formatted(i, j);
                var positionConfig = gameConfig.get(positionKey);
                var configParts = positionConfig.split(",");
                var expected = Integer.parseInt(configParts[0]);
                var fixed = Boolean.parseBoolean(configParts[1]);
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }

        return spaces;

    }
}
