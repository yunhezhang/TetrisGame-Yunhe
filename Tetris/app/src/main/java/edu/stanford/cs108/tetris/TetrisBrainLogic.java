package edu.stanford.cs108.tetris;

/**
 * Created by Yunhe Zhang on 2/6/2017.
 */

public class TetrisBrainLogic extends TetrisLogic {
    private DefaultBrain brain;
    private boolean brainMode;
    public TetrisBrainLogic(TetrisUIInterface uiInterface) {
        super(uiInterface);
        this.brain = new DefaultBrain();
        this.brainMode = false;

    }
    public void setMode(boolean mode) {
        this.brainMode = mode;
    }

    @Override
    protected void tick(int verb) {
        if(this.brainMode && verb == TetrisLogic.DOWN) {
            board.undo();
            Brain.Move bestMove = brain.bestMove(board, currentPiece, HEIGHT, new Brain.Move());
            if(bestMove != null) {
                if(!bestMove.piece.equals(currentPiece)) currentPiece = currentPiece.fastRotation();
            }
            if(bestMove.x > currentX) currentX++;
            else if(bestMove.x < currentX) currentX--;
        }
        super.tick(verb);
    }
}
