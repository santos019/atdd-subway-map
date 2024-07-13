package subway.station.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateStationRequest {

    @NotNull
    private Long id;
    @NotBlank(message = "name is not empty")
    private String name;
}
