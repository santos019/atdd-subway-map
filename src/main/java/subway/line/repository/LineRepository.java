package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subway.line.entity.Line;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Long> {

//    @Query("SELECT DISTINCT l FROM Line l LEFT JOIN FETCH l.section")
//    List<Line> findAllWithSection();
}
