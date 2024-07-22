package subway.line.component;

import org.springframework.stereotype.Component;
import subway.line.entity.Line;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.LineRepository;

@Component
public class LineComponent {

    private final LineRepository lineRepository;

    public LineComponent(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line getLineByIdOrThrow(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException("line is not found."));
    }

    public Line saveLine(Line line) {
        return lineRepository.save(line);
    }
}
