package com.javadiscord.discord.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReactionCountDetails(
        @JsonProperty("burst") int burst, @JsonProperty("normal") int normal) {}