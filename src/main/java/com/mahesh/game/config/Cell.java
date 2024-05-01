package com.mahesh.game.config;

import com.fasterxml.jackson.annotation.JsonCreator;

public record Cell(int row, int column) {

	@JsonCreator
	public static Cell from(String s) {
		String[] split = s.split(":");
		return new Cell(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
	}
}
