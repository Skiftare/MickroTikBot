package edu.Integrations.telegram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@ExtendWith(MockitoExtension.class)
class SubscriptionCheckerTest {
    
    private SubscriptionChecker subscriptionChecker;
    
    @BeforeEach
    void setUp() {
        subscriptionChecker = spy(new SubscriptionChecker("test_token"));
    }

    @Test
    void testIsUserSubscribedAsMember() throws TelegramApiException {
        // Подготовка
        ChatMember chatMember = mock(ChatMember.class);
        when(chatMember.getStatus()).thenReturn("member");
        
        doReturn(chatMember).when(subscriptionChecker).execute(any(GetChatMember.class));

        // Проверка
        boolean result = subscriptionChecker.isUserSubscribed(123L, "@testChannel");
        
        assertTrue(result);
        verify(subscriptionChecker).execute(any(GetChatMember.class));
    }

    @Test
    void testIsUserSubscribedAsAdmin() throws TelegramApiException {
        // Подготовка
        ChatMember chatMember = mock(ChatMember.class);
        when(chatMember.getStatus()).thenReturn("administrator");
        
        doReturn(chatMember).when(subscriptionChecker).execute(any(GetChatMember.class));

        // Проверка
        boolean result = subscriptionChecker.isUserSubscribed(123L, "@testChannel");
        
        assertTrue(result);
        verify(subscriptionChecker).execute(any(GetChatMember.class));
    }

    @Test
    void testIsUserSubscribedAsCreator() throws TelegramApiException {
        // Подготовка
        ChatMember chatMember = mock(ChatMember.class);
        when(chatMember.getStatus()).thenReturn("creator");
        
        doReturn(chatMember).when(subscriptionChecker).execute(any(GetChatMember.class));

        // Проверка
        boolean result = subscriptionChecker.isUserSubscribed(123L, "@testChannel");
        
        assertTrue(result);
        verify(subscriptionChecker).execute(any(GetChatMember.class));
    }

    @Test
    void testIsUserNotSubscribed() throws TelegramApiException {
        // Подготовка
        ChatMember chatMember = mock(ChatMember.class);
        when(chatMember.getStatus()).thenReturn("left");
        
        doReturn(chatMember).when(subscriptionChecker).execute(any(GetChatMember.class));

        // Проверка
        boolean result = subscriptionChecker.isUserSubscribed(123L, "@testChannel");
        
        assertFalse(result);
        verify(subscriptionChecker).execute(any(GetChatMember.class));
    }

    @Test
    void testApiException() throws TelegramApiException {
        // Подготовка
        doThrow(new TelegramApiException("API Error"))
            .when(subscriptionChecker)
            .execute(any(GetChatMember.class));

        // Проверка
        boolean result = subscriptionChecker.isUserSubscribed(123L, "@testChannel");
        
        assertFalse(result);
        verify(subscriptionChecker).execute(any(GetChatMember.class));
    }

    @Test
    void testGetBotToken() {
        assertEquals("test_token", subscriptionChecker.getBotToken());
    }

    @Test
    void testCorrectParametersPassedToGetChatMember() throws TelegramApiException {
        // Подготовка
        ChatMember chatMember = mock(ChatMember.class);
        when(chatMember.getStatus()).thenReturn("member");
        
        // Захватываем параметры вызова
        ArgumentCaptor<GetChatMember> getChatMemberCaptor = ArgumentCaptor.forClass(GetChatMember.class);
        doReturn(chatMember).when(subscriptionChecker).execute(getChatMemberCaptor.capture());

        // Выполнение
        subscriptionChecker.isUserSubscribed(123L, "@testChannel");

        // Проверка параметров
        GetChatMember capturedGetChatMember = getChatMemberCaptor.getValue();
        assertEquals("@testChannel", capturedGetChatMember.getChatId());
        assertEquals(123L, capturedGetChatMember.getUserId());
    }
} 