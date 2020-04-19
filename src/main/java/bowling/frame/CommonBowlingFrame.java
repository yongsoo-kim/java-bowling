package bowling.frame;

import bowling.FrameScore;
import bowling.Pin;
import bowling.Score;
import bowling.framestate.State;
import bowling.framestate.common.Ready;

import java.util.Arrays;
import java.util.Objects;

public class CommonBowlingFrame implements BowlingFrame {

    private State state;
    private BowlingFrame nextFrame;

    private CommonBowlingFrame(final State state, final BowlingFrame nextFrame) {
        this.state = state;
        this.nextFrame = nextFrame;
    }

    public static CommonBowlingFrame newInstance() {
        return new CommonBowlingFrame(Ready.newInstance(), null);
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
        return getScore(state.createFrameScore());
    }

    @Override
    public Score addingUpScore(final FrameScore beforeScore) {
        return getScore(state.addingUpFrameScore(beforeScore));
    }

    private Score getScore(final FrameScore frameScore) {
        if (frameScore.canCalculateSelfScore()) {
            return frameScore.getScore();
        }

        if (Objects.isNull(nextFrame)) {
            return getNowScore(frameScore);
        }

        return nextFrame.addingUpScore(frameScore);
    }

    private Score getNowScore(final FrameScore frameScore) {
        FrameScore nowFrameScore = frameScore.addingUp(Arrays.asList(Score.ofZeroPins(), Score.ofZeroPins()));
        return nowFrameScore.getScore();
    }

    @Override
    public BowlingFrame appendNextFrame(final int frameNumber) {
        if (!Objects.isNull(nextFrame)) {
            throw new IllegalStateException("can not add next frame. It already existed");
        }

        nextFrame = BowlingFrame.newInstance(frameNumber + 1);
        return nextFrame;
    }

    @Override
    public boolean isOver() {
        return state.isOver();
    }

    @Override
    public boolean canCalculateScore() {
        return canGetCalculateScore(state.createFrameScore());
    }

    @Override
    public boolean canCalculateScore(final FrameScore frameScore) {
        return canGetCalculateScore(state.addingUpFrameScore(frameScore));
    }

    private boolean canGetCalculateScore(FrameScore frameScore) {
        if (frameScore.canCalculateSelfScore()) {
            return true;
        }

        if (Objects.isNull(nextFrame)) {
            return false;
        }

        return nextFrame.canCalculateScore(frameScore);
    }


    @Override
    public State getState() {
        return state;
    }

}