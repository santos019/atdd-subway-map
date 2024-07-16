package subway.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.line.dto.CreateLineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.RetrieveLineResponse;
import subway.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.util.LineStep.*;
import static subway.util.StationStep.여러_개의_지하철_역_생성;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {


    @DisplayName("지하철 노선을 생성한다")
    @Test
    @Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createLine() {
        // given
        List<StationResponse> 지하철_역_목록 = 여러_개의_지하철_역_생성(List.of("강남역", "역삼역"));

        // when
        CreateLineResponse 원본_지하철_노선 = new CreateLineResponse("신분당선", "Red", List.of(지하철_역_목록.get(0), 지하철_역_목록.get(1)), 10L);
        CreateLineResponse 생성된_지하철_노선 = 지하철_노선_생성("신분당선", "Red", 지하철_역_목록.get(0).getId(), 지하철_역_목록.get(1).getId(), 10L);

        // then
        assertThat(생성된_지하철_노선.equals(원본_지하철_노선)).isTrue();
    }

    @DisplayName("전체 지하철 노선을 조회한다")
    @Test
    @Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void retrieveAllLine() {
        // given
        List<StationResponse> 생성된_지하철_역_목록 = 여러_개의_지하철_역_생성(List.of("강남역", "역삼역", "선릉역", "논현역"));

        RetrieveLineResponse 비교할_지하철_노선_목록 = new RetrieveLineResponse(List.of((지하철_노선_생성("신분당선", "Red", 생성된_지하철_역_목록.get(0).getId(), 생성된_지하철_역_목록.get(1).getId(), 10L)),
                지하철_노선_생성("분당선", "Red", 생성된_지하철_역_목록.get(2).getId(), 생성된_지하철_역_목록.get(3).getId(), 20L)));

        // when
        RetrieveLineResponse 지하철_노선_목록 = 지하철_전체_노선_조회();

        // then
        assertThat(지하철_노선_목록.getCreateLineResponseList().size()).isEqualTo(2);
        for (int i = 0; i < 비교할_지하철_노선_목록.getCreateLineResponseList().size(); i++) {
            CreateLineResponse 비교할_지하철_노선 = 비교할_지하철_노선_목록.getCreateLineResponseList().get(i);
            CreateLineResponse 지하철_노선 = 지하철_노선_목록.getCreateLineResponseList().get(i);

            assertThat(비교할_지하철_노선.equals(지하철_노선)).isTrue();
        }

    }

    @DisplayName("지하철 노선을 조회한다")
    @Test
    @Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void retrieveLine() {
        // when
        List<StationResponse> 생성된_지하철_역_목록 = 여러_개의_지하철_역_생성(List.of("강남역", "역삼역", "선릉역", "논현역"));
        RetrieveLineResponse 비교할_지하철_노선_목록 = new RetrieveLineResponse();
        CreateLineResponse 지하철_노선_1 = 지하철_노선_생성("신분당선", "Red", 생성된_지하철_역_목록.get(0).getId(), 생성된_지하철_역_목록.get(1).getId(), 10L);
        비교할_지하철_노선_목록.getCreateLineResponseList().add(지하철_노선_1);

        // when
        CreateLineResponse 지하철_노선 = 지하철_노선_조회(지하철_노선_1.getId());

        // then
        CreateLineResponse 비교할_지하철_노선 = 비교할_지하철_노선_목록.getCreateLineResponseList().get(0);
        assertThat(비교할_지하철_노선.equals(지하철_노선)).isTrue();

    }

    @DisplayName("지하철 노선을 수정한다")
    @Test
    @Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void modifyStation() {
        // given
        List<StationResponse> 생성된_지하철_역_목록 = 여러_개의_지하철_역_생성(List.of("강남역", "역삼역", "선릉역", "논현역"));
        CreateLineResponse 지하철_노선_1 = 지하철_노선_생성("신분당선", "Red", 생성된_지하철_역_목록.get(0).getId(), 생성된_지하철_역_목록.get(1).getId(), 10L);

        ModifyLineRequest 지하철_노선_변경_요청 = new ModifyLineRequest("바꾼_신분당선", "Blue");

        // when
        지하철_노선_수정(지하철_노선_1.getId(), 지하철_노선_변경_요청);

        // then
        CreateLineResponse 변경된_지하철_노선 = 지하철_노선_조회(지하철_노선_1.getId());
        assertThat(지하철_노선_변경_요청.getName()).isEqualTo(변경된_지하철_노선.getName());
        assertThat(지하철_노선_변경_요청.getColor()).isEqualTo(변경된_지하철_노선.getColor());

    }

    @DisplayName("지하철 노선을 삭제한다")
    @Test
    @Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteStation() {
        // given
        List<StationResponse> 생성된_지하철_역_목록 = 여러_개의_지하철_역_생성(List.of("강남역", "역삼역", "선릉역", "논현역"));
        CreateLineResponse 지하철_노선_1 = 지하철_노선_생성("신분당선", "Red", 생성된_지하철_역_목록.get(0).getId(), 생성된_지하철_역_목록.get(1).getId(), 10L);

        // when & then
        지하철_노선_삭제(지하철_노선_1.getId());
    }

}
