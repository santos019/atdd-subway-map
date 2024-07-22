package subway.section.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.component.LineComponent;
import subway.line.entity.Line;
import subway.section.component.SectionComponent;
import subway.section.dto.SectionRequest;
import subway.section.dto.SectionResponse;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.section.exception.SectionException;
import subway.section.repository.SectionRepository;
import subway.station.component.StationComponent;
import subway.station.entity.Station;

import java.util.List;

import static subway.common.constant.ErrorCode.*;
import static subway.converter.LineConverter.convertToStationIds;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StationComponent stationComponent;
    private final LineComponent lineComponent;
    private final SectionComponent sectionComponent;

    public SectionService(SectionRepository sectionRepository, StationComponent stationComponent, LineComponent lineComponent, SectionComponent sectionComponent) {
        this.sectionRepository = sectionRepository;
        this.stationComponent = stationComponent;
        this.lineComponent = lineComponent;
        this.sectionComponent = sectionComponent;
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

        sections.setLastDownStationId(downStation.getId());
        sections.addSection(section);
        lineComponent.saveLine(line);

        return new SectionResponse(lineId, section);
    }

    @Transactional
    public void deleteSection(Long lineId, long stationId) {
        Section section = sectionComponent.getByDownStationId(stationId);

        Line line = lineComponent.getLineByIdOrThrow(lineId);
        Sections sections = line.getSections();

        if (sections.getLastDownStationId() != stationId) {
            throw new SectionException(String.valueOf(SECTION_NOT_PERMISSION_NOT_LAST_DESCENDING_STATION));
        }

        if (sections.getSections().size() <= 1) {
            throw new SectionException(String.valueOf(SECTION_NOT_PERMISSION_COUNT_TOO_LOW));
        }

        sections.removeSection(section);
        sections.setLastUpStationId(section.getUpStation().getId());

        sectionComponent.deleteSection(section);
        lineComponent.saveLine(line);
    }

}
