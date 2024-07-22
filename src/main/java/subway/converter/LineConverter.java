package subway.converter;

import subway.line.dto.LineResponse;
import subway.line.entity.Line;
import subway.station.dto.StationResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class LineConverter {

    private LineConverter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static LineResponse convertToLineResponseByLine(final Line line) {
        List<StationResponse> stationResponses = line.getSections().getSections().stream()
                .flatMap(section -> section.getStations().stream())
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .distinct()
                .collect(toList());

        return convertToLineResponseByLineAndStations(line, stationResponses);
    }

    public static LineResponse convertToLineResponseByLineAndStations(Line line, List<StationResponse> stationResponses) {
        LineResponse lineResponse = new LineResponse(line.getId(), line.getName(), line.getColor(), line.getDistance());
        lineResponse.addCreateStationResponses(stationResponses);

        return lineResponse;
    }

    public static List<Long> convertToStationIds(Line line) {
        return line.getSections().getSections().stream()
                .flatMap(sectionValue -> sectionValue.getStations().stream())
                .map(station -> station.getId())
                .distinct()
                .collect(toList());
    }

}