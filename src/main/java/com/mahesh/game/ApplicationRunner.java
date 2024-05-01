
package com.mahesh.game;

import java.io.File;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mahesh.game.config.Configuration;
import com.mahesh.game.modules.GameResult;
import com.mahesh.game.modules.ScratchGame;
import com.mahesh.game.utils.CommandLineParserUtil;

public class ApplicationRunner {
	public static void main(String[] args) {
		CommandLineParserUtil parser = new CommandLineParserUtil();
		if (parser.validate(args)) {
			String configFile = parser.getConfigFilePath();
			BigDecimal bettingAmount = parser.getBettingAmount();
			try {
				ObjectMapper objectMapper = getJsonBuilder();
				Configuration configuration = (Configuration) objectMapper.readValue(new File(configFile),
						Configuration.class);
				long startTime = System.currentTimeMillis();
				ScratchGame scratchGame = new ScratchGame(configuration);
				GameResult gameResult = scratchGame.play(bettingAmount);
				System.out.println("Total time elapsed: " + (System.currentTimeMillis() - startTime) + " ms");

				objectMapper.writerWithDefaultPrettyPrinter().writeValue(System.out, gameResult);
				System.out.println(gameResult);
			} catch (Exception e) {
				System.out.println("Error occured while running scratch game: " + e.getMessage());
			}
		} else {
			System.out.println("Invalid parameters");
		}
	}

	private static ObjectMapper getJsonBuilder() {
		JsonMapper.Builder builder = JsonMapper.builder();
		builder.enable(new MapperFeature[] { MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS });
		builder.enable(new JsonParser.Feature[] { JsonParser.Feature.ALLOW_COMMENTS });
		ObjectMapper objectMapper = builder.build();
		return objectMapper;
	}
}
