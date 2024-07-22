package subway.section.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.component.LineComponent;
import subway.line.entity.Line;
import subway.section.dto.SectionRequest;
import subway.section.dto.SectionResponse;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.section.exception.SectionException;
import subway.section.repository.SectionRepository;
import subway.station.component.StationComponent;
import subway.station.entity.Station;

import java.util.List;

import static subway.common.constant.ErrorCode.SECTION_NOT_MATCH;
import static subway.converter.LineConverter.convertToStationIds;

@Service
public class SectionService {

    private final StationComponent stationComponent;
    private final LineComponent lineComponent;

    public SectionService(StationComponent stationComponent, LineComponent lineComponent) {
        this.stationComponent = stationComponent;
        this.lineComponent = lineComponent;
    }

    @Transactional
    public SectionResponse createSection(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationComponent.getStationByIdOrThrow(sectionRequest.getUpStationId());
        Station downStation = stationComponent.getStationByIdOrThrow(sectionRequest.getDownStationId());
        Section section = Section.of(upStation, downStation, sectionRequest.getDistance());

        Line line = lineComponent.getLineByIdOrThrow(lineId);
        Sections sections = line.getSections();

        if (sections.getLastDownStationId() != upStation.getId()) {
            throw new SectionException(String.valueOf(SECTION_NOT_MATCH));
        }

        List<Long> sectionsStation = convertToStationIds(line);

        if (sectionsStation.contains(downStation.getId())) {
            throw new SectionException("This descending station already exists. ");
        }

        sections.addSection(section);
        lineComponent.saveLine(line);

        return new SectionResponse(lineId, section);
    }


}
