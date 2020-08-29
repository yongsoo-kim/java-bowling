# 볼링 게임 점수판
## 진행 방법
* 볼링 게임 점수판 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

# 1단계 - 질문 삭제하기 기능 리팩토링
### 질문 삭제하기 요구사항
* 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
* 로그인 사용자와 질문한 사람이 같은 경우 삭제 가능하다.
* 답변이 없는 경우 삭제가 가능하다.
* 질문자와 답변 글의 모든 답변자가 같은경우 삭제가 가능하다.
* 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
* 질문자와 답변자가 다른경우 답변을 삭제할 수 없다.
* 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory 를 활용해 남긴다.

### 프로그래밍 요구사항
* qna.service.QnaService의 deleteQuestion()는 앞의 질문 삭제 기능을 구현한 코드이다. 
* 이 메소드는 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드가 섞여 있다.
* 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드 에 대해 단위 테스트를 구현한다.
<pre><code>
public class QnAService {
    public void deleteQuestion(User loginUser, long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);
        if (!question.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        List<Answer> answers = question.getAnswers();
        for (Answer answer : answers) {
            if (!answer.isOwner(loginUser)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
        }

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.setDeleted(true);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, questionId, question.getWriter(), LocalDateTime.now()));
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        deleteHistoryService.saveAll(deleteHistories);
    }
}
</code></pre>

### 힌트1
* 객체의 상태 데이터를 꺼내지(get)말고 메시지를 보낸다.
    * 규칙 8: 일급 콜렉션을 쓴다.
    * Question의 List를 일급 콜렉션으로 구현해 본다.
* 규칙 7: 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
    * 인스턴스 변수의 수를 줄이기 위해 도전한다.
### 힌트2
* 테스트하기 쉬운 부분과 테스트하기 어려운 부분을 분리해 테스트 가능한 부분만 단위테스트한다.
![Mission4-Step1-Hint1](https://nextstep-storage.s3.ap-northeast-2.amazonaws.com/2020-04-08T11%3A45%3A28.056legacy_refactoring_1.png) 
![Mission4-Step1-Hint2](https://nextstep-storage.s3.ap-northeast-2.amazonaws.com/2020-04-08T11%3A45%3A34.262legacy_refactoring_2.png)
![Mission4-Step1-Hint3](https://nextstep-storage.s3.ap-northeast-2.amazonaws.com/2020-04-08T11%3A45%3A40.213legacy_refactoring_3.png)

### Step1 리뷰 사항
* [x] [fix01][Answer.java] isOwner 가 있으므로 과도한 method 추출 제거 
* [x] [fix02][Answer.java] makeDeleteHistory 또한 과도한 method 추출 제거
* [x] [fix03][DeleteHistories.java] record(List<DeleteHistory) -> recordAll 로 변경 
* [x] [fix04][DeleteHistories.java] DeleteHistories.emptyHistory() 를 통해 객체를 생성할 수 있게 수정  
 