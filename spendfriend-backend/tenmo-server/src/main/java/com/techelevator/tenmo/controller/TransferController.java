package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.business.AccountService;
import com.techelevator.tenmo.business.TransferService;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("transfer/")
@PreAuthorize("isAuthenticated()")
public class TransferController {

    AccountService accountService;
    TransferService transferService;

    @Autowired
    public TransferController(AccountService accountService, TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
    }

    @PostMapping("send")
    public Transfer sendTransfer(@RequestBody TransferDTO transferDetails, Principal principal) {
        Transfer transfer = transferService.createTransfer(transferDetails, principal.getName());
        return transferService.sendTransfer(transfer, principal.getName());
    }

    @PostMapping("request")
    public Transfer requestTransfer(@RequestBody TransferDTO transferDetails, Principal principal) {
        Transfer transfer = transferService.createTransfer(transferDetails, principal.getName());
        return transferService.requestTransfer(transfer);
    }

    @PutMapping("approve/{transferId}")
    public Transfer approveTransfer(@PathVariable long transferId, Principal principal) {
        Transfer pendingTransfer = transferService.findById(transferId);
        return transferService.approveTransfer(pendingTransfer, principal.getName());
    }

    @PutMapping("reject/{transferId}")
    public Transfer rejectTransfer(@PathVariable long transferId, Principal principal) {
        Transfer pendingTransfer = transferService.findById(transferId);
        return transferService.rejectTransfer(pendingTransfer, principal.getName());
    }

    @GetMapping("account/{accountId}")
    public List<Transfer> allTransfersByAccountId(@PathVariable long accountId) {
        return transferService.findAllByAccountId(accountId);
    }

    @GetMapping("account/{accountId}/from")
    public List<Transfer> allSentTransfersByAccountId(@PathVariable long accountId) {
        return transferService.findAllByAccountFrom(accountId);
    }

    @GetMapping("account/{accountId}/to")
    public List<Transfer> allReceivedTransfersByAccountId(@PathVariable long accountId) {
        return transferService.findAllByAccountTo(accountId);
    }

    @GetMapping("account/{accountId}/from/status/{statusId}")
    public List<Transfer> allSentTransfersByAccountIdAndStatus(@PathVariable long accountId,
                                                               @PathVariable long statusId) {
        return transferService.findAllByAccountFromAndTransferStatusId(accountId, statusId);
    }

//    @GetMapping("account/{accountId}/to/status/{statusId}")
//    public List<Transfer> allReceivedTransfersByAccountIdAndStatus(@PathVariable long accountId,
//                                                                   @PathVariable long statusId) {
//        return transferService.findAllByAccountToAndTransferStatusId(accountId, statusId);
//    }

    @GetMapping("{transferId}")
    public Transfer getTransferByTransferId(@PathVariable long transferId) {
        return transferService.findById(transferId);
    }

}
