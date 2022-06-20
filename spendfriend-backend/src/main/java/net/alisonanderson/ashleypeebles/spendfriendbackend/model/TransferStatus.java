package net.alisonanderson.ashleypeebles.spendfriendbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="transfer_status")
public enum TransferStatus {
    PENDING,
    APPROVED,
    REJECTED,
    INVALID_TRANSFER,
    INVALID_AMOUNT,
    USER_NOT_FOUND,
    NSF,
    UNAUTHORIZED
    ;

    @Id
    @Column(name = "transfer_status_id")
    @JoinColumn(name="transfer_status_id")
    @JsonProperty("transferStatusId")
    private long transferStatusId;

    @Column(name="transfer_status_desc")
    @JsonProperty("transferStatusDescription")
    private String transferStatusDescription;

    @JsonIgnore
    @OneToMany(mappedBy = "transferStatus")
    private List<Transfer> transfers;

    @Autowired
    TransferStatus() {
    }

    TransferStatus(long transferStatusId, String transferStatusDescription) {
        this.transferStatusId = transferStatusId;
        this.transferStatusDescription = transferStatusDescription;
    }

    TransferStatus(long transferStatusId, String transferStatusDescription, List<Transfer> transfers) {
        this.transferStatusId = transferStatusId;
        this.transferStatusDescription = transferStatusDescription;
        this.transfers = transfers;
    }

    public long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatusDescription() {
        return transferStatusDescription;
    }

    public void setTransferStatusDescription(String transferStatusDescription) {
        this.transferStatusDescription = transferStatusDescription;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }
}
