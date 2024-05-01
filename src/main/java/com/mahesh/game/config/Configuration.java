package com.mahesh.game.config;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Configuration(int columns, int rows,
		Map<String, Symbol> symbols, Probabilities probabilities,
		@JsonProperty("win_combinations") Map<String, WinCombination> winCombinations) {

	public boolean isValid() {
		if (this.columns <= 0 && this.rows <= 0) {
			System.out.println("Invalid metrix values");
			return false;
		}
		if (this.symbols.isEmpty()) {
			System.out.println("Invalid symbols");
			return false;
		}
		if (this.probabilities == null || this.probabilities.standardSymbols().isEmpty()) {
			System.out.println("Invalid probabilities");
			return false;
		}
		if (this.winCombinations.isEmpty()) {
			System.out.println("Win Combinations are empty");
			return false;
		}
		return true;
	}
}
