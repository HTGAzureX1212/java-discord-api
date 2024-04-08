package com.javadiscord.jdi.internal.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * A payload sent by Discord over a gateway connection.
 *
 * @param opcode The opcode of this payload.
 * @param data The data associated with this payload.
 * @param sequenceNumber This indicates how many events (including this one) has been received from Discord.
 *                       Typically used for heartbeats and resuming sessions.
 * @param eventName The name of the event associated with this payload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GatewayEvent(
        @JsonProperty("op") int opcode,
        @JsonProperty("d") JsonNode data,
        @JsonProperty("s") int sequenceNumber,
        @JsonProperty("t") String eventName) {}
