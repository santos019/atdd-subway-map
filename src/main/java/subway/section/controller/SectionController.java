package subway.section.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.dto.SectionRequest;
import subway.section.dto.SectionResponse;
import subway.section.service.SectionService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/lines/{id}/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity createSection(@PathVariable Long id, @Valid @RequestBody SectionRequest sectionRequest) {
        SectionResponse sectionResponse = sectionService.createSection(id, sectionRequest);

        return ResponseEntity.created(URI.create("/lines/" + sectionResponse.getLineId() + "/sections")).body(sectionResponse);
    }

    @DeleteMapping
    public ResponseEntity deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        sectionService.deleteSection(id, stationId);

        return ResponseEntity.ok().build();
    }

}
