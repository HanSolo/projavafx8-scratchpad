package projavafx.zenguitar.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ZenGuitarMain extends Application {
  static int LOW_NOTE = 18;
  static int NUM_FRETS = 13;
  static double STRING_WIDTH = 1900;
  static double STRING_HEIGHT = 100;

  public static void main(String[] args) {
      Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    VBox guitarStringsContainer = new VBox();
    guitarStringsContainer.setSpacing(0);
    guitarStringsContainer.setId("background");
    guitarStringsContainer.getChildren().addAll(
        new Rectangle(STRING_WIDTH, STRING_HEIGHT / 2, Color.TRANSPARENT),
        new GuitarString(LOW_NOTE + 46, NUM_FRETS, STRING_WIDTH, STRING_HEIGHT),
        new GuitarString(LOW_NOTE + 41, NUM_FRETS, STRING_WIDTH, STRING_HEIGHT),
        new GuitarString(LOW_NOTE + 37, NUM_FRETS, STRING_WIDTH, STRING_HEIGHT),
        new GuitarString(LOW_NOTE + 32, NUM_FRETS, STRING_WIDTH, STRING_HEIGHT),
        new GuitarString(LOW_NOTE + 27, NUM_FRETS, STRING_WIDTH, STRING_HEIGHT),
        new GuitarString(LOW_NOTE + 22, NUM_FRETS, STRING_WIDTH, STRING_HEIGHT),
        new GuitarString(LOW_NOTE + 17, NUM_FRETS, STRING_WIDTH, STRING_HEIGHT),
        new GuitarString(LOW_NOTE + 12, NUM_FRETS, STRING_WIDTH, STRING_HEIGHT)
    );

    Scene scene = new Scene(guitarStringsContainer, STRING_WIDTH, 900);
    scene.getStylesheets().addAll(ZenGuitarMain.class
        .getResource("zenGuitar.css").toExternalForm());

      stage.setScene(scene);
      stage.setTitle("ZenGuitar");
      stage.show();
  }
}
