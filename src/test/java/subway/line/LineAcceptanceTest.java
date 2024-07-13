package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.CreateLineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.RetrieveLineResponse;
import subway.station.dto.CreateStationResponse;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @DisplayName("지하철 노선을 생성한다")
    @Test
    public void createLine() {
        // given
        List<CreateStationResponse> 지하철_역_목록 = 여러_개의_지하철_역_생성(List.of("강남역", "역삼역"));

        CreateLineResponse 생성된_지하철_노선 = 지하철_노선_생성("신분당선", "Red", 지하철_역_목록.get(0).getId(), 지하철_역_목록.get(1).getId(), 10L);
        생성된_지하철_노선.getStations().sort(Comparator.comparingLong(CreateStationResponse::getId));

        assertThat(생성된_지하철_노선.getColor()).isEqualTo("Red");
        assertThat(생성된_지하철_노선.getDistance()).isEqualTo(10L);
        assertThat(생성된_지하철_노선.getStations().size()).isEqualTo(2);

        for (int i = 0; i < 생성된_지하철_노선.getStations().size(); i++) {
            assertThat(생성된_지하철_노선.getStations().get(i).getId()).isEqualTo(지하철_역_목록.get(i).getId());
            assertThat(생성된_지하철_노선.getStations().get(i).getName()).isEqualTo(지하철_역_목록.get(i).getName());
        }

    }

    @DisplayName("전체 지하철 노선을 조회한다")
    public void retrieveAllLine() {
        List<CreateStationResponse> 생성된_지하철_역_목록 = 여러_개의_지하철_역_생성(List.of("강남역", "역삼역", "선릉역", "논현역"));
        RetrieveLineResponse 비교할_지하철_노선_목록 = new RetrieveLineResponse();
        CreateLineResponse 지하철_노선_1 = 지하철_노선_생성("신분당선", "Red", 생성된_지하철_역_목록.get(0).getId(), 생성된_지하철_역_목록.get(1).getId(), 10L);
        비교할_지하철_노선_목록.getCreateLineResponseList().add(지하철_노선_1);
        List<CreateStationResponse> 지하철_노선_1에_등록된_지하철_역_목록 = 지하철_노선_1.getStations();
        지하철_노선_1에_등록된_지하철_역_목록.sort(Comparator.comparingLong(CreateStationResponse::getId));

        CreateLineResponse 지하철_노선_2 = 지하철_노선_생성("분당선", "Red", 생성된_지하철_역_목록.get(2).getId(), 생성된_지하철_역_목록.get(3).getId(), 20L);
        List<CreateStationResponse> 지하철_노선_2에_등록된_지하철_역_목록 = 지하철_노선_2.getStations();
        지하철_노선_2에_등록된_지하철_역_목록.sort(Comparator.comparingLong(CreateStationResponse::getId));
        비교할_지하철_노선_목록.getCreateLineResponseList().add(지하철_노선_2);

        RetrieveLineResponse 지하철_노선_목록 = 지하철_전체_노선_조회();
        지하철_노선_목록.getCreateLineResponseList().sort(Comparator.comparingLong(CreateLineResponse::getId));

        assertThat(지하철_노선_목록.getCreateLineResponseList().size()).isEqualTo(2);
        for (int i = 0; i < 비교할_지하철_노선_목록.getCreateLineResponseList().size(); i++) {
            CreateLineResponse 비교할_지하철_노선 = 비교할_지하철_노선_목록.getCreateLineResponseList().get(i);
            CreateLineResponse 지하철_노선 = 지하철_노선_목록.getCreateLineResponseList().get(i);

            assertThat(비교할_지하철_노선.getId()).isEqualTo(지하철_노선.getId());
            assertThat(비교할_지하철_노선.getName()).isEqualTo(지하철_노선.getName());
            assertThat(비교할_지하철_노선.getColor()).isEqualTo(지하철_노선.getColor());
            assertThat(비교할_지하철_노선.getDistance()).isEqualTo(지하철_노선.getDistance());

            for (int j = 0; j < 비교할_지하철_노선.getStations().size(); j++) {
                CreateStationResponse 비교할_지하철_역 = 비교할_지하철_노선.getStations().get(j);
                CreateStationResponse 지하철_역 = 지하철_노선.getStations().get(j);
                assertThat(비교할_지하철_역.getId()).isEqualTo(지하철_역.getId());
                assertThat(비교할_지하철_역.getName()).isEqualTo(지하철_역.getName());
            }
        }

    }

    @DisplayName("지하철 노선을 조회한다")
    public void retrieveLine() {
        List<CreateStationResponse> 생성된_지하철_역_목록 = 여러_개의_지하철_역_생성(List.of("강남역", "역삼역", "선릉역", "논현역"));
        RetrieveLineResponse 비교할_지하철_노선_목록 = new RetrieveLineResponse();
        CreateLineResponse 지하철_노선_1 = 지하철_노선_생성("신분당선", "Red", 생성된_지하철_역_목록.get(0).getId(), 생성된_지하철_역_목록.get(1).getId(), 10L);
        비교할_지하철_노선_목록.getCreateLineResponseList().add(지하철_노선_1);
        List<CreateStationResponse> 지하철_노선_1에_등록된_지하철_역_목록 = 지하철_노선_1.getStations();
        지하철_노선_1에_등록된_지하철_역_목록.sort(Comparator.comparingLong(CreateStationResponse::getId));

        CreateLineResponse 지하철_노선 = 지하철_노선_조회(지하철_노선_1.getId());

        CreateLineResponse 비교할_지하철_노선 = 비교할_지하철_노선_목록.getCreateLineResponseList().get(0);

        assertThat(비교할_지하철_노선.getId()).isEqualTo(지하철_노선.getId());
        assertThat(비교할_지하철_노선.getName()).isEqualTo(지하철_노선.getName());
        assertThat(비교할_지하철_노선.getColor()).isEqualTo(지하철_노선.getColor());
        assertThat(비교할_지하철_노선.getDistance()).isEqualTo(지하철_노선.getDistance());

        for (int j = 0; j < 비교할_지하철_노선.getStations().size(); j++) {
            CreateStationResponse 비교할_지하철_역 = 비교할_지하철_노선.getStations().get(j);
            CreateStationResponse 지하철_역 = 지하철_노선.getStations().get(j);
            assertThat(비교할_지하철_역.getId()).isEqualTo(지하철_역.getId());
            assertThat(비교할_지하철_역.getName()).isEqualTo(지하철_역.getName());
        }


    }

    @DisplayName("지하철 노선을 수정한다")
    public void modifyStation() {
        List<CreateStationResponse> 생성된_지하철_역_목록 = 여러_개의_지하철_역_생성(List.of("강남역", "역삼역", "선릉역", "논현역"));
        RetrieveLineResponse 비교할_지하철_노선_목록 = new RetrieveLineResponse();
        CreateLineResponse 지하철_노선_1 = 지하철_노선_생성("신분당선", "Red", 생성된_지하철_역_목록.get(0).getId(), 생성된_지하철_역_목록.get(1).getId(), 10L);
        비교할_지하철_노선_목록.getCreateLineResponseList().add(지하철_노선_1);
        List<CreateStationResponse> 지하철_노선_1에_등록된_지하철_역_목록 = 지하철_노선_1.getStations();
        지하철_노선_1에_등록된_지하철_역_목록.sort(Comparator.comparingLong(CreateStationResponse::getId));

        ModifyLineRequest 지하철_노선_변경_요청 = new ModifyLineRequest("바꾼_신분당선", "Blue");

        지하철_노선_수정(지하철_노선_1.getId(), 지하철_노선_변경_요청);
        CreateLineResponse 변경된_지하철_노선 = 지하철_노선_조회(지하철_노선_1.getId());

        assertThat(지하철_노선_변경_요청.getName()).isEqualTo(변경된_지하철_노선.getName());
        assertThat(지하철_노선_변경_요청.getColor()).isEqualTo(변경된_지하철_노선.getColor());

    }

    @DisplayName("지하철 노선을 삭제한다")
    public void deleteStation() {
        List<CreateStationResponse> 생성된_지하철_역_목록 = 여러_개의_지하철_역_생성(List.of("강남역", "역삼역", "선릉역", "논현역"));
        CreateLineResponse 지하철_노선_1 = 지하철_노선_생성("신분당선", "Red", 생성된_지하철_역_목록.get(0).getId(), 생성된_지하철_역_목록.get(1).getId(), 10L);

        지하철_노선_삭제(지하철_노선_1.getId());

        Response 지하철_노선_삭제_응답 = 지하철_노선_조회_Return_Response(지하철_노선_1.getId());
        assertThat(지하철_노선_삭제_응답.getStatusCode()).isEqualTo(204);

    }

    public void 지하철_노선_삭제(Long deleteStationIdx) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(deleteStationIdx)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + deleteStationIdx)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    public void 지하철_노선_수정(Long modifyStationIdx, ModifyLineRequest modifyLineRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(modifyLineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + modifyStationIdx)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    public Response 지하철_노선_조회_Return_Response(Long stationIndex) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + stationIndex)
                .then().log().all()
                .extract().response();

    }

    public CreateLineResponse 지하철_노선_조회(Long stationIndex) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/" + stationIndex)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.body().as(CreateLineResponse.class);
    }

    public RetrieveLineResponse 지하철_전체_노선_조회() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.body().as(RetrieveLineResponse.class);
    }

    public CreateLineResponse 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, Long distance) {
        CreateLineRequest createLineRequest = new CreateLineRequest(name, color, upStationId,
                downStationId, distance);

        ExtractableResponse<Response> createdResponse = RestAssured.given().log().all()
                .body(createLineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/line")
                .then().log().all()
                .extract();

        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return createdResponse.as(CreateLineResponse.class);
    }

    public List<CreateStationResponse> 여러_개의_지하철_역_생성(List<String> stationList) {
        List<CreateStationResponse> createStationResponseList = new ArrayList<>();
        for (String station : stationList) {
            CreateStationResponse createStationResponse = 지하철_노선도_등록(station);
            createStationResponseList.add(createStationResponse);
        }
        createStationResponseList.sort(Comparator.comparingLong(CreateStationResponse::getId));

        return createStationResponseList;
    }

    public CreateStationResponse 지하철_노선도_등록(String stationName) {
        Map<String, String> createdStationParams = new HashMap<>();
        createdStationParams.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(createdStationParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
        Long stationId_response = response.body().jsonPath().getLong("id");
        String stationName_response = response.body().jsonPath().getString("name");

        return new CreateStationResponse(stationId_response, stationName_response);
    }
}
