package zerobase.Weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import zerobase.Weather.domain.Diary;
import zerobase.Weather.error.InvalidDate;
import zerobase.Weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;


// ### Controller : Client와 직접 상호작용하는 부분.
// 무슨 정보를 제공해줄 것인가? 무슨 기능을 사용할 수 있도록 할 것인가?
@RestController
@Tag(name = "Weather Diary API Document", description = "Welecome to Weather Diary API Document")
public class DiaryController {
    // 여기에서 받은 리퀘스트를 DiaryService로 보내겠다.
    private final DiaryService diaryService;
    public DiaryController(DiaryService diaryService){
        this.diaryService = diaryService;
    }

    // # 날씨일기 저장 API
    @PostMapping("/create/diary")
    @Operation(
            summary = "날씨일기 저장 API",
            description = "POST 메서드를 통해 오늘의 날짜(date)는 url로, 일기 내용(text)은 body로 받습니다."
                    + "날짜 입력 형식은 YYYY-MM-DD 입니다."
    )
    void createDiary(
            // 클라이언트의 리퀘스트로 받은 오늘의 날짜(date)와 일기 내용(text)을
            // diaryService의 메서드 createDiary로 전달하겠다.
            // 오늘의 날짜(date)는 url로, 일기 내용(text)는 body로 보내겠다.
            // 날짜 입력 형식은 ISO.DATE로...
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text
    ){
        diaryService.createDiary(date, text);
    }

    // # 하루치 날씨일기 읽기 API
    @GetMapping("/read/diary")
    @Operation(
            summary = "날씨일기 불러오기(하루) API",
            description = "GET메서드를 통해 일기를 불러올 날짜(date)를 YYYY-MM-DD 형식으로 받습니다."
    )
    List<Diary> readDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        return diaryService.readDiary(date);
    }

    // # 기간내 날씨 일기 읽기 API
    // http://localhost:8080/read/diaries?startDate=2022-09-28&endDate=2022-09-29
    @GetMapping("/read/diaries")
    @Operation(
            summary = "날씨일기 불러오기(기간) API",
            description = "조회할 기간의 시작 날짜(startDate)와 마지막 날짜(endDate)를"
                    + "YYYY-MM-DD 형식으로 받습니다."
    )
    List<Diary> readDiaries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ){
        return diaryService.readDiaries(startDate, endDate);
    }

    // # 일기 수정 API (해당 날짜의 맨 첫번째 일기만)
    @PutMapping("/update/diary")
    @Operation(
            summary = "일기 수정 API",
            description = "PUT 메서드를 통해, 선택한 날짜(date)의 <<맨 첫번째>> 일기를 수정합니다."
                    + "날짜는 YYYY-MM-DD 형식으로 받습니다."
    )
    void updateDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text
    ){
        diaryService.updateDiary(date, text);
    }

    // # 일기 삭제 API
    @DeleteMapping("/delete/diary")
    @Operation(
            summary = "일기 삭제 API",
            description = "DELETE 메서드를 통해, 선택한 날짜(date)의 <<모든>> 일기를 삭제합니다."
                    + "날짜는 YYYY-MM-DD 형식으로 받습니다."
    )
    void deleteDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        diaryService.deleteDiary(date);
    }

    // # 예외 처리
    // (DiaryController라는 클래스에서 발생하는 모든 예외를 처리해준다.)
//    @ExceptionHandler(InvalidDate.class)
//    ResponseEntity<ErrorResponse> handleLineException(
//            final InvalidDate.class error){
//    }
    // 이 방법으로는 컴파일 에러 발생. ControllerAdvice 방법 사용할 것.



}
