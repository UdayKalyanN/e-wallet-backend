package com.example.ewallet.wallet.service.resource;

import com.example.ewallet.wallet.domain.Wallet;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
public class WalletResponse {

    private Long userId;

    private Long walletId;

    private Double balance;


    public WalletResponse(Wallet wallet) {
        this.userId = wallet.getUserId();
        this.walletId = wallet.getWalletId();
        this.balance = wallet.getBalance();
    }
}
