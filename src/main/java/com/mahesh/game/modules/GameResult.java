package com.mahesh.game.modules;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GameResult(String[][] matrix, BigDecimal reward,
		@JsonProperty("applied_winning_combinations") Map<String, List<String>> winningCombinations,
		@JsonProperty("applied_bonus_symbol") String bonusSymbol) {
}
