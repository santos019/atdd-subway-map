package subway.line.entity;

import subway.line.dto.CreateLineResponse;
import subway.section.entity.Section;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long distance;

    @OneToMany(mappedBy = "line", fetch = FetchType.EAGER)
    private List<Section> sections;

    public Line() {

    }

    public Line(String name, String color, Long distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public CreateLineResponse lineToCreateLineResponse() {
        CreateLineResponse createLineResponse = new CreateLineResponse(id, name, color, distance);
        return createLineResponse;
    }

    public List<Section> getSections() {
        return this.sections;

    }

}
