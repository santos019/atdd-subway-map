package subway.section.component;

import org.springframework.stereotype.Component;
import subway.section.entity.Section;
import subway.section.exception.SectionException;
import subway.section.repository.SectionRepository;

import static subway.common.constant.ErrorCode.SECTION_NOT_FOUND;

@Component
public class SectionComponent {

    private final SectionRepository sectionRepository;

    public SectionComponent(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
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

