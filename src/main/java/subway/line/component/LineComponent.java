package subway.line.component;

import org.springframework.stereotype.Component;
import subway.line.entity.Line;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.LineRepository;

import static subway.common.constant.ErrorCode.LINE_NOT_FOUND;

@Component
public class LineComponent {

    private final LineRepository lineRepository;

    public LineComponent(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line getLineByIdOrThrow(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(String.valueOf(LINE_NOT_FOUND)));
    }

    public Line saveLine(Line line) {
        return lineRepository.save(line);
    }
}
