package com.mahesh.game.utils;

import java.io.File;
import java.math.BigDecimal;

/**
 * Command line argument parser and validator.
 */
public class CommandLineParserUtil {
	private String configFilePath;

	private BigDecimal bettingAmount;

	public boolean validate(String[] args) {
		if (args.length == 0) {
			printHelp();
			return false;
		}
		try {
			for (int i = 0; i < args.length; i++) {
				String arg = args[i];
				switch (arg) {
				case "--config":
					this.configFilePath = args[++i];
					break;
				case "--betting-amount":
					try {
						this.bettingAmount = new BigDecimal(args[++i]);
					} catch (NumberFormatException e) {
						System.err.println("Invalid betting amount: " + args[i]);
						return false;
					}
					break;
				case "--help":
					return printHelp();
				default:
					System.err.println("Unknown argument: " + arg);
					return printHelp();
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Invalid arguments");
			return printHelp();
		}
		if (this.configFilePath == null) {
			System.err.println("Config file path is not provided");
			return false;
		}
		if (!(new File(this.configFilePath)).exists()) {
			System.err.println("Config file does not exist: " + this.configFilePath);
			return false;
		}
		if (this.bettingAmount == null || this.bettingAmount.signum() <= 0) {
			System.err.println("Betting amount is invalid");
			return false;
		}
		return true;
	}

	public String getConfigFilePath() {
		return this.configFilePath;
	}

	public BigDecimal getBettingAmount() {
		return this.bettingAmount;
	}

	private boolean printHelp() {
		System.out.println("Usage: java -jar application.jar [options]");
		System.out.println("Options:");
		System.out.println("  --config <path>            Path to config file");
		System.out.println("  --betting-amount <amount>  Betting amount");
		System.out.println("  --help                     Print help");
		return false;
	}
}
