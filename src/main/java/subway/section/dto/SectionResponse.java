package subway.section.dto;

import subway.section.entity.Section;

public class SectionResponse {

    private Long lineId;
    private Section section;

    public SectionResponse(Long lineId, Section section) {
        this.lineId = lineId;
        this.section = section;
    }

    public static SectionResponse of (Long lineId, Section section) {
        return new SectionResponse(lineId, section);

    }

    public Long getLineId() {
        return lineId;
    }
}
