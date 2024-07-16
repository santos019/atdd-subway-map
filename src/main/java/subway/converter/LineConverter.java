package subway.converter;

import subway.line.dto.CreateLineResponse;
import subway.line.entity.Line;
import subway.station.dto.StationResponse;

import java.util.List;

public class LineConverter {
    public static CreateLineResponse convertToCreateLineResponse (Line line, List<StationResponse> stationResponses) {
        CreateLineResponse createLineResponse = new CreateLineResponse(line.getId(), line.getName(), line.getColor(), line.getDistance());
        createLineResponse.addCreateStationResponses(stationResponses);

        return createLineResponse;
    }
}
