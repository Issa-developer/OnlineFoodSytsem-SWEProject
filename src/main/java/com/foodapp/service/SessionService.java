package com.foodapp.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;

@Service
public class SessionService {
    private static final Map<String, Session> activeSessions = new ConcurrentHashMap<>();
    private static final int SESSION_DURATION_HOURS = 24;

    // MUST be static to be called from UserService
    public static String createSession(String userId) {
        String sessionToken = UUID.randomUUID().toString();
        LocalDateTime expiryTime = LocalDateTime.now().plusHours(SESSION_DURATION_HOURS);

        Session session = new Session(userId, expiryTime);
        activeSessions.put(sessionToken, session);

        System.out.println("SESSION CREATED for user: " + userId);
        System.out.println("SESSION TOKEN: " + sessionToken);
        return sessionToken;
    }

    public static boolean validateSession(String sessionToken) {
        Session session = activeSessions.get(sessionToken);

        if (session != null && session.getExpiryTime().isAfter(LocalDateTime.now())) {
            return true;
        } else {
            activeSessions.remove(sessionToken);
            return false;
        }
    }

    public static void printAllSessions() {
        System.out.println("=== ACTIVE SESSIONS ===");
        activeSessions.forEach((token, session) -> {
            System.out.println("Token: " + token + " | User: " + session.getUserId() + " | Expires: " + session.getExpiryTime());
        });
        System.out.println("Total active sessions: " + activeSessions.size());
    }

    public static String getUserIdFromToken(String sessionToken) {
        Session session = activeSessions.get(sessionToken);
        return (session != null && session.getExpiryTime().isAfter(LocalDateTime.now()))
                ? session.getUserId()
                : null;
    }

    public static boolean invalidateSession(String sessionToken) {
        boolean removed = activeSessions.remove(sessionToken) != null;
        System.out.println("SESSION INVALIDATED: " + sessionToken + " - " + (removed ? "SUCCESS" : "FAILED"));
        return removed;
    }

    private static class Session {
        private final String userId;
        private final LocalDateTime expiryTime;

        public Session(String userId, LocalDateTime expiryTime) {
            this.userId = userId;
            this.expiryTime = expiryTime;
        }

        public String getUserId() { return userId; }
        public LocalDateTime getExpiryTime() { return expiryTime; }
    }
}