package subway.common.constant;

public enum ErrorCode {
    STATION_NOT_FOUND(400, "STATION_NOT_FOUND", "Station을 찾을 수 없습니다."),
    LINE_NOT_FOUND(400, "LINE_NOT_FOUND", "Line을 찾을 수 없습니다."),
    ERROR_MESSAGE(499, "ERROR_MESSAGE", "관리자에게 문의하세요.");

    private final int status;
    private final String code;
    private final String description;

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }


    ErrorCode(int status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }

    public static ErrorCode getCollectedErrorResponse(String code) {
        if (code == "LINE_NOT_FOUND") return LINE_NOT_FOUND;
        else if (code == "STATION_NOT_FOUND") return STATION_NOT_FOUND;
        else return ERROR_MESSAGE;
    }
}
