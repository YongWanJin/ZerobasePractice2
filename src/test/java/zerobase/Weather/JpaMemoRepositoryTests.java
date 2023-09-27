package zerobase.Weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.Weather.domain.Memo;
import zerobase.Weather.repository.JpaMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JpaMemoRepositoryTests {

    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void insertMemoTest(){
        // given
        Memo newMemo = new Memo(1, "NEW NEW NEW!!!!");
        // when
        jpaMemoRepository.save(newMemo); // 메서드 save가 이미 내장되어있다.
        // then
        List<Memo> memoList = jpaMemoRepository.findAll(); // findAll 메서드도 내장
        assertTrue(memoList.size() > 0);
    }

    @Test
    void findByIdFailTest(){
        // given
        Memo newMemo = new Memo(10, "It will fail.");
        // when
        jpaMemoRepository.save(newMemo);
        // then
        Optional<Memo> result = jpaMemoRepository.findById(10);
            // 애초에 id가 10인 것은 없음.
        assertNotEquals(result.get().getId(), 10);
    }

}
