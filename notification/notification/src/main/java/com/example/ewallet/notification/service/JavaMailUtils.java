package com.example.ewallet.notification.service;

public class JavaMailUtils {

    public static String getSuccessSenderEmailContent(String UserName, Double amount){
        StringBuilder content = new StringBuilder();
        content.append("Dear " + UserName + ", \n");
        content.append("Your account has debited with " + amount + " . \n");
        return content.toString();
    }

    public static String getSuccessReceiverEmailContent(String receiverUserName, String senderUserName, Double amount){
        StringBuilder content = new StringBuilder();
        content.append("Dear " + receiverUserName + ", \n");
        content.append("Your Account has been credited with " + amount + " from " + senderUserName + " \n");
        return content.toString();
    }

    public static String getFailedSenderEmailContent(String UserName, Double amount){
        StringBuilder content = new StringBuilder();
        content.append("Dear " + UserName + ", \n");
        content.append("Your transaction of " + amount + " has been failed. \n");
        return content.toString();
    }

    public static String getSubjectSenderSuccessfulTransaction(String senderUserName, Double amount){
        StringBuilder subject = new StringBuilder();
        subject.append("Sent "+amount+" to "+senderUserName);
        return subject.toString();
    }

    public static String getSubjectReceiverSuccessfulTransaction(String UserName, Double amount){
        StringBuilder subject = new StringBuilder();
        subject.append("Received "+amount+" from "+UserName);
        return subject.toString();
    }

    public static String getSubjectFailedTransaction(String UserName, Double amount){
        StringBuilder subject = new StringBuilder();
        subject.append("Failed Transaction of "+amount+" to "+UserName);
        return subject.toString();
    }
}
