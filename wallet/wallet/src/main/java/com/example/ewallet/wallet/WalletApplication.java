package com.example.ewallet.wallet;

import com.example.ewallet.wallet.domain.Wallet;
import com.example.ewallet.wallet.service.WalletService;
import com.example.ewallet.wallet.service.resource.WalletTransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}

//	@Autowired
//	WalletService walletService;
//
//	@Override
//	public void run(String... args) throws Exception {
//		WalletTransactionRequest walletTransactionRequest = new WalletTransactionRequest();
//		walletTransactionRequest.setReceiverId(2L);
//		walletTransactionRequest.setSenderId(1L);
//		walletTransactionRequest.setAmount(1000.0);
//		walletTransactionRequest.setTransactionType("DEPOSIT");
//		walletTransactionRequest.setDescription("Initial Deposit");
//		walletService.performTransaction(walletTransactionRequest);
//
//	}
}
