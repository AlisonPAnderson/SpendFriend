package net.alisonanderson.ashleypeebles.spendfriendbackend.dao;

import net.alisonanderson.ashleypeebles.spendfriendbackend.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query(value = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
            "FROM transfer " +
            "WHERE account_from = :account_id " +
            "OR account_to = :account_id" ,
            nativeQuery = true)
    List<Transfer> findAllByAccountId(@Param("account_id") long accountId);

    @Query(value = "SELECT transfer_id, transfer_type_id, transfer.transfer_status_id, account_from, account_to, amount " +
            "FROM transfer " +
            "JOIN transfer_status " +
            "ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
            "WHERE transfer.transfer_status_id = :status " +
            "AND (account_from = :accountid " +
            "OR account_to = :accountid)" ,
            nativeQuery = true)
    List<Transfer> findAllByStatusAndUser(@Param("status") long transferStatusId,
                                          @Param("accountid") long accountId);

   Transfer findById(long id);

   @Query(value = "select * from transfer t " +
           "JOIN account a ON t.account_from = a.account_id " +
           "WHERE transfer_status_id = :transferStatusId AND a.account_id = :accountId",
           nativeQuery = true)
   List<Transfer> findAllByAccountFromAndTransferStatusId(long accountId, long transferStatusId);

    //<editor-fold desc="May not need - was for viewing outstanding requests">
    /* @Query(value = "select * from transfer t " +
            "JOIN account a ON t.account_to = a.account_id " +
            "WHERE transfer_status_id = :transferStatusId AND a.account_id = :accountId ",
            nativeQuery = true)
    List<Transfer> findAllByAccountToAndTransferStatusId(long accountId, long transferStatusId); */
    //</editor-fold>

    List<Transfer> findAllByAccountIdFrom(long accountIdFrom);

    List<Transfer> findAllByAccountIdTo(long accountIdTo);
}
