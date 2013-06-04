package projavafx.zenguitar.ui;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Application;
import javafx.scene.GroupBuilder;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    Scene scene  = SceneBuilder.create()
      .width(STRING_WIDTH)
      .height(900)
      .root(
          guitarStringsContainer
      )
      .build();

    scene.getStylesheets().addAll(ZenGuitarMain.class
        .getResource("zenGuitar.css").toExternalForm());

      stage.setScene(scene);
      stage.setTitle("ZenGuitar");
      stage.show();
  }
}
