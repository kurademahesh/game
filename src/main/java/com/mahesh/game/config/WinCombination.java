package com.mahesh.game.config;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WinCombination(
		
		@JsonProperty("reward_multiplier") BigDecimal rewardMultiplier, 
		CombinationMatch when,
		CombinationGroup group, int count, 
		@JsonProperty("covered_areas") List<List<Cell>> coveredAreas) {

}
