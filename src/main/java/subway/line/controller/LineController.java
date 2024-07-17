package subway.line.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.CreateLineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.RetrieveLineResponse;
import subway.line.service.LineService;
import subway.station.dto.StationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<CreateLineResponse> createLine(@RequestBody CreateLineRequest createLineRequest) throws Exception {
        CreateLineResponse line = lineService.saveStation(createLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<RetrieveLineResponse> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CreateLineResponse> showLine(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyLine(
            @PathVariable Long id,
            @RequestBody ModifyLineRequest modifyLineRequest) throws Exception {
            lineService.modifyLine(id, modifyLineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(
            @PathVariable Long id) throws Exception {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }
}
