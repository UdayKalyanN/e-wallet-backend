package com.example.ewallet.wallet.service;

import com.example.ewallet.wallet.domain.Wallet;
import com.example.ewallet.wallet.service.resource.WalletResponse;
import com.example.ewallet.wallet.service.resource.WalletTransactionRequest;

public interface WalletService {

    public void createWallet(Long userId);

    public Wallet deleteWallet(Long userId);

    public WalletResponse getWallet(Long userId);

    public boolean performTransaction(WalletTransactionRequest walletTransactionRequest);

}
