package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.component.LineComponent;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.LinesResponse;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.section.repository.SectionRepository;
import subway.station.component.StationComponent;
import subway.station.dto.StationResponse;
import subway.station.entity.Station;

import java.util.List;

import static subway.converter.LineConverter.convertToLineResponseByLine;
import static subway.converter.LineConverter.convertToLineResponseByLineAndStations;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationComponent stationComponent;
    private final LineComponent lineComponent;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationComponent stationComponent, LineComponent lineComponent) {
        this.lineRepository = lineRepository;
        this.stationComponent = stationComponent;
        this.sectionRepository = sectionRepository;
        this.lineComponent = lineComponent;
    }

    @Transactional
    public LineResponse saveLine(final CreateLineRequest createLineRequest) {
        Station upStation = stationComponent.getStationByIdOrThrow(createLineRequest.getUpStationId());
        Station downStation = stationComponent.getStationByIdOrThrow(createLineRequest.getDownStationId());

        Section section = Section.of(upStation, downStation, createLineRequest.getDistance());

        Sections sections = new Sections();
        sections.addSection(section);

        sections.setLastUpStationId(upStation.getId());
        sections.setLastDownStationId(downStation.getId());

        Line line = Line.of(createLineRequest.getName(), createLineRequest.getColor(), createLineRequest.getDistance(), sections);

        lineRepository.save(line);

        StationResponse upStationResponse = new StationResponse(upStation.getId(), upStation.getName());
        StationResponse downStationResponse = new StationResponse(downStation.getId(), downStation.getName());

        LineResponse lineResponse = convertToLineResponseByLineAndStations(line, List.of(upStationResponse, downStationResponse));

        return lineResponse;
    }

    @Transactional(readOnly = true)
    public LinesResponse findAllLines() {
        List<Line> lines = lineRepository.findAll();
        LinesResponse linesResponse = new LinesResponse();
        for (Line line : lines) {
            linesResponse.addLineResponse(convertToLineResponseByLine(line));
        }

        return linesResponse;
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(final Long id) {
        Line line = lineComponent.getLineByIdOrThrow(id);

        return convertToLineResponseByLine(line);
    }

    @Transactional
    public void modifyLine(final Long id, final ModifyLineRequest modifyLineRequest) {
        Line line = lineComponent.getLineByIdOrThrow(id);
        line.changeName(modifyLineRequest.getName());
        line.changeColor(modifyLineRequest.getColor());

        lineRepository.save(line);
    }

    @Transactional
    public void deleteLine(final Long id) {
        Line line = lineComponent.getLineByIdOrThrow(id);
        lineRepository.delete(line);
    }

}
