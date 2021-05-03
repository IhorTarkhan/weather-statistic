package org.openjfx;

import org.openjfx.property.DarkSkyProperty;
import org.openjfx.ui.StageWrapper;

public class Main extends StageWrapper {
  public static void main(String[] args) {
    DarkSkyProperty.load(args);
    launch();
  }
}
