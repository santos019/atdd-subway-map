package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    @DisplayName("2개의 지하철역을 생성한 다음, 2개의 지하철역을 조회한다.")
    @Test
    void showStation() {
        // given
        List <String> createdStationNameList = List.of("강남역", "역삼역");

        for(int i = 0; i < createdStationNameList.size(); i ++) {

            Map<String, String> createdStationParams = new HashMap<>();
            createdStationParams.put("name", createdStationNameList.get(i));

            ExtractableResponse<Response> createdStationResponse =
                    RestAssured.given().log().all()
                            .body(createdStationParams)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/stations")
                            .then().log().all()
                            .extract();

            assertThat(createdStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        }

        // when
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames.size()).isEqualTo(createdStationNameList.size());
        assertThat(stationNames).containsAll(createdStationNameList);
    }

    @DisplayName("지하철역을 생성한 다음, 해당 지하철역을 삭제한 뒤, 지하철역 목록을 조회하여 삭제된 것을 확인한다.")
    @Test
    void deleteStation() {

        // given
        List <String> createdStationNameList = new ArrayList<>(List.of("강남역", "역삼역"));
        List <Long> createdStationIdList = new ArrayList<>();

        for(int i = 0; i < createdStationNameList.size(); i ++) {

            Map<String, String> createdStationParams = new HashMap<>();
            createdStationParams.put("name", createdStationNameList.get(i));

            ExtractableResponse<Response> createdStationResponse =
                    RestAssured.given().log().all()
                            .body(createdStationParams)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/stations")
                            .then().log().all()
                            .extract();

            assertThat(createdStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            createdStationIdList.add(createdStationResponse.body().jsonPath().getLong("id"));
        }

        List<String> stationNamesAfterCreation =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        assertThat(stationNamesAfterCreation.size()).isEqualTo(createdStationNameList.size());
        assertThat(stationNamesAfterCreation.size()).isEqualTo(createdStationIdList.size());
        assertThat(stationNamesAfterCreation).containsAll(createdStationNameList);

        // when
        for(int j = 0; j < createdStationIdList.size(); j++) {

            ExtractableResponse<Response> deletedStationResponse =
                    RestAssured.given().log().all()
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().delete("/stations/" + createdStationIdList.get(j))
                            .then().log().all()
                            .extract();

            assertThat(deletedStationResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        }

        // then
        List<String> stationNamesAfterDeletion =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        assertThat(stationNamesAfterDeletion).doesNotContainAnyElementsOf(createdStationNameList);

    }

}