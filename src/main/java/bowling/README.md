# 볼링 점수판 구현

## Public
- Player (플레이어 및 Frames)
  - playerName
  - Frames
  - getCurrentFrameNumber
  - shot
  - isGameSet
  - dto
    - getName
    - FramesDto

## package-private
- Frames (Frame 일급 컬렉션)
  - getCurrentFrameNumber
  - shot
  - isGameSet
  - dto
    - List<FrameDto>
  
- Frame (개별점수 리스트)
  - next
  - last
  - shot
  - isFrameSet
  - dto
    - FrameDto(List<ShotScoreDto>)

- ShotScores (ShotScore 일급 컬렉션)  
    - add
    - isSize
    - isClear
    - dto
        - List<ShotScoreDto>

- ShotScore (개별 점수)
  - next
  - isClear (스페어 또는 스트라이크 처리인지)
  - dto
    - getScoreType
    - getScore