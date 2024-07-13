package subway.station.dto;

public class CreateStationResponse {

    private Long id;
    private String name;

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }


    public CreateStationResponse() {
    }

    public CreateStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;

    }
}
