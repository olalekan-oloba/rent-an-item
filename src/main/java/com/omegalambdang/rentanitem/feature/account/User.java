package com.omegalambdang.rentanitem.feature.account;

import com.omegalambdang.rentanitem.common.audit.AuditListener;
import com.omegalambdang.rentanitem.common.audit.AuditSection;
import com.omegalambdang.rentanitem.common.audit.Auditable;
import com.omegalambdang.rentanitem.common.entity.FellowshipEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.omegalambdang.rentanitem.constants.SchemaConstants.TABLE_USERS;


@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditListener.class)
@Getter
@Setter
@Where(clause = "deleted='0'")
@Entity
@Table(name=TABLE_USERS)
@NoArgsConstructor
@SuppressWarnings("NullAway.Init")
public class User extends FellowshipEntity<Long, User> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column(length = 64)
    private String password;

    @Column(length = 100)
    @Nullable
    private String passwordResetToken;

    @Nullable
    private LocalDateTime passwordResetVldtyTerm;

    @Nullable
    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    @Column
    private UserStatus status;

    @Nullable
    private LocalDate statusDate;

    private boolean emailVerified;

    @Nullable
    private Boolean isRentor;

    @Embedded
    private AuditSection auditSection = new AuditSection();

    public User(long id) {
        super();
        this.setId(id);
    }

    public Optional<LocalDate> getStatusDate() {
        return Optional.ofNullable(statusDate);
    }

}
