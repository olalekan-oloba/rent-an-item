package com.omegalambdang.rentanitem.feature.payment;

import com.omegalambdang.rentanitem.common.audit.AuditListener;
import com.omegalambdang.rentanitem.common.audit.AuditSection;
import com.omegalambdang.rentanitem.common.audit.Auditable;
import com.omegalambdang.rentanitem.common.entity.FellowshipEntity;
import com.omegalambdang.rentanitem.feature.account.rentor.Rentor;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static com.omegalambdang.rentanitem.constants.SchemaConstants.TABLE_PAYMENT_ACCOUNT_REFERENCES;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@EqualsAndHashCode
@SQLDelete(sql =
        "UPDATE payment_accounts " +
                "SET deleted = '1' " +
                "WHERE id = ?")
@Where(clause = "deleted='0'")
@Table(name = TABLE_PAYMENT_ACCOUNT_REFERENCES)
@SuppressWarnings("NullAway.Init")
public class PaymentAccountReference extends FellowshipEntity<Long, PaymentAccountReference> implements Auditable {
    @Id
    @Column(name = "user_id")
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id",unique = true)
    private Rentor rentor;
    @Column
    private String bankCode;
    @Column
    @Nullable
    private String bankName;
    @Column
    @Nullable
    private String currency;
    @Column
    private String accountNo;
    @Column
    private String accountName;
    @Column
    @Nullable
    private String paymentRecipientCode; //code linking stylist to external bank account for payments
    private boolean paymentInProgress;

    public PaymentAccountReference(long id) {
        super();
        this.setId(id);
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }

    @Embedded
    private AuditSection auditSection = new AuditSection();
}
