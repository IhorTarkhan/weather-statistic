package org.openjfx;

import org.openjfx.property.DarkSkyProperty;

public class Main extends UI {
  public static void main(String[] args) {
    DarkSkyProperty.load(args);
    launch();
  }
}
