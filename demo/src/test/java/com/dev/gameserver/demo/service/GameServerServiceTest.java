package com.dev.gameserver.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dev.gameserver.demo.exception.UnauthorizedAccessException;
import com.dev.gameserver.demo.model.GameServer;
import com.dev.gameserver.demo.model.GameTemplate;
import com.dev.gameserver.demo.model.ServerPrivacy;
import com.dev.gameserver.demo.model.ServerStatus;
import com.dev.gameserver.demo.model.User;
import com.dev.gameserver.demo.repository.GameServerRepository;
import com.dev.gameserver.demo.repository.GameTemplateRepository;
import com.dev.gameserver.demo.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class GameServerServiceTest {

    @Mock
    private GameServerRepository serverRepository;

    @Mock
    private GameTemplateRepository gameTemplateRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GameServerService gameServerService;

    private User owner;
    private User otherUser;
    private GameTemplate template;
    private GameServer server;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setUsername("quentin");

        otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("thomas");

        template = new GameTemplate();
        template.setId(1L);
        template.setName("Minecraft");

        server = new GameServer();
        server.setId(10L);
        server.setUser(owner);
        server.setGameTemplate(template);
        server.setName("Quentin Server");
    }

    @Test
    void createServer_Success() {
        when(gameTemplateRepository.findById(1L)).thenReturn(Optional.of(template));
        when(userRepository.findByUsername("quentin")).thenReturn(Optional.of(owner));
        when(serverRepository.save(any(GameServer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GameServer created = gameServerService.createServer(1L, 10, "quentin", "Mon Serveur", "PUBLIC", "pass");

        assertNotNull(created);
        assertEquals("Mon Serveur", created.getName());
        assertEquals(ServerStatus.OFFLINE, created.getStatus());
        assertEquals(ServerPrivacy.PUBLIC, created.getPrivacy());
        assertEquals(owner, created.getUser());
        assertEquals(template, created.getGameTemplate());
    }

    @Test
    void deleteServer_Success_WhenOwner() {
        when(serverRepository.findById(10L)).thenReturn(Optional.of(server));
        when(userRepository.findByUsername("quentin")).thenReturn(Optional.of(owner));

        assertDoesNotThrow(() -> gameServerService.deleteServer(10L, "quentin"));
        verify(serverRepository, times(1)).delete(server);
    }

    @Test
    void deleteServer_ThrowsException_WhenNotOwner() {
        when(serverRepository.findById(10L)).thenReturn(Optional.of(server));
        // Simulate "thomas" trying to delete "quentin"'s server
        when(userRepository.findByUsername("thomas")).thenReturn(Optional.of(otherUser));

        assertThrows(UnauthorizedAccessException.class, () -> {
            gameServerService.deleteServer(10L, "thomas");
        });
        
        verify(serverRepository, never()).delete(any());
    }
}
