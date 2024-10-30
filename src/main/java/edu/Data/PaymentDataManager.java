package edu.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.logging.Logger;

public class PaymentDataManager {
    private static HashMap<Long, String> IdToComment;
    private static HashMap<String, Long> CommentToId;
    private static HashMap<Long, Long> CommentToAmount;
    private static HashMap<String, Boolean> CommentToStatus;
    private static HashMap<Long, LocalDateTime> IdToCreationTime ;


    public record PaymentRecord(Long id, String memo, Boolean status, Long amount) {
    }
    static {
        IdToComment = new HashMap<>();
        CommentToId = new HashMap<>();
        CommentToAmount = new HashMap<>();
        CommentToStatus = new HashMap<>();
        IdToCreationTime = new HashMap<>();
    }


    public static void addWaitingForPayment(Long id, String comment, Long amount) {
        IdToComment.put(id, comment);
        CommentToId.put(comment, id);
        CommentToAmount.put(id, amount);
        CommentToStatus.put(comment, false);
        IdToCreationTime.put(id, LocalDateTime.now());
    }
    public static void removeWaitingForPayment(Long id) {
        String comment = IdToComment.get(id);
        IdToComment.remove(id);
        CommentToId.remove(comment);
        CommentToAmount.remove(id);
        CommentToStatus.remove(comment);
    }
    public static boolean isPaymentSuccess(Long id) {
        String comment = IdToComment.get(id);
        Logger.getAnonymousLogger().info("Checking payment status for comment: " + comment);
        if(CommentToStatus.containsKey(comment)) {
            Logger.getAnonymousLogger().info("Payment status for comment: " + comment + " is " + CommentToStatus.get(comment));
            boolean status = CommentToStatus.get(comment);
            return status;
        }
        return false;



    }
    public static void changeStatus(String comment) {
        if(CommentToStatus.containsKey(comment)) {
            CommentToStatus.put(comment, true);
        }
    }



}
