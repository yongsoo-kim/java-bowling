package bowling.domain.frame;

import bowling.domain.point.Point;

import java.util.LinkedList;

public class Frames {
    private static final int TOTAL_NORMALFRAME_COUNT = 9;
    private static final int MAX_FRAME_COUNT = 10;

    private final LinkedList<Frame> frames;

    public Frames() {
        this.frames = new LinkedList<>();
        frames.add(new NormalFrame());
    }

    public LinkedList<Frame> getFrames() {
        return new LinkedList<>(frames);
    }

    public Frame getNextFrame() {
        if (!frames.getLast().isThrowable() && size() != MAX_FRAME_COUNT) {
            makeFrame();
        }
        return frames.getLast();
    }

    private void makeFrame() {
        if (frames.size() == TOTAL_NORMALFRAME_COUNT) {
            frames.addLast(new FinalFrame());
            return;
        }
        frames.addLast(new NormalFrame());
    }

    public void calculateScores(Point point) {
        this.frames.stream()
                .filter(frame -> frame.hasScore())
                .forEach(frame -> frame.calculateScore(point.getPoint()));
        frames.getLast().addScore();
    }

    public int size() {
        return frames.size();
    }

    public Frame getLastFrame() {
        return frames.getLast();
    }

    public boolean isMax() {
        return size() == MAX_FRAME_COUNT;
    }

    public boolean isEndLastFrame() {
        return isMax() && !getLastFrame().isThrowable();
    }
}