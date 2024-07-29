package com.example.ewallet.wallet.controller;

import com.example.ewallet.wallet.service.WalletService;
import com.example.ewallet.wallet.service.resource.WalletTransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WalletController {

    @Autowired
    WalletService walletService;

    @GetMapping("wallet/{userId}")
    public ResponseEntity<?> getWallet(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(walletService.getWallet(userId), HttpStatus.OK) ;
    }

    @PostMapping("wallet/transaction")
    public ResponseEntity<?> transaction(@RequestBody WalletTransactionRequest walletTransactionRequest) {
        boolean success=walletService.performTransaction(walletTransactionRequest);
        if(success)
            return new ResponseEntity<>(true,HttpStatus.OK);
        else
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
    }


}
