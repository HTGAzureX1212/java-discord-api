package com.javadiscord.gateway.handlers.heartbeat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadiscord.Discord;
import com.javadiscord.gateway.ConnectionMediator;
import com.javadiscord.gateway.GatewayEvent;
import com.javadiscord.gateway.handlers.MessageHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloHandler implements MessageHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final HeartbeatService heartbeatService;

    public HelloHandler(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record HeartbeatData(@JsonProperty("heartbeat_interval") int interval) {}

    @Override
    public void handle(GatewayEvent event, ConnectionMediator connectionMediator, Discord discord) {
        JsonNode jsonData = event.data();
        try {
            HeartbeatData heartbeatData =
                    OBJECT_MAPPER.readValue(jsonData.toString(), HeartbeatData.class);
            heartbeatService.startHeartbeat(
                    connectionMediator.getWebSocketManagerProxy().getWebSocket(),
                    heartbeatData.interval);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to parse Heartbeat data", e);
        }
    }
}
