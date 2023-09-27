package zerobase.Weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.Weather.domain.Memo;
import zerobase.Weather.repository.JdbcMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional // 코드를 돌려도 DB에 저장되지 않음. (트랜젝션화)
public class JdbcMemoRepositoryTests { // 원본 클래스명 + Test"s"

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest(){
        // given : 변수가 주어졌을 때
        Memo newMemo = new Memo(2, "this is new memo2222");

        // when : 이러이러한 동작을 수행했을 때
        jdbcMemoRepository.save(newMemo);

        // then : 다음과 같은 결과가 나올 것이다.
        Optional<Memo> result = jdbcMemoRepository.findById(2);
        assertEquals(result.get().getText(), "this is new memo2222");
    }

    @Test
    void findAllMemoTest(){
        // given
        List<Memo> memoList = jdbcMemoRepository.findAll();
        // when
        // then
        assertNotNull(memoList);
    }
}
