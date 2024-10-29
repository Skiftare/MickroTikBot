package edu.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PaymentDataManager {
    private static final HashMap<Long, String> IdToComment = new HashMap<>();
    private static final HashMap<String, Long> CommentToId = new HashMap<>();
    private static final HashMap<Long, Long> CommentToAmount = new HashMap<>();
    private static final HashMap<String, Boolean> CommentToStatus = new HashMap<>();
    private static final HashMap<Long, LocalDateTime> IdToCreationTime = new HashMap<>();
    
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    static {
        initializeCleanupTask();
    }

    public record PaymentRecord(Long id, String memo, Boolean status, Long amount) {
    }

    private static void initializeCleanupTask() {
        scheduler.scheduleAtFixedRate(() -> {
            LocalDateTime now = LocalDateTime.now();
            synchronized (PaymentDataManager.class) {
                cleanupExpiredPayments(now);
            }
        }, getInitialDelay(), 1, TimeUnit.HOURS);
    }

    private static long getInitialDelay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextHour = now.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        return java.time.Duration.between(now, nextHour).toMinutes();
    }

    private static void cleanupExpiredPayments(LocalDateTime now) {
        Map<Long, String> expiredPayments = new HashMap<>();
        
        IdToCreationTime.entrySet().removeIf(entry -> {
            if (entry.getValue().plusHours(1).isBefore(now)) {
                if(CommentToStatus.get(IdToComment.get(entry.getKey())) == false) {
                    Long id = entry.getKey();

                    expiredPayments.put(id, IdToComment.get(id));

                    return true;
                }
            }
            return false;
        });

        for (Map.Entry<Long, String> entry : expiredPayments.entrySet()) {
            Long id = entry.getKey();
            String memo = entry.getValue();
            Boolean status = CommentToStatus.get(memo);
            Long amount = CommentToAmount.get(id);
            
            PaymentRecord record = new PaymentRecord(id, memo, status, amount);
            processExpiredPayment(record);
            
            removeWaitingForPayment(id);
        }
    }

    private static void processExpiredPayment(PaymentRecord record) {
        // TODO: Здесь реализовать логику отправки записи в бота
        // Например: botService.processPaymentRecord(record);
    }

    public static synchronized void addWaitingForPayment(Long id, String comment, Long amount) {
        IdToComment.put(id, comment);
        CommentToId.put(comment, id);
        CommentToAmount.put(id, amount);
        CommentToStatus.put(comment, false);
        IdToCreationTime.put(id, LocalDateTime.now());
    }
    public static synchronized void removeWaitingForPayment(Long id) {
        String comment = IdToComment.get(id);
        IdToComment.remove(id);
        CommentToId.remove(comment);
        CommentToAmount.remove(id);
        CommentToStatus.remove(comment);
    }
    public static synchronized boolean isPaymentSuccess(Long id) {
        String comment = IdToComment.get(id);
        return CommentToStatus.get(comment);

    }
    public static synchronized void changeStatus(String comment) {
        if(CommentToStatus.containsKey(comment)) {
            CommentToStatus.put(comment, true);
        }
    }



}
