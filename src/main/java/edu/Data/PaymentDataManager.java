package edu.Data;

import java.util.HashMap;

public class PaymentDataManager {
    private static HashMap<Long, String> IdToComment = new HashMap<>();
    private static HashMap<String, Long> CommentToId = new HashMap<>();
    private static HashMap<Long, Long> CommentToAmount = new HashMap<>();
    private static HashMap<String, Boolean> CommentToStatus = new HashMap<>();

    public static synchronized void addWaitingForPayment(Long id, String comment, Long amount) {
        IdToComment.put(id, comment);
        CommentToId.put(comment, id);
        CommentToAmount.put(id, amount);
        CommentToStatus.put(comment, false);
    }
    public static synchronized void removeWaitingForPayment(Long id) {
        String comment = IdToComment.get(id);
        IdToComment.remove(id);
        CommentToId.remove(comment);
        CommentToAmount.remove(id);
        CommentToStatus.remove(comment);
    }
    public static synchronized boolean isWaitingForPayment(Long id) {
        return IdToComment.containsKey(id);
    }
    public static synchronized void changeStatus(String comment) {
        if(CommentToStatus.containsKey(comment)) {
            CommentToStatus.put(comment, true);
            //Здесь надо как-то вызывать бота, для того чтобы он отправил сообщение пользователю с кредами/сообщением об успешной оплате
        }
    }


}
