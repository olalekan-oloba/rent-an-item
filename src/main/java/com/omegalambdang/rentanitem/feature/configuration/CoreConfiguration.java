package com.omegalambdang.rentanitem.feature.configuration;

import com.omegalambdang.rentanitem.common.audit.AuditListener;
import com.omegalambdang.rentanitem.common.audit.AuditSection;
import com.omegalambdang.rentanitem.common.audit.Auditable;
import com.omegalambdang.rentanitem.common.entity.FellowshipEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.omegalambdang.rentanitem.constants.SchemaConstants.TABLE_CORE_CONFIGURATIONS;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Table(name = TABLE_CORE_CONFIGURATIONS)
@SuppressWarnings("NullAway.Init")
public class CoreConfiguration extends FellowshipEntity<Long, CoreConfiguration> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String configurationName;

    private String value;
    @Column
    private String configurationKey;

    @Nullable
    private String description;

    @Column
    @Nullable
    private String configurationGroup;

    private int sortOrder;

    @Enumerated(EnumType.STRING)
    private CoreConfigrationType configrationType;

    @Embedded
    private AuditSection auditSection = new AuditSection();
}
