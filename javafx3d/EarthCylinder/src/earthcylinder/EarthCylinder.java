/* 
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Oracle designates this 
 * particular file as subject to the "Classpath" exception as provided 
 * by Oracle in the LICENSE file that accompanied this code. * 
 * This code is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License 
 * version 2 for more details (a copy is included in the LICENSE file that 
 * accompanied this code). 
 * 
 * You should have received a copy of the GNU General Public License version 
 * 2 along with this work; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 * 
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA 
 * or visit www.oracle.com if you need additional information or have any 
 * questions. 
 */ 

package earthcylinder;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage; 

public class EarthCylinder extends Application {
     
  double anchorX, anchorY;
  
  private double anchorAngleX = 0;
  private double anchorAngleY = 0;
  
  private final DoubleProperty angleX = new SimpleDoubleProperty(0);
  private final DoubleProperty angleY = new SimpleDoubleProperty(0);
  
  PerspectiveCamera scenePerspectiveCamera = new PerspectiveCamera(false);
  
  /**     
   * @param args the command line arguments     
   */    
  public static void main(String[] args) {        
    // Remove this line once dirtyopts bug is fixed for 3D primitive        
    System.setProperty("prism.dirtyopts", "false");        
    launch(args);    
  }     
    
  @Override    
  public void start(Stage primaryStage) {        
    primaryStage.setTitle("EarthCylinder");     

    Image diffuseMap = 
      new Image(EarthCylinder.class
        .getResource("earth-mercator.jpg")
        .toExternalForm());

    PhongMaterial earthMaterial = new PhongMaterial();
    earthMaterial.setDiffuseMap(diffuseMap);

    final Cylinder earth = new Cylinder (200, 400);        
    earth.setMaterial(earthMaterial); 
          
    final Group parent = new Group(earth);
    parent.setTranslateX(450);
    parent.setTranslateY(450);
    parent.setTranslateZ(0);

    Rotate xRotate;
    Rotate yRotate;
    parent.getTransforms().setAll(
      xRotate = new Rotate(0, Rotate.X_AXIS),
      yRotate = new Rotate(0, Rotate.Y_AXIS)
    );
    xRotate.angleProperty().bind(angleX);
    yRotate.angleProperty().bind(angleY);
    
    final Group root = new Group();
    root.getChildren().add(parent);
    
    final Scene scene = new Scene(root, 900, 900, true);
    scene.setFill(Color.BLACK);
      
    scene.setOnMousePressed(event -> {                
      anchorX = event.getSceneX();                
      anchorY = event.getSceneY();  
      
      anchorAngleX = angleX.get();
      anchorAngleY = angleY.get();
    });         
      
    scene.setOnMouseDragged(event -> {                
      angleX.set(anchorAngleX - (anchorY -  event.getSceneY()));
      angleY.set(anchorAngleY + anchorX -  event.getSceneX());
    });         
      
    PointLight pointLight = new PointLight(Color.WHITE);
    pointLight.setTranslateX(400);
    pointLight.setTranslateY(400);
    pointLight.setTranslateZ(-3000);
      
    scene.setCamera(scenePerspectiveCamera);
    
    root.getChildren().addAll(pointLight, scenePerspectiveCamera);
      
    primaryStage.setScene(scene);     
    primaryStage.show();    
  }
}