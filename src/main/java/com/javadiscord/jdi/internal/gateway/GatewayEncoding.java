package com.javadiscord.jdi.internal.gateway;

/**
 * The encoding to be used to communicate with the Discord WebSocket gateway.
 *
 * @author Suraj Kumar
 */
public enum GatewayEncoding {
    /**
     * Plain text JSON encoding.
     */
    JSON,
    /**
     * Binary ETF encoding.
     */
    ETF
}
