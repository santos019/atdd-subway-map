package subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.CreateLineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.RetrieveLineResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LineStep {

    public static CreateLineResponse 지하철_노선_조회(Long stationIndex) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/" + stationIndex)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.body().as(CreateLineResponse.class);
    }

    public static RetrieveLineResponse 지하철_전체_노선_조회() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.body().as(RetrieveLineResponse.class);
    }

    public static CreateLineResponse 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, Long distance) {
        CreateLineRequest createLineRequest = new CreateLineRequest(name, color, upStationId,
                downStationId, distance);

        ExtractableResponse<Response> createdResponse = RestAssured.given().log().all()
                .body(createLineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return createdResponse.as(CreateLineResponse.class);
    }

    public static void 지하철_노선_수정(Long modifyStationIdx, ModifyLineRequest modifyLineRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(modifyLineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + modifyStationIdx)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    public static void 지하철_노선_삭제(Long deleteStationIdx) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(deleteStationIdx)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + deleteStationIdx)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

}
