package subway.common.dto;

import subway.common.constant.ErrorCode;

public class ErrorResponse {
    private Integer status;
    private String code;
    private String description;

    public ErrorResponse(ErrorCode collectedErrorResponse) {
        this.status = collectedErrorResponse.getStatus();
        this.code = collectedErrorResponse.getCode();
        this.description = collectedErrorResponse.getDescription();
    }
}
