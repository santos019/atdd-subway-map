package subway.section.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.entity.Line;
import subway.line.service.LineService;
import subway.section.dto.SectionRequest;
import subway.section.dto.SectionResponse;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.section.exception.SectionException;
import subway.section.repository.SectionRepository;
import subway.station.entity.Station;
import subway.station.service.StationService;

import java.util.List;

import static subway.common.constant.ErrorCode.*;
import static subway.converter.LineConverter.convertToStationIds;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StationService stationService;
    private final LineService lineService;

    public SectionService(SectionRepository sectionRepository, StationService stationService, LineService lineService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional
    public SectionResponse createSection(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationService.getStationByIdOrThrow(sectionRequest.getUpStationId());
        Station downStation = stationService.getStationByIdOrThrow(sectionRequest.getDownStationId());
        Section section = Section.of(upStation, downStation, sectionRequest.getDistance());

        Line line = lineService.getLineByIdOrThrow(lineId);
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
        lineService.saveLine(line);

        return new SectionResponse(lineId, section);
    }

    @Transactional
    public void deleteSection(Long lineId, long stationId) {
        Section section = getByDownStationId(stationId);

        Line line = lineService.getLineByIdOrThrow(lineId);
        Sections sections = line.getSections();

        if (sections.getLastDownStationId() != stationId) {
            throw new SectionException(String.valueOf(SECTION_NOT_PERMISSION_NOT_LAST_DESCENDING_STATION));
        }

        if (sections.getSections().size() <= 1) {
            throw new SectionException(String.valueOf(SECTION_NOT_PERMISSION_COUNT_TOO_LOW));
        }

        sections.removeSection(section);
        sections.setLastUpStationId(section.getUpStation().getId());

        deleteSection(section);
        lineService.saveLine(line);
    }

    public Section getByDownStationId(Long stationId) {
        return sectionRepository.findByDownStationId(stationId).orElseThrow(
                () -> new SectionException(String.valueOf(SECTION_NOT_FOUND))
        );
    }

    public void deleteSection(Section section) {
        sectionRepository.delete(section);
    }

}
