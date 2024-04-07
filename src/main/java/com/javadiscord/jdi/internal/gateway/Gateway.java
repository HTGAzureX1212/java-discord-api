package com.javadiscord.jdi.internal.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A preferred set of configuration for usage when connecting to the Discord WebSocket gateway.
 * <p>
 * This object is returned by calling the <a href="https://discord.com/developers/docs/topics/gateway#get-gateway-bot">Get Gateway Bot</a>
 * endpoint.
 *
 * @param url WebSocket URL that can be used for connecting to the Discord gateway.
 * @param shards Recommended number of <a href="https://discord.com/developers/docs/topics/gateway#sharding">shards</a>
 *               to use when connecting to the Discord gateway.
 * @param sessionStartLimit Information on the current session start limit.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Gateway(
        String url,
        int shards,
        @JsonProperty("session_start_limit") SessionStartLimit sessionStartLimit) {}
