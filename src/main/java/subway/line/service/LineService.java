package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.CreateLineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.RetrieveLineResponse;
import subway.line.entity.Line;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.LineRepository;
import subway.section.entity.Section;
import subway.section.repository.SectionRepository;
import subway.station.dto.StationResponse;
import subway.station.entity.Station;
import subway.station.exception.StationNotFoundException;
import subway.station.repository.StationRepository;

import java.util.List;

import static subway.converter.LineConverter.convertToCreateLineResponse;

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

    private Station getStationByIdOrThrow(Long stationId){
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException("Station is not found."));
    }

    private Line getLineByIdOrThrow(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException("line is not found."));
    }

    @Transactional
    public CreateLineResponse saveStation(final CreateLineRequest createLineRequest) throws Exception {
        Station upStation = getStationByIdOrThrow(createLineRequest.getUpStationId());
        Station downStation = getStationByIdOrThrow(createLineRequest.getDownStationId());

        Section upSection = new Section(upStation);
        Section downSection = new Section(downStation);

        Line line = new Line(createLineRequest.getName(), createLineRequest.getColor(), createLineRequest.getDistance(), List.of(upSection, downSection));
        lineRepository.save(line);

        StationResponse upStationResponse = new StationResponse(upStation.getId(), upStation.getName());
        StationResponse downStationResponse = new StationResponse(downStation.getId(), downStation.getName());

        CreateLineResponse createLineResponse = convertToCreateLineResponse(line, List.of(upStationResponse, downStationResponse));

        return createLineResponse;
    }

    @Transactional
    public RetrieveLineResponse findAllLines() {
        List<Line> lines = lineRepository.findAllWithSections();
        RetrieveLineResponse retrieveLineResponse = new RetrieveLineResponse();
        for (Line line : lines) {
            retrieveLineResponse.addCreateLineResponse(buildCreateLineResponse(line));
        }

        return retrieveLineResponse;
    }

    @Transactional
    public CreateLineResponse findLine(final Long id) {
        Line line = getLineByIdOrThrow(id);

        return buildCreateLineResponse(line);
    }

    @Transactional
    public void modifyLine(final Long id, final ModifyLineRequest modifyLineRequest) {
        Line line = getLineByIdOrThrow(id);
        line.changeName(modifyLineRequest.getName());
        line.changeColor(modifyLineRequest.getColor());

        lineRepository.save(line);
    }

    @Transactional
    public void deleteLine(final Long id) {
        Line line = getLineByIdOrThrow(id);
        lineRepository.delete(line);
    }

    public CreateLineResponse buildCreateLineResponse(final Line line) {
        CreateLineResponse createLineResponse = line.lineToCreateLineResponse();
        List<Section> sections = line.getSections();
        for (Section section : sections) {
            Station station = section.getStation();
            createLineResponse.addCreateStationResponse(new StationResponse(station.getId(), station.getName()));
        }

        return createLineResponse;
    }
}
