package subway.line.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.CreateLineResponse;
import subway.line.service.LineService;

import java.net.URI;

@RestController
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/line")
    public ResponseEntity<CreateLineResponse> createLine(@RequestBody CreateLineRequest createLineRequest) throws Exception {
        CreateLineResponse line = lineService.saveStation(createLineRequest);
        return ResponseEntity.created(URI.create("/line/" + line.getId())).body(line);
    }

}
