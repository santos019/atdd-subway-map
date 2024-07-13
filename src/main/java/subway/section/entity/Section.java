package subway.section.entity;

import subway.line.entity.Line;
import subway.station.entity.Station;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "STATION_ID")
    private Station station;

    @ManyToOne
    @JoinColumn(name = "LINE_ID")
    private Line line;

    public Section() {

    }

    public Section (Station station, Line line) {
        this.station = station;
        this.line = line;
    }


}
