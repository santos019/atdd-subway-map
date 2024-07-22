package subway.station.component;

import org.springframework.stereotype.Component;
import subway.station.entity.Station;
import subway.station.exception.StationNotFoundException;
import subway.station.repository.StationRepository;

import static subway.common.constant.ErrorCode.STATION_NOT_FOUND;

@Component
public class StationComponent {

    private final StationRepository stationRepository;

    public StationComponent(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station getStationByIdOrThrow(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(String.valueOf(STATION_NOT_FOUND)));
    }
}
