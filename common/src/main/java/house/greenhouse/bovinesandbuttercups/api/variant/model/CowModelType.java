package house.greenhouse.bovinesandbuttercups.api.variant.model;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a Cow Model's location without having to be on the client.
 */
public record CowModelType(@Nullable String namespaceOverride, @Nullable String pathOverride, String babySuffix) {}
