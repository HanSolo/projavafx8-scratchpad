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
 * GesturesExample.fx - A JavaFX touch API example
 *
 *  Developed 2013 by James L. Weaver jim.weaver [at] javafxpert.com
 *  as a JavaFX 8 example for the Pro JavaFX 8 book.
 */

package projavafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Jim
 */
public class GesturesExample extends Application {
    
  private static final double RECT_WIDTH = 400;
  private static final double RECT_HEIGHT = 300;
  
  private final Image gesturesImg =
     new Image(getClass().getResourceAsStream("gestures.jpg"));
  
  ImageView rect = new ImageView(gesturesImg);    

  private double curZoomFactor = 1.0;
  private double curRotateAngle = 0.0;
  private double curPosX = 0.0;
  private double curPosY = 0.0;

  @Override
  public void start(Stage primaryStage) {
    rect.setFitWidth(RECT_WIDTH);
    rect.setFitHeight(RECT_HEIGHT);
    rect.setPreserveRatio(true);
    
    Pane root = new Pane();
    root.getChildren().add(rect);
    rect.setLayoutX(200);
    rect.setLayoutY(150);

    rect.setOnZoomStarted(e -> {
      curZoomFactor = rect.getScaleX();
    });

    rect.setOnZoom(e -> {
      rect.setScaleX(e.getTotalZoomFactor() * curZoomFactor);
      rect.setScaleY(e.getTotalZoomFactor() * curZoomFactor);
    });

    rect.setOnRotationStarted(e -> {
      curRotateAngle = rect.getRotate();
    });

    rect.setOnRotate(e -> {
      rect.setRotate(e.getTotalAngle() + curRotateAngle);
    });

    rect.setOnScrollStarted(e -> {
      curPosX = rect.getLayoutX();
      curPosY = rect.getLayoutY();
    });

    rect.setOnScroll(e -> {
      if (!e.isInertia()) {
        rect.setLayoutX(curPosX + e.getTotalDeltaX());
        rect.setLayoutY(curPosY + e.getTotalDeltaY());
      }
    });

    Scene scene = new Scene(root, 800, 600);

    primaryStage.setTitle("Gestures Example");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
