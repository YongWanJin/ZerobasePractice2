package zerobase.Weather.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.Weather.domain.Memo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository  // 이것은 DB와 연동되는 클래스이다.
public class JdbcMemoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcMemoRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        // dataSource : src/main/resources/application.properties에 지정된 정보들이 담긴 객체
        // 객체 dataSource를 주입받아 jdbcTemplate에 넣는다.
    }

    // 텍스트를 DB에 저장 (JDBC 사용)
    public Memo save(Memo memo){
        String sql = "insert into memo values(?,?)"; // jdbc라서 쿼리문 직접 작성
        jdbcTemplate.update(sql, memo.getId(), memo.getText());
        return memo;
    }

    // DB에 있는 텍스트 목록 조회 (JDBC 사용)
    public List<Memo> findAll(){
        String sql = "select * from memo";
        return jdbcTemplate.query(sql, memoRowMapper());

    }
    private RowMapper<Memo> memoRowMapper(){
        return (rs, rowNum) -> new Memo(
                rs.getInt("id"),
                rs.getString("text")
        );
        // ResultSet의 구조
        // {id = 1, text = '오늘의 날씨는 맑았다.'}
        // 이것을 Memo 자료형으로 대응시키는 클래스가 RowMapper<T>임.
    }

    // id값으로 텍스트 조회 (JDBC 사용)
    public Optional<Memo> findById(int id){
        String sql = "select * from memo where id = ?";
        return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
        // .stream().findFirst() : 중복값이 있을 경우를 대비,
        // 매서드 query()는 언제나 리스트를 반환한다.
        // 우리가 원하는 것은 Memo객체 한개. 어차피 id라는 pk값으로 가져왔으니
        // 리스트의 맨 첫번째 값이 우리가 원하는 값임.
        // Optional<T> : 만약 null값이 나왔을때를 대비하여 더 쉽게 처리할 수 있게하는 객체
    }

}
