package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.CreateLineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.RetrieveLineResponse;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;
import subway.section.repository.SectionRepository;
import subway.station.repository.StationRepository;
import subway.station.dto.CreateStationResponse;
import subway.section.entity.Section;
import subway.station.entity.Station;

import java.util.List;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public CreateLineResponse saveStation(final CreateLineRequest createLineRequest) throws Exception {
        Line line = lineRepository.save(createLineRequest.createLineRequestToLine());
        Station upStation = stationRepository.findById(createLineRequest.getUpStationId())
                .orElseThrow(() -> new Exception("upStation is not found."));
        Station downStation = stationRepository.findById(createLineRequest.getDownStationId())
                .orElseThrow(() -> new Exception("downStation is not found."));

        Section upSection = new Section(upStation, line);
        sectionRepository.save(upSection);
        Section downSection = new Section(downStation, line);
        sectionRepository.save(downSection);

        CreateStationResponse upStationResponse = new CreateStationResponse(upStation.getId(), upStation.getName());
        CreateStationResponse downStationResponse = new CreateStationResponse(downStation.getId(), downStation.getName());

        CreateLineResponse createLineResponse = line.lineToCreateLineResponse();
        createLineResponse.addCreateStationResponse(upStationResponse);
        createLineResponse.addCreateStationResponse(downStationResponse);

        return createLineResponse;
    }

    @Transactional
    public RetrieveLineResponse findAllLines() {
        List<Line> lines = lineRepository.findAll();
        RetrieveLineResponse retrieveLineResponse = new RetrieveLineResponse();
        for(Line line : lines) {
            retrieveLineResponse.addCreateLineResponse(buildCreateLineResponse(line));
        }

        return retrieveLineResponse;
    }

    @Transactional
    public CreateLineResponse findLine(final Long id) throws Exception {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new Exception("line is not found."));

        return buildCreateLineResponse(line);
    }

    @Transactional
    public void modifyLine(final Long id, final ModifyLineRequest modifyLineRequest) throws Exception {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new Exception("line is not found."));

        line.changeName(modifyLineRequest.getName());
        line.changeColor(modifyLineRequest.getColor());

        lineRepository.save(line);
    }

    public CreateLineResponse buildCreateLineResponse(final Line line) {
        CreateLineResponse createLineResponse = line.lineToCreateLineResponse();
        List<Section> sections = line.getSections();
        for(Section section : sections) {
            Station station = section.getStation();
            createLineResponse.addCreateStationResponse(new CreateStationResponse(station.getId(), station.getName()));
        }

        return createLineResponse;
    }
}
