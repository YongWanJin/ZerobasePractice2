package zerobase.Weather.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.Weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;

@Repository
// 어떤 객체를 저장할건지(Diary), 그리고 pk값의 자료형은 무엇인지
// JpaRepository에 적는다.
public interface DiaryRepository extends JpaRepository<Diary, Integer>{
    // # 하루치 일기 조회
    List<Diary> findAllByDate(LocalDate date);

    // # 기간 범위내 일기 조회
    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    // # 해당 날짜의 일기 수정 (맨 처음에 쓰인것만)
    Diary getFirstByDate(LocalDate date);

    // # 해당 날짜의 일기 삭제
    @Transactional
    Diary deleteAllByDate(LocalDate date);
}
