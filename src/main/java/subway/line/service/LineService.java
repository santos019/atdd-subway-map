package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.LinesResponse;
import subway.line.entity.Line;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.LineRepository;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.section.repository.SectionRepository;
import subway.station.dto.StationResponse;
import subway.station.entity.Station;
import subway.station.exception.StationNotFoundException;
import subway.station.repository.StationRepository;

import java.util.List;

import static subway.converter.LineConverter.convertToLineResponseByLine;
import static subway.converter.LineConverter.convertToLineResponseByLineAndStations;

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
    public LineResponse saveLine(final CreateLineRequest createLineRequest) {
        Station upStation = getStationByIdOrThrow(createLineRequest.getUpStationId());
        Station downStation = getStationByIdOrThrow(createLineRequest.getDownStationId());

        Section upSection = Section.of(upStation);
        Section downSection = Section.of(downStation);

        Line line = Line.of(createLineRequest.getName(), createLineRequest.getColor(), createLineRequest.getDistance(), Sections.of(List.of(upSection, downSection)));

        lineRepository.save(line);

        StationResponse upStationResponse = new StationResponse(upStation.getId(), upStation.getName());
        StationResponse downStationResponse = new StationResponse(downStation.getId(), downStation.getName());

        LineResponse lineResponse = convertToLineResponseByLineAndStations(line, List.of(upStationResponse, downStationResponse));

        return lineResponse;
    }

    @Transactional(readOnly = true)
    public LinesResponse findAllLines() {
        List<Line> lines = lineRepository.findAllWithSections();
        LinesResponse linesResponse = new LinesResponse();
        for (Line line : lines) {
            linesResponse.addLineResponse(convertToLineResponseByLine(line));
        }

        return linesResponse;
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(final Long id) {
        Line line = getLineByIdOrThrow(id);

        return convertToLineResponseByLine(line);
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

    private Station getStationByIdOrThrow(Long stationId){
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException("Station is not found."));
    }

    private Line getLineByIdOrThrow(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException("line is not found."));
    }
}
