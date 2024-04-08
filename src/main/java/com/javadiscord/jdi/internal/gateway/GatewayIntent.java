package com.javadiscord.jdi.internal.gateway;

import java.util.List;

/**
 * Gateway intents.
 * <p>
 * As of version 8 of the Discord API, developers are required to set gateway intents when
 * establishing a WebSocket gateway connection. These intents determine what events you
 * receive and what fields you receive for the API objects.
 * <p>
 * Some intents are <a href="https://discord.com/developers/docs/topics/gateway#privileged-intents">privileged intents</a>.
 * <p>
 * See the <a href="https://discord.com/developers/docs/topics/gateway#gateway-intents">Discord API documentation</a> for more information.
 */
public enum GatewayIntent {
    /**
     * The Guilds intent.
     * <p>
     * Specifying this intent allows you to receive information related to guilds and channels.
     */
    GUILDS(1),
    /**
     * The Guild Members intent.
     * <p>
     * Specifying this intent allows you to receive information related to guild members.
     */
    GUILD_MEMBERS(1 << 1),
    /**
     * The Guild Moderation intent.
     * <p>
     * Specifying this intent allows you to receive information about audit logs and guilds bans.
     */
    GUILD_MODERATION(1 << 2),
    /**
     * The Guild Emojis and Stickers intent.
     * <p>
     * Specifying this intent allows you to receive information about guild emojis and stickers.
     */
    GUILD_EMOJIS_AND_STICKERS(1 << 3),
    /**
     * The Guild Integrations intent.
     * <p>
     * Specifying this intent allows you to receive information about guild integrations.
     */
    GUILD_INTEGRATIONS(1 << 4),
    /**
     * The Guild Webhooks intent.
     * <p>
     * Specifying this intent allows you to receive information about guild webhooks.
     */
    GUILD_WEBHOOKS(1 << 5),
    /**
     * The Guild Invites intent.
     * <p>
     * Specifying this intent allows you to receive information about guild invites.
     */
    GUILD_INVITES(1 << 6),
    /**
     * The Guild Voice States intent.
     * <p>
     * Specifying this intent allows you to receive information about guild voice states.
     */
    GUILD_VOICE_STATES(1 << 7),
    /**
     * The Guild Presences intent.
     * <p>
     * Specifying this intent allows you to receive information about guild presence data.
     */
    GUILD_PRESENCES(1 << 8),
    /**
     * The Guild Messages intent.
     * <p>
     * Specifying this intent allows you to receive partial message data.
     */
    GUILD_MESSAGES(1 << 9),
    /**
     * The Guild Message Reactions intent.
     * <p>
     * Specifying this intent allows you to receive message reactions data.
     */
    GUILD_MESSAGE_REACTIONS(1 << 10),
    /**
     * The Guild Message Typing intent.
     * <p>
     * Specifying this intent allows you to receive message typing events.
     */
    GUILD_MESSAGE_TYPING(1 << 11),
    /**
     * The Direct Messages intent.
     * <p>
     * Specifying this intent allows you to receive direct message data.
     */
    DIRECT_MESSAGES(1 << 12),
    /**
     * The Direct Message Reactions intent.
     * <p>
     * Specifying this intent allows you to receive direct message reactions data.
     */
    DIRECT_MESSAGE_REACTIONS(1 << 13),
    /**
     * The Direct Message Typing intent.
     * <p>
     * Specifying this intent allows you to receive direct message typing events.
     */
    DIRECT_MESSAGE_TYPING(1 << 14),
    /**
     * The Message Content intent.
     * <p>
     * Specifying this intent allows you to receive message content data.
     */
    MESSAGE_CONTENT(1 << 15),
    /**
     * The Guild Scheduled Events intent.
     * <p>
     * Specifying this intent allows you to receive guild scheduled events data.
     */
    GUILD_SCHEDULED_EVENTS(1 << 16),
    /**
     * The Auto Moderation Configuration intent.
     * <p>
     * Specifying this intent allows you to receive auto moderation configuration events.
     */
    AUTO_MODERATION_CONFIGURATION(1 << 20),
    /**
     * The Auto Moderation Execution intent.
     * <p>
     * Specifying this intent allows you to receive auto moderation execution events.
     */
    AUTO_MODERATION_EXECUTION(1 << 21);

    /**
     * The bitflags value represented by this intent.
     */
    private final int value;

    /**
     * Constructs a {@link GatewayIntent} with the provided bitflags value.
     * @param value The bitflags value representing the gateway intent.
     */
    GatewayIntent(int value) {
        this.value = value;
    }

    /**
     * Obtains the underlying bitflags representation of this gateway intent.
     *
     * @return The bitflags representation.
     */
    public int getValue() {
        return value;
    }

    /**
     * Obtains the bitflags representation for when all gateway intents are enabled.
     *
     * @return The bitflags representation.
     */
    public static int allIntents() {
        return valueOf(List.of(GatewayIntent.values()));
    }

    /**
     * Computes the combined bitflags representation for a given list of gateway intents.
     *
     * @param intents The list of gateway intents.
     * @return The combined bitflags representation.
     */
    public static int valueOf(List<GatewayIntent> intents) {
        int combinedIntents = 0;
        for (GatewayIntent intent : intents) {
            combinedIntents |= intent.getValue();
        }
        return combinedIntents;
    }
}
