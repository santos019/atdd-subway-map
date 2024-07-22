package subway.station.component;

import org.springframework.stereotype.Component;
import subway.station.entity.Station;
import subway.station.exception.StationNotFoundException;
import subway.station.repository.StationRepository;

@Component
public class StationComponent {

    private final StationRepository stationRepository;

    public StationComponent(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station getStationByIdOrThrow(Long stationId){
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException("Station is not found."));
    }
}
