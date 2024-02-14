package com.omegalambdang.rentanitem.feature.reference.zone;

import com.omegalambdang.rentanitem.common.entity.FellowshipEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.omegalambdang.rentanitem.constants.SchemaConstants.TABLE_ZONES;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TABLE_ZONES)
@SuppressWarnings("NullAway.Init")
public class Zone extends FellowshipEntity<Integer, Zone> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="NAME", nullable = false, length=120)
    private String name;

    @Column(name = "ZONE_CODE", unique = true, nullable = false)
    private String code;

    @Column(name = "ZONE_SUPPORTED",nullable = false)
    private boolean supported = false;

    @Column(name = "COUNTRY_ISO_CODE", nullable = false)
    private String countryIsoCode;

    public Zone(String name, String code) {
        this.setCode(code);
        this.setName(name);
    }

    public Zone(int id) {
        super();
        this.setId(id);
    }
}
