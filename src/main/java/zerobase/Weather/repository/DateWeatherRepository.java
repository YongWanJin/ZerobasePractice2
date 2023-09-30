package zerobase.Weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.Weather.domain.DateWeather;

import java.time.LocalDate;
import java.util.List;

@Repository
// JpaRepository<DateWeather, LocalDate>
// 이 레포지토리에 저장할 데이터의 자료형은 DateWeather,
// 이 Entity객체의 pk의 자료형은 LocalDate
public interface DateWeatherRepository extends JpaRepository<DateWeather, LocalDate> {
    List<DateWeather> findAllByDate(LocalDate date);
}
