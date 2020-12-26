package de.speexx.poc.json.schema4records;


import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sascha.kohlmann
 */
public record JacksonRecord(@NotNull @JsonProperty("test") String testValue) {}
