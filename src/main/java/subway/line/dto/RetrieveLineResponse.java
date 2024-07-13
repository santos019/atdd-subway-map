package subway.line.dto;

import java.util.ArrayList;
import java.util.List;

public class RetrieveLineResponse {

    private List<CreateLineResponse> createLineResponseList = new ArrayList<>();

    public List<CreateLineResponse> getCreateLineResponseList() {
        return this.createLineResponseList;

    }
}
