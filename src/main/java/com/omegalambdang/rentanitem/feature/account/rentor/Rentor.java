package com.omegalambdang.rentanitem.feature.account.rentor;

import com.omegalambdang.rentanitem.common.audit.AuditListener;
import com.omegalambdang.rentanitem.feature.account.Gender;
import com.omegalambdang.rentanitem.feature.account.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static com.omegalambdang.rentanitem.constants.SchemaConstants.TABLE_RENTORS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@SQLDelete(sql =
        "UPDATE rentors " +
                "SET deleted = '1' " +
                "WHERE id = ?")
@Where(clause = "deleted='0'")
@Table(name = TABLE_RENTORS)
@SuppressWarnings("NullAway.Init")
public class Rentor extends User {

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String phone;

    @Column
    private String contactAddress;
    @Column
    private boolean customerAgreement;
    public Rentor(long id) {
        this.setId(id);
    }

}
