package com.laila.pet_symptom_tracker.mainconfig;

import java.io.PrintStream;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

public class CustomBanner implements Banner {
  private static final String seaGreen = "\u001B[38;2;0;145;110m";

  private static final String orchidPink = "\u001B[38;2;238;207;212m";
  private static final String chinaRose = "\u001B[38;2;171;79;104m";

  private static final String BANNER =
      orchidPink
          + "\n"
          + "       ,gggg,                                        \n"
          + "      d8\" \"8I                     ,dPYb,             \n"
          + "      88  ,dP                     IP'`Yb             \n"
          + "   8888888P\"                 gg   I8  8I             \n"
          + "      88                     \"\"   I8  8'             \n"
          + "      88          ,gggg,gg   gg   I8 dP    ,gggg,gg  \n"
          + " ,aa,_88         dP\"  \"Y8I   88   I8dP    dP\"  \"Y8I  \n"
          + "dP\" \"88P        i8'    ,8I   88   I8P    i8'    ,8I  \n"
          + "Yb,_,d88b,,_   ,d8,   ,d8b,_,88,_,d8b,_ ,d8,   ,d8b, \n"
          + " \"Y8P\"  \"Y88888P\"Y8888P\"`Y88P\"\"Y88P'\"Y88P\"Y8888P\"`Y8 \n"
          + chinaRose
          + "Don't forget to have fun! \n\n"
          + seaGreen;

  @Override
  public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
    out.println(BANNER);
  }
}
