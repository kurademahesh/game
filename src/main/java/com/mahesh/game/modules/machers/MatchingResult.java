package com.mahesh.game.modules.machers;

/**
 * @author Mahesh
 * 
 * Holds the matching results combination details
 */
import java.math.BigDecimal;

import com.mahesh.game.config.CombinationGroup;

public record MatchingResult(String symbol, String combination, CombinationGroup group, BigDecimal rewardMultiplier) {
}
