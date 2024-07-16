package subway.section.entity;

import subway.station.entity.Station;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATION_ID")
    private Station station;

    public Section() {

    }

    public Section (Station station) {
        this.station = station;
    }

    public Station getStation() {
        return this.station;
    }

}
