package com.example.ewallet.user.service.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    private Long receiverId;
    private double amount;
    private String description;
    private String transactionType;
}
