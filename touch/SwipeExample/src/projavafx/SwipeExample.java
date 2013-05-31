/*
 * Copyright (c) 2013, Pro JavaFX Authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JFXtras nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * SwipeExample.fx - A JavaFX touch API example
 *
 *  Developed 2013 by James L. Weaver jim.weaver [at] javafxpert.com
 *  as a JavaFX 8 example for the Pro JavaFX 8 book.
 */

package projavafx;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Jim
 */
public class SwipeExample extends Application {
    
  private static final double RECT_WIDTH = 400;
  private static final double RECT_HEIGHT = 300;
  private static final Duration dur =
    Duration.millis(200);
  
  private final Image swipeImg =
     new Image(getClass().getResourceAsStream("swipe.jpg"));
  
  ImageView rect = new ImageView(swipeImg);    

  TranslateTransition rectTT =
    new TranslateTransition(dur, rect);
  
  @Override
  public void start(Stage primaryStage) {
    Pane root = new Pane();
    root.getChildren().add(rect);

    rect.setOnSwipeRight(e -> {
      if (rect.getTranslateX() <= 0) {
        rectTT.setByX(RECT_WIDTH);
        rectTT.setByY(0);
        rectTT.playFromStart();
      }
    });

    rect.setOnSwipeLeft(e -> {
      if (rect.getTranslateX() > 0) {
        rectTT.setByX(-RECT_WIDTH);
        rectTT.setByY(0);
        rectTT.playFromStart();
      }
    });

    rect.setOnSwipeDown(e -> {
      if (rect.getTranslateY() <= 0) {
        rectTT.setByX(0);
        rectTT.setByY(RECT_HEIGHT);
        rectTT.playFromStart();
      }
    });

    rect.setOnSwipeUp(e -> {
      if (rect.getTranslateY() > 0) {
        rectTT.setByX(0);
        rectTT.setByY(-RECT_HEIGHT);
        rectTT.playFromStart();
      }
    });

    Scene scene = new Scene(root, 800, 600);

    primaryStage.setTitle("Swipe Example");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
