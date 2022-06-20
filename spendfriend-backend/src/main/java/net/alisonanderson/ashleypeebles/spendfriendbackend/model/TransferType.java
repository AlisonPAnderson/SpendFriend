package net.alisonanderson.ashleypeebles.spendfriendbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="transfer_type")
public enum TransferType {
    REQUEST, SEND;

    @Id
    @Column(name = "transfer_type_id")
    @JoinColumn(name="transfer_type_id")
    private long transferTypeId;

    @Column(name="transfer_type_desc")
    private String transferTypeDescription;

    @JsonIgnore
    @OneToMany(mappedBy = "transferType")
    private List<Transfer> transfers;

    @Autowired
    TransferType() {
    }

    TransferType(long transferTypeId, String transferTypeDescription, List<Transfer> transfers) {
        this.transferTypeId = transferTypeId;
        this.transferTypeDescription = transferTypeDescription;
        this.transfers = transfers;
    }

    TransferType(long transferTypeId, String transferTypeDescription) {
        this.transferTypeId = transferTypeId;
        this.transferTypeDescription = transferTypeDescription;
    }


    public long getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferTypeDescription() {
        return transferTypeDescription;
    }

    public void setTransferTypeDescription(String transferTypeDescription) {
        this.transferTypeDescription = transferTypeDescription;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }

}
