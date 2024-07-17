package subway.line.entity;

import subway.section.entity.Sections;

import javax.persistence.*;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "SECTIONS_ID")
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color, Long distance, Sections sections) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.sections = sections;
    }

    public static Line of(String name, String color, Long distance, Sections sections) {
        return new Line(name, color, distance, sections);
    }

    public void changeColor(String color) {
        this.color = color;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getDistance() {
        return distance;
    }

    public Sections getSections() {
        return this.sections;
    }

}
