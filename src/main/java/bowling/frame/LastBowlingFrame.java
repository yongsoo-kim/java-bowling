package bowling.frame;

import bowling.FrameScore;
import bowling.Pin;
import bowling.Score;
import bowling.framestate.State;
import bowling.framestate.last.ReadyLastFrame;

import java.util.Arrays;

public class LastBowlingFrame implements BowlingFrame {

    public static final int LAST_FRAME_MAX_BOWL_COUNT = 3;

    private State state;

    private LastBowlingFrame(final State state) {
        this.state = state;
    }

    public static LastBowlingFrame newInstance() {
        return new LastBowlingFrame(ReadyLastFrame.newInstance());
    }

    @Override
    public void bowl(final Pin pinCount) {
        state = state.bowl(pinCount);
    }

    @Override
    public Score getTotalScore(final Score beforeScore) {
        return getFrameScore().add(beforeScore);
    }

    @Override
    public Score getFrameScore() {
        FrameScore frameScore = state.createFrameScore();
        if (frameScore.canCalculateSelfScore()) {
            return frameScore.getScore();
        }

        return addingUpScore(frameScore);
    }

    @Override
    public Score addingUpScore(final FrameScore beforeScore) {
        FrameScore addingUpFrameScore = state.addingUpFrameScore(beforeScore);

        if (addingUpFrameScore.canCalculateSelfScore()) {
            return addingUpFrameScore.getScore();
        }

        addingUpFrameScore = beforeScore.addingUp(Arrays.asList(Score.ofZeroPins(), Score.ofZeroPins(), Score.ofZeroPins()));
        return addingUpFrameScore.getScore();
    }

    @Override
    public BowlingFrame appendNextFrame(final int frameNumber) {
        throw new IllegalStateException("It is last frame. can not add next frame");
    }

    @Override
    public boolean isOver() {
        return state.isOver();
    }

    @Override
    public boolean canCalculateScore() {
        FrameScore frameScore = state.createFrameScore();
        return frameScore.canCalculateSelfScore();

    }

    @Override
    public boolean canCalculateScore(FrameScore beforeScore) {
        FrameScore addingUpFrameScore = state.addingUpFrameScore(beforeScore);
        return addingUpFrameScore.canCalculateSelfScore();
    }

    @Override
    public State getState() {
        return state;
    }
}