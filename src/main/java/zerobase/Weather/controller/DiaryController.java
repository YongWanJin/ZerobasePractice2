package zerobase.Weather.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.Weather.domain.Diary;
import zerobase.Weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

// ### Controller : Client와 직접 상호작용하는 부분.
// 무슨 정보를 제공해줄 것인가? 무슨 기능을 사용할 수 있도록 할 것인가?

@RestController
public class DiaryController {
    // 여기에서 받은 리퀘스트를 DiaryService로 보내겠다.
    private final DiaryService diaryService;
    public DiaryController(DiaryService diaryService){
        this.diaryService = diaryService;
    }

    // # 날씨일기 저장 API
    @PostMapping("/create/diary")
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
    List<Diary> readDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        return diaryService.readDiary(date);
    }

    // # 기간내 날씨 일기 읽기 API
    // http://localhost:8080/read/diaries?startDate=2022-09-28&endDate=2022-09-29
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ){
        return diaryService.readDiaries(startDate, endDate);
    }

    // # 일기 수정 API (해당 날짜의 맨 첫번째 일기만)
    @PutMapping("/update/diary")
    void updateDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text
    ){
        diaryService.updateDiary(date, text);
    }

    // # 일기 삭제 API
    @DeleteMapping("/delete/diary")
    void deleteDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        diaryService.deleteDiary(date);
    }



}
