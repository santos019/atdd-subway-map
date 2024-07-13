package subway.line.dto;

import subway.station.dto.CreateStationResponse;

import java.util.ArrayList;
import java.util.List;

public class CreateLineResponse {

    private Long id;
    private String name;
    private String color;
    private Long distance;
    private List<CreateStationResponse> stations = new ArrayList<>();

    public CreateLineResponse() {
    }

    public CreateLineResponse(Long id, String name, String color, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;

    }

    public void addCreateStationResponse(CreateStationResponse createStationResponse) {
        stations.add(createStationResponse);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public Long getDistance() {
        return this.distance;
    }

    public List<CreateStationResponse> getStations() {
        return this.stations;
    }


}
