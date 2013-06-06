package projavafx.zenguitar.ui;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import org.jfugue.pattern.Pattern;
import org.jfugue.realtime.RealTimePlayer;
import org.jfugue.theory.Note;

import javax.sound.midi.MidiUnavailableException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Jim
 * Date: 5/28/13
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class GuitarString extends Region {
  private RealTimePlayer _player;
  double _width;
  double _height;

  int _openNoteValue;

  int _numFrets;

  Polyline _stringLine;
  Rectangle _rect;
  Line[] _fretLines;
  DropShadow _dropShadowStringLine;

  // Most recent note value played
  //TODO: Remove?
  int _noteValue;

  // Y position of TouchPoint for most recent note played
  double _noteValuePosY;

  // Touchpoint IDs and corresponding note values
  HashMap _tpNoteVals;

  /**
   *
   * @param openNoteValue
   * @param numFrets
   */
  public GuitarString(int openNoteValue, int numFrets, double width, double height) {
    _width = width;
    _height = height;
    _openNoteValue = openNoteValue;
    _numFrets = numFrets;

    _tpNoteVals = new HashMap();

    //setId("fretboard");

    try {
      _player = new RealTimePlayer();
      _player.changeInstrument(24);
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }

    _rect = new Rectangle();
    _rect.setFill(Color.TRANSPARENT);
    _rect.setStroke(Color.TRANSPARENT);

    _fretLines = new Line[_numFrets];
    for (int idx = 0; idx < _numFrets; idx++) {
      _fretLines[idx] = new Line();
      _fretLines[idx].setStroke(Color.rgb(222, 215, 165));
      _fretLines[idx].setStrokeWidth(3);
    }

    InnerShadow stringInnerShadow = new InnerShadow();
    stringInnerShadow.setColor(Color.rgb(0, 0, 0, 0.9));
    stringInnerShadow.setBlurType(BlurType.TWO_PASS_BOX);

    _dropShadowStringLine = new DropShadow();
    _dropShadowStringLine.setColor(Color.rgb(0, 0, 0, 0.65));
    _dropShadowStringLine.setBlurType(BlurType.GAUSSIAN);
    _dropShadowStringLine.setRadius(10);
    _dropShadowStringLine.setOffsetY(13);
    _dropShadowStringLine.setInput(stringInnerShadow);

    _stringLine = new Polyline();
    _stringLine.setStroke(Color.rgb(220, 220, 220));
    _stringLine.setStrokeWidth(10);
    _stringLine.setEffect(_dropShadowStringLine);
    updateStringLine();

    setPrefSize(width, height);
    getChildren().add(_rect);
    for (int idx = 0; idx < _numFrets; idx++) {
      getChildren().add(_fretLines[idx]);
    }
    getChildren().add(_stringLine);

    setOnTouchPressed(te -> handleTouchPressed(te));
    setOnTouchReleased(te -> handleTouchReleased(te));
    setOnTouchMoved(te -> handleTouchMoved(te));
    setOnTouchStationary(te -> handleTouchMoved(te));
  }

  @Override
  public void setPrefSize(double width, double height) {
    super.setPrefSize(width, height);
    _width = width;
    _height = height;
    updateStringLine();
  }

  @Override
  public void setWidth(double width) {
    _width = width;
    updateStringLine();
  }

  @Override
  public void setHeight(double height) {
    _height = height;
    updateStringLine();
  }

  void handleTouchPressed(TouchEvent te) {
    if (isHighestTouchOnString(te)) {
      //release(te.getTouchPoint());
      releaseAll(te);
      _stringLine.setStroke(Color.RED);
      _dropShadowStringLine.setOffsetY(3);
      play(computeNoteValue(te.getTouchPoint().getX()), te.getTouchPoint(), false);
      //System.out.println("Touched");
    }
  }

  void handleTouchReleased(TouchEvent te) {
    _stringLine.setStroke(Color.rgb(220, 220, 220));
    _dropShadowStringLine.setOffsetY(13);
    release(te.getTouchPoint());
    //System.out.println("Released");
  }

  void handleTouchMoved(TouchEvent te) {
    if (isHighestTouchOnString(te)) {
      int noteValue = computeNoteValue(te.getTouchPoint().getX());
      if (noteValue != _noteValue) {
        _stringLine.setStroke(Color.rgb(220, 220, 220));
        release(te.getTouchPoint());
        play(computeNoteValue(te.getTouchPoint().getX()), te.getTouchPoint(), true);
        //System.out.println("Moved to another fret");
      }
      else {
        double bendDist = Math.abs((te.getTouchPoint().getY() - _noteValuePosY));
        double bendPct = bendDist / _height;
        if (bendPct > .25) {
          byte msb = (byte)Math.min(255 * (bendPct - .25), 64);
          _player.changePitchWheel((byte)0, msb);
          //System.out.println("Bending msb:" + msb);
        }

      }
    }
  }

  boolean isHighestTouchOnString(TouchEvent te) {
    double pointX = te.getTouchPoint().getX();
    double maxX = 0.0;
    for (TouchPoint tp : te.getTouchPoints()) {
      if (tp.belongsTo(this)) {
        maxX = Math.max(tp.getX(), maxX);
      }
    }
    return pointX >= maxX;
  }

  void updateStringLine() {
    _rect.setWidth(_width);
    _rect.setHeight(_height);

    for (int idx = 0; idx < _numFrets; idx++) {
      _fretLines[idx].setStartX(idx * (_width / _numFrets));
      _fretLines[idx].setStartY(0);
      _fretLines[idx].setEndX(idx * (_width / _numFrets));
      _fretLines[idx].setEndY(_height);
    };

    _stringLine.getPoints().clear();
    _stringLine.getPoints().addAll(0.0, _height / 2, _width, _height / 2);
  }

  int computeNoteValue(double stringPosX) {
    //TODO: Change to logarithmic scale?
    double noteWidth = _width / _numFrets;
    double noteValue = stringPosX / noteWidth;
    int intNoteValue = new Double(noteValue).intValue();
    return _openNoteValue + intNoteValue;
  }

  private void play(int noteValue, TouchPoint tp, boolean softAttack) {
    _tpNoteVals.put(tp.getId(), new Integer(noteValue));
    //System.out.println("Putting tp:" + tp.getId() + "noteValue:" + noteValue);
    _noteValue = noteValue;
    _noteValuePosY = tp.getY();
    Note note = new Note(noteValue);
    if (softAttack) {
      note.setAttackVelocity((byte)(note.getAttackVelocity() * 0.75));
    }
    Pattern pattern = note.getPattern();
    _player.play(pattern + "s-");
    //System.out.println("Playing:" + pattern + "s-");
  }

  private void release(TouchPoint tp) {
    _player.changePitchWheel((byte)0, (byte)0);
    int noteValue = 0;
    Object nv = _tpNoteVals.get(tp.getId());
    if (nv != null) {
      noteValue = ((Integer)nv).intValue();
      //System.out.println("Got tp:" + tp.getId() + "noteValue:" + noteValue);
    }
    Pattern pattern = new Note(noteValue).getPattern();
    _player.play(pattern + "-s");
  }

  private void releaseAll(TouchEvent te) {
    for (TouchPoint tp : te.getTouchPoints()) {
      if (tp.belongsTo(this)) {
        release(tp);
      }
    }
  }
}
