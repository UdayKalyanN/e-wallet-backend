package com.example.ewallet.wallet.service.Impl;

import com.example.ewallet.wallet.domain.TransactionType;
import com.example.ewallet.wallet.domain.Wallet;
import com.example.ewallet.wallet.exception.WalletException;
import com.example.ewallet.wallet.repository.WalletRepository;
import com.example.ewallet.wallet.service.WalletService;
import com.example.ewallet.wallet.service.resource.WalletResponse;
import com.example.ewallet.wallet.service.resource.WalletTransactionRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class WalletServiceImpl implements WalletService {

    private Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);
    //private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Autowired
    WalletRepository walletRepository;

    @Override
    public void createWallet(Long userId) {
        Wallet wallet=walletRepository.findByUserId(userId);
        try{
            if(Objects.nonNull(wallet)){
                logger.info("Wallet already exists for user: {}", userId);
                return;
            }
            // Rest Call or webclient to user service to check if user exists.
            wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setBalance(0.0);
            wallet.setActive(true);
            walletRepository.save(wallet);
        }catch (Exception e){
            logger.error("Error while creating wallet: {}", e.getMessage());
        }
    }

    @Override
    public Wallet deleteWallet(Long userId) {
        try{
            Wallet wallet=walletRepository.findByUserId(userId);
            if(wallet.isActive()){
                wallet.setActive(false);
                 walletRepository.save(wallet);
                 return wallet;
            }
            else{
                logger.info("Wallet not found for user: {}", userId);
                return null;
            }
        }catch (Exception e){
            logger.error("Error while deleting wallet: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public WalletResponse getWallet(Long userId) {
        Wallet wallet=walletRepository.findByUserId(userId);
        if(Objects.nonNull(wallet)){
            logger.info("Wallet found for user: {}", userId);
            return new WalletResponse(wallet);
        }
        else{
            logger.info("Wallet not found for user: {}", userId);
            throw new WalletException("EWALLET_USER_NOT_FOUND_EXCEPTION","Wallet not found for user: "+userId);
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = WalletException.class)
    public boolean performTransaction(WalletTransactionRequest walletTransactionRequest) {
        Wallet senderWallet=walletRepository.findByUserId(walletTransactionRequest.getSenderId());
        Wallet receiverWallet=walletRepository.findByUserId(walletTransactionRequest.getReceiverId());

        if(TransactionType.DEPOSIT.name().equals(walletTransactionRequest.getTransactionType())){
            if(receiverWallet.isActive()){

                updateWallet(senderWallet,1*walletTransactionRequest.getAmount());
                logger.info("Wallet updated successfully for user: {}", walletTransactionRequest.getSenderId());
                return true;
            }
            else{
                logger.info("Wallet is not active for user: {}", walletTransactionRequest.getSenderId());
                throw new WalletException("EWALLET_USER_NOT_ACTIVE_EXCEPTION","Wallet is not active for user: "+walletTransactionRequest.getSenderId());
            }
        }
        else if(TransactionType.WITHDRAWAL.name().equals(walletTransactionRequest.getTransactionType())){
            if(!senderWallet.isActive()){
                logger.info("Wallet is not active for user: {}", walletTransactionRequest.getSenderId());
                throw new WalletException("EWALLET_USER_NOT_ACTIVE_EXCEPTION","Wallet is not active for user: "+walletTransactionRequest.getSenderId());
            }
            updateWallet(receiverWallet,-1*walletTransactionRequest.getAmount());
            logger.info("Wallet updated successfully for user: {}", walletTransactionRequest.getReceiverId());
            return true;
        }
        else if(TransactionType.TRANSFER.name().equals(walletTransactionRequest.getTransactionType())){
            try{
                if(Objects.isNull(senderWallet) || Objects.isNull(receiverWallet)){
                    logger.info("Wallet is not found for user: {}", walletTransactionRequest.getSenderId());
                    throw new WalletException("EWALLET_USER_NOT_FOUND_EXCEPTION","Wallet is not found for user: "+walletTransactionRequest.getSenderId());
                }

                if(!senderWallet.isActive() || !receiverWallet.isActive()){
                    logger.info("Wallet is not active for user: {}", walletTransactionRequest.getSenderId());
                    throw new WalletException("EWALLET_USER_NOT_ACTIVE_EXCEPTION","Wallet is not active for user: "+walletTransactionRequest.getSenderId());
                }
                handleTransaction(senderWallet,receiverWallet,walletTransactionRequest.getAmount());
                return true;
            }catch (WalletException e){
                logger.error("Error while performing transaction: {}", e.getMessage());
                return false;
            }
        }
        else{
            logger.info("Invalid transaction type: {}", walletTransactionRequest.getTransactionType());
            throw new WalletException("EWALLET_INVALID_TRANSACTION_TYPE_EXCEPTION","Invalid transaction type: "+walletTransactionRequest.getTransactionType());
        }
    }

    private void updateWallet(Wallet senderWallet, Double amount) {
        senderWallet.setBalance(senderWallet.getBalance()+amount);
        walletRepository.save(senderWallet);
    }

    public void handleTransaction(Wallet senderWallet, Wallet receiverWallet, Double amount) {
       try{
            Wallet senderCopy = new Wallet();
            BeanUtils.copyProperties(senderWallet, senderCopy);
            Wallet receiverCopy = new Wallet();
            BeanUtils.copyProperties(receiverWallet, receiverCopy);
            if(senderCopy.getBalance() < amount){
                logger.info("Insufficient balance for user: {}", senderWallet.getUserId());
                throw new WalletException("EWALLET_INSUFFICIENT_BALANCE_EXCEPTION","Insufficient balance for user: "+senderWallet.getUserId());
            }
            senderCopy.setBalance(senderCopy.getBalance() - amount);
            receiverCopy.setBalance(receiverCopy.getBalance() + amount);
            walletRepository.save(senderCopy);
            walletRepository.save(receiverCopy);
        }catch (WalletException e){
            logger.error("Error while performing transaction: {}", e.getMessage());
            throw e;
        }
    }
}
