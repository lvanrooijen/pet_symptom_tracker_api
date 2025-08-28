package com.laila.pet_symptom_tracker.mainconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColoredLogger {
  private static final String RED = "\u001B[38;2;255;0;0m";
  private static final String BLUE = "\u001B[38;2;0;120;255m";
  private static final String GREEN = "\u001B[38;2;0;200;0m";
  private static final String PINK = "\u001B[38;2;255;105;180m";
  private static final String RESET_COLOR = "\u001B[0m";
  private static final String CUSTOM_RESET_COLOR = "\u001B[38;2;0;145;110m";
  private static final Logger log = LoggerFactory.getLogger(ColoredLogger.class);

  public static void logWarning(String message) {
    log.warn(RED + "{}" + CUSTOM_RESET_COLOR, message);
  }

  public static void logInBlue(String message) {
    log.info(BLUE + "{}" + CUSTOM_RESET_COLOR, message);
  }

  public static void logInGreen(String message) {
    log.info(GREEN + "{}" + CUSTOM_RESET_COLOR, message);
  }

  public static void logCustomInColor(int red, int green, int blue, String message) {
    String color = "\u001B" + "[38;2;" + red + ";" + green + ";" + blue + "m";
    log.info("{}{}" + CUSTOM_RESET_COLOR, color, message);
  }

  public static void prettyInPink(String message) {
    log.info(PINK + "{}" + CUSTOM_RESET_COLOR, message);
  }
}
