package subway.line.dto;

import subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class CreateLineResponse {

    private Long id;
    private String name;
    private String color;
    private Long distance;
    private List<StationResponse> stations = new ArrayList<>();

    public CreateLineResponse() {
    }

    public CreateLineResponse(String name, String color, List<StationResponse> stations, Long distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;

    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CreateLineResponse createLineResponse_obj = (CreateLineResponse) obj;

        if (!name.equals(createLineResponse_obj.name)) return false;
        if (!color.equals(createLineResponse_obj.color)) return false;
        if (!distance.equals(createLineResponse_obj.distance)) return false;
        if (stations.size() != (createLineResponse_obj.stations.size())) return false;

        List<StationResponse> stations_obj = createLineResponse_obj.getStations();

        for (int i = 0; i < stations.size(); i++) {
            if (!stations_obj.get(i).getId().equals(stations.get(i).getId())) return false;
            if (!stations_obj.get(i).getName().equals(stations.get(i).getName())) return false;
        }

        return true;
    }

    public CreateLineResponse(Long id, String name, String color, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;

    }

    public void addCreateStationResponse(StationResponse createStationResponse) {
        stations.add(createStationResponse);
    }

    public void addCreateStationResponses(List<StationResponse> stationResponses) {
        for(StationResponse stationResponse : stationResponses) {
            stations.add(stationResponse);
        }
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

    public List<StationResponse> getStations() {
        return this.stations;
    }


}
