package com.javadiscord.jdi.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadiscord.jdi.internal.api.DiscordRequestDispatcher;
import com.javadiscord.jdi.internal.gateway.*;
import com.javadiscord.jdi.internal.gateway.identify.IdentifyRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The core of JavaDiscordAPI. It manages gateway connections to Discord with a
 * {@link com.javadiscord.jdi.internal.gateway.WebSocketManager} and runs a
 * {@link com.javadiscord.jdi.internal.api.DiscordRequestDispatcher}.
 *
 * @author Suraj Kumar
 * @since 1.0.0
 */
public class Discord {
    /**
     * An instance of a {@link org.apache.logging.log4j.Logger} for diagnostics
     * and logging purposes.
     */
    private static final Logger LOGGER = LogManager.getLogger();
    /**
     * An instance of a {@link java.util.concurrent.Executor} for managing
     * {@link java.lang.Runnable} tasks.
     */
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
    /**
     * An {@link com.fasterxml.jackson.databind.ObjectMapper} for reading and writing
     * JSON from Java objects.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * The official website of JavaDiscordAPI. This is used when identifying with Discord
     * through the WebSocket gateway.
     */
    private static final String WEBSITE = "https://javadiscord.com/";
    /**
     * The OAuth token of the bot that is required for authentication when using most
     * endpoints of the Discord API.
     */
    private final String botToken;
    /**
     * The identify request payload sent to Discord after establishing a connection
     * to the WebSocket gateway.
     */
    private final IdentifyRequest identifyRequest;
    /**
     * An HTTP request dispatcher for sending HTTP requests to the Discord API
     * endpoints.
     */
    private final DiscordRequestDispatcher discordRequestDispatcher;
    /**
     * The JSON payload resulting from a request to the <a href="https://discord.com/developers/docs/topics/gateway#get-gateway-bot">Get Gateway Bot</a>
     * endpoint.
     */
    private final Gateway gateway;
    /**
     * Configuration options for specifying compression algorithm and API version for
     * the WebSocket gateway.
     */
    private final GatewaySetting gatewaySetting;

    /**
     * Constructs a new Discord client with the specified bot token.
     * <p>
     * By default, this constructs a client enabling all of the
     * <a href="https://discord.com/developers/docs/topics/gateway#gateway-intents">Gateway Intents</a>.
     *
     * @param botToken The bot token to use for this instance of the client.
     */
    public Discord(String botToken) {
        this(
                botToken,
                IdentifyRequest.builder()
                        .token(botToken)
                        .os(WEBSITE)
                        .browser(WEBSITE)
                        .device(WEBSITE)
                        .compress(false)
                        .largeThreshold(250)
                        .shard(new int[] {0, 1})
                        .activityName("")
                        .activityType(0)
                        .presenceStatus("online")
                        .afk(false)
                        .intents(GatewayIntent.allIntents())
                        .build());
    }

    /**
     * Constructs a new Discord client with the specified bot token and list of
     * {@link com.javadiscord.jdi.internal.gateway.GatewayIntent}s.
     *
     * @param botToken The bot token to use for this instance of the client.
     * @param intents The list of <a href="https://discord.com/developers/docs/topics/gateway#gateway-intents">Gateway Intents</a> to use.
     */
    public Discord(String botToken, List<GatewayIntent> intents) {
        this(
                botToken,
                IdentifyRequest.builder()
                        .token(botToken)
                        .os(WEBSITE)
                        .browser(WEBSITE)
                        .device(WEBSITE)
                        .compress(false)
                        .largeThreshold(250)
                        .shard(new int[] {0, 1})
                        .activityName("")
                        .activityType(0)
                        .presenceStatus("online")
                        .afk(false)
                        .intents(GatewayIntent.valueOf(intents))
                        .build());
    }

    /**
     * Constructs a new Discord client with the specified bot token and a custom
     * {@link com.javadiscord.jdi.internal.gateway.identify.IdentifyRequest} payload.
     *
     * @param botToken The bot token to use for this instance of the client.
     * @param identifyRequest The custom identify request payload to use for this instance.
     */
    public Discord(String botToken, IdentifyRequest identifyRequest) {
        this.botToken = botToken;
        this.discordRequestDispatcher = new DiscordRequestDispatcher(botToken);
        this.gateway = getGatewayURL(botToken);
        this.gatewaySetting =
                new GatewaySetting().setEncoding(GatewayEncoding.JSON).setApiVersion(10);
        this.identifyRequest = identifyRequest;
    }

    /**
     * Initiates a Discord gateway connection and starts a background task for the
     * request dispatcher.
     */
    public void start() {
        WebSocketManager webSocketManager =
                new WebSocketManager(
                        new GatewaySetting().setApiVersion(10).setEncoding(GatewayEncoding.JSON),
                        identifyRequest,
                        this);

        WebSocketManagerProxy webSocketManagerProxy = new WebSocketManagerProxy(webSocketManager);
        ConnectionDetails connectionDetails =
                new ConnectionDetails(gateway.url(), botToken, gatewaySetting);
        ConnectionMediator connectionMediator =
                new ConnectionMediator(connectionDetails, webSocketManagerProxy);
        webSocketManagerProxy.start(connectionMediator);

        EXECUTOR.execute(discordRequestDispatcher);
    }

    /**
     * Obtains the preferred gateway configuration for establishing the connection.
     *
     * @param authentication The bot token for use.
     * @return The preferred gateway configuration returned from Discord.
     */
    private static Gateway getGatewayURL(String authentication) {
        try (HttpClient httpClient = HttpClient.newBuilder().build()) {
            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create("https://discord.com/api/gateway/bot"))
                            .header("Authorization", "Bot " + authentication)
                            .GET()
                            .build();
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return OBJECT_MAPPER.readValue(response.body(), Gateway.class);
        } catch (Exception e) {
            LOGGER.error("Failed to fetch the gateway URL from discord");
            throw new RuntimeException(e);
        }
    }
}
