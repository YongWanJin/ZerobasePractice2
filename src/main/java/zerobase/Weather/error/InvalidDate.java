package zerobase.Weather.error;

// # 내가 설정한 예외상황
// : 올바르지 않은 날짜 형식으로 리퀘스트했을때
public class InvalidDate extends RuntimeException {
    private static final String MESSAGE = "너무 과거 혹은 미래의 날짜입니다.";
    public InvalidDate(){
        super(MESSAGE);
    }
}
