package com.mahesh.game.components;

import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mahesh.game.config.Configuration;
import com.mahesh.game.modules.GameBoard;
import com.mahesh.game.modules.GameProcessor;
import com.mahesh.game.modules.GameResult;

/**
 * @author Mahesh
 *
 */
class GameProcessorTest {

	private Configuration config;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws IOException {
		objectMapper = getJsonBuilder();
		config = objectMapper.readValue(getClass().getClassLoader().getResource("config.json"), Configuration.class);
	}

	@Test
	void testAnalyzeFail() throws Exception {
		GameProcessor processor = new GameProcessor(config);
		GameBoard gameBoard = new GameBoard(new String[][] { { "A", "B", "C" }, { "E", "B", "5x" }, { "F", "D", "C" } },
				"5x");

		GameResult gameResult = processor.process(gameBoard, new BigDecimal("100"));

		printResult(gameResult);

		assertEquals(ZERO, gameResult.reward());
		assertNull(gameResult.winningCombinations());
		assertNull(gameResult.bonusSymbol());
	}

	// B = 25
	// F = 1.5
	// same_symbol_3_times = 1
	// same_symbol_4_times = 1.5
	// same_symbols_horizontally = 2
	@Test
	void testAnalyzeWin2Symbols() {
		GameProcessor processor = new GameProcessor(config);
		GameBoard gameBoard = new GameBoard(new String[][] { { "B", "B", "B" }, { "F", "F", "F" }, { "C", "F", "C" } },
				null);

		GameResult gameResult = processor.process(gameBoard, new BigDecimal("100"));

		printResult(gameResult);

		assertEquals(5450, gameResult.reward().intValue());
		assertEquals(2, gameResult.winningCombinations().size());
		assertEquals(2, gameResult.winningCombinations().get("B").size());
		assertEquals(2, gameResult.winningCombinations().get("F").size());
		assertTrue(gameResult.winningCombinations().get("B").contains("same_symbol_3_times"));
		assertTrue(gameResult.winningCombinations().get("B").contains("same_symbols_horizontally"));
		assertTrue(gameResult.winningCombinations().get("F").contains("same_symbols_horizontally"));
		assertTrue(gameResult.winningCombinations().get("F").contains("same_symbol_4_times"));
		assertNull(gameResult.bonusSymbol());
	}

	// B = 25
	// same_symbol_6_times = 3
	// same_symbols_horizontally = 2
	@Test
	void testAnalyzeWinMultipleInTheSameGroup() {
		GameProcessor processor = new GameProcessor(config);
		GameBoard gameBoard = new GameBoard(
				new String[][] { { "B", "B", "B" }, { "B", "B", "B" }, { "C", "F", "MISS" } }, "MISS");

		GameResult gameResult = processor.process(gameBoard, new BigDecimal("100"));

		System.out.println(gameBoard);

		assertEquals(15000, gameResult.reward().intValue());
		assertEquals(1, gameResult.winningCombinations().size());
		assertEquals(2, gameResult.winningCombinations().get("B").size());
		assertTrue(gameResult.winningCombinations().get("B").contains("same_symbol_6_times"));
		assertTrue(gameResult.winningCombinations().get("B").contains("same_symbols_horizontally"));
		assertEquals("MISS", gameResult.bonusSymbol());
	}

	@Test
	void testAnalyzeWin3Combinations() {
		GameProcessor processor = new GameProcessor(config);
		GameBoard gameBoard = new GameBoard(
				new String[][] { { "B", "B", "C" }, { "B", "B", "+1000" }, { "B", "D", "B" } }, "+1000");

		GameResult gameResult = processor.process(gameBoard, new BigDecimal("100"));

		// B = 25
		// +1000 = 1000
		// same_symbol_6_times = 3
		// same_symbols_vertically = 2
		// same_symbols_diagonally_left_to_right = 5
		assertEquals(76000, gameResult.reward().intValue());
		assertEquals(1, gameResult.winningCombinations().size());
		assertEquals(3, gameResult.winningCombinations().get("B").size());
		assertTrue(gameResult.winningCombinations().get("B").contains("same_symbol_6_times"));
		assertTrue(gameResult.winningCombinations().get("B").contains("same_symbols_vertically"));
		assertTrue(gameResult.winningCombinations().get("B").contains("same_symbols_diagonally_left_to_right"));
		assertEquals("+1000", gameResult.bonusSymbol());
	}

	@Test
	void testAnalyzeWin_same_symbol_3_times() {
		GameProcessor processor = new GameProcessor(config);
		GameBoard gameBoard = new GameBoard(
				new String[][] { { "A", "B", "C" }, { "E", "B", "10x" }, { "F", "D", "B" } }, "10x");

		GameResult gameResult = processor.process(gameBoard, new BigDecimal("100"));
		assertEquals(25000, gameResult.reward().intValue());
		assertEquals(1, gameResult.winningCombinations().size());
		assertEquals(1, gameResult.winningCombinations().get("B").size());
		assertEquals("same_symbol_3_times", gameResult.winningCombinations().get("B").get(0));
		assertEquals("10x", gameResult.bonusSymbol());
	}

	private static ObjectMapper getJsonBuilder() {
		JsonMapper.Builder builder = JsonMapper.builder();
		builder.enable(new MapperFeature[] { MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS });
		builder.enable(new JsonParser.Feature[] { JsonParser.Feature.ALLOW_COMMENTS });
		ObjectMapper objectMapper = builder.build();
		return objectMapper;
	}

	private void printResult(GameResult gameResult) {
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(System.out, gameResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}