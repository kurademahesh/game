package com.mahesh.game.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Probabilities(
		@JsonProperty("standard_symbols") List<CellProbability> standardSymbols, 
		@JsonProperty("bonus_symbols") CellProbability bonusSymbols) {
}
