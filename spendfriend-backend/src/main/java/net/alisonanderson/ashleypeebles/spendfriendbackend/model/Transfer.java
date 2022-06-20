package net.alisonanderson.ashleypeebles.spendfriendbackend.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transfer")


public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private long transferId;

    @Column(name="transfer_type_id")
    private long transferTypeId;

    @Column(name="transfer_status_id")
    @JsonProperty("transferStatusId")
    private long transferStatusId;

    @Column(name = "account_from")
    @JsonProperty("accountIdFrom")
    private long accountIdFrom;

    @Column(name = "account_to")
    @JsonProperty("accountIdTo")
    private long accountIdTo;

    @Column(name="amount")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="transfer_status_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Enumerated(EnumType.STRING)
    private TransferStatus transferStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="transfer_type_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_from", referencedColumnName = "account_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_to", referencedColumnName = "account_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Account toAccount;

        public Transfer(long transferId, long transferTypeId, long transferStatusId, long accountIdFrom, long accountIdTo, BigDecimal amount, TransferStatus transferStatus, TransferType transferType, Account fromAccount, Account toAccount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
        this.amount = amount;
        this.transferStatus = transferStatus;
        this.transferType = transferType;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

}
