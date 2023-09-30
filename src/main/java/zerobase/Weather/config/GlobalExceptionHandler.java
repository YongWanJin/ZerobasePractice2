package zerobase.Weather.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// # 예외 처리
// 이 프로젝트의 모든 controller에서 발생하는 예외들을 처리해준다.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // status 400대가 뜨면 해당 예외가 발생하도록 지시
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Exception handleAllException(){
        System.out.println("error from GlobalExceptionHandler");
        return new Exception();
    }
}
