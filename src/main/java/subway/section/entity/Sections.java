package subway.section.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "SECTION_ID")
    private List<Section> sections = new ArrayList<>();

    public Sections() {}

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }
}
