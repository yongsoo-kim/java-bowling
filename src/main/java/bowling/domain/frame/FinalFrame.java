package bowling.domain.frame;

import bowling.domain.PinCount;
import bowling.domain.state.Ready;
import bowling.domain.state.State;

public class FinalFrame implements Frame {

  private final Frame frame;
  private static final int FINAL_PLAY_COUNT = 10;
  private static final int FINAL_FRAME_COUNT = 3;
  private static int count;

  public FinalFrame(State state) {
    this.frame = BaseFrame.of(FINAL_PLAY_COUNT, state);
  }

  public static Frame createWithReady() {
    return FinalFrame.of(new Ready());
  }

  public static FinalFrame of(State state) {
    return new FinalFrame(state);
  }

  @Override
  public Frame next() {
    count++;
    if (getState().isBonus() && count < FINAL_FRAME_COUNT) {
      return createWithReady();
    }
    return FinalFrame.of(getState());
  }

  @Override
  public void play(PinCount pinCount) {
    frame.play(pinCount);
  }

  @Override
  public int getPlayCount() {
    return frame.getPlayCount();
  }

  @Override
  public State getState() {
    return frame.getState();
  }

  @Override
  public boolean isEnd() {
    return frame.getState().isEnd();
  }

}