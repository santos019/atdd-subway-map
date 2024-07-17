package subway.converter;

import subway.line.dto.LineResponse;
import subway.line.entity.Line;
import subway.station.dto.StationResponse;
import subway.station.entity.Station;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class LineConverter {

    private LineConverter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static LineResponse convertToLineResponseByLineAndStations(Line line, List<StationResponse> stationResponses) {
        LineResponse lineResponse = new LineResponse(line.getId(), line.getName(), line.getColor(), line.getDistance());
        lineResponse.addCreateStationResponses(stationResponses);

        return lineResponse;
    }

    public static LineResponse convertToLineResponseByLine(final Line line) {
        List<StationResponse> stationResponses = line.getSections().getSections().stream()
                .map(section -> {
                    Station station = section.getStation();
                    return new StationResponse(station.getId(), station.getName());
                }).collect(toList());

        return convertToLineResponseByLineAndStations(line, stationResponses);
    }


}