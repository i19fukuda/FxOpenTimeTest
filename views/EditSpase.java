package views;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import midi.MidiConductor;

public class EditSpase {
    final private int LINE_WIDE_SIZE = 48;
    final private int MAX_NOTE_SIZE = 127;
    final private int TEMPO = 120;
    private int maxRootWidth = 6000;
    private int maxRootHeight = 5000;
    private ScrollPane editSpaseRoot;
    private AnchorPane spase;
    private MidiConductor midiConductor;
    private ArrayList<NoteRect> notes;

    public EditSpase (MidiConductor midiConductor){
        this.notes = new ArrayList<>();
        this.midiConductor = midiConductor;
        this.editSpaseRoot = new ScrollPane();
        /*
        Rectangle rect = new Rectangle(2000,2040,Color.RED);
        this.editSpaseRoot.setContent(rect);
        this.editSpaseRoot.setPrefSize(1900, 500);
        */
        //Pane spase = new Pane();
        this.spase = new AnchorPane();
        ArrayList<Line> xLines = new ArrayList<>();
        ArrayList<Line> yLines = new ArrayList<>();
        ArrayList<Label>xLab = new ArrayList<>();
        ArrayList<Label>yLab = new ArrayList<>();
        for(int xPoint=0; xPoint<this.maxRootWidth; xPoint+=LINE_WIDE_SIZE){
            xLines.add(new Line(xPoint, 0, xPoint, this.maxRootHeight));

            Label xLabel = new Label(Integer.toString(xPoint));
            AnchorPane.setTopAnchor(xLabel, 0.0);
            AnchorPane.setLeftAnchor(xLabel,(double)xPoint);
            xLab.add(xLabel);

            // System.out.println(xPoint);
        }
        for(int yPoint=0; yPoint<maxRootHeight; yPoint+=25){
            yLines.add(new Line(0, yPoint, this.maxRootWidth, yPoint));

            Label yLabel = new Label(Integer.toString(yPoint/25));
            AnchorPane.setTopAnchor(yLabel, (double)yPoint);
            AnchorPane.setLeftAnchor(yLabel,0.0);
            yLab.add(yLabel);
        }

        for(Line l:xLines){
            spase.getChildren().add(l);
            // System.out.println(l.getStartX() + "," + l.getStartY()+ "," + l.getEndX() + "," + l.getEndY());
        }
        for(Line l:yLines){
            spase.getChildren().add(l);
        }
        for(Label l:xLab){
            spase.getChildren().add(l);
        }
        for(Label l:yLab){
            spase.getChildren().add(l);
        }
        this.editSpaseRoot.setContent(spase);

        /*
        NoteRect sampleNote = new NoteRect(50, 200, 0);
        Rectangle noteRect = sampleNote.getRect();
        AnchorPane.setLeftAnchor(noteRect, sampleNote.getnoteStartTick());
        AnchorPane.setTopAnchor(noteRect, sampleNote.getNotePich());

        spase.getChildren().add(sampleNote.getRect());
        */

        this.spase.setOnMouseClicked(event -> clicEventHandler(event));
        this.editSpaseRoot.setContent(spase);
        this.editSpaseRoot.setPrefSize(1900, 500);
    }

    private void clicEventHandler(MouseEvent event){
        // 矩形を追加していく．音の情報をもった特殊な長方形
        if(event.getClickCount() == 2){
            double x = event.getX();
            double y = event.getY();

            //todo
            x = x - (x%LINE_WIDE_SIZE);
            y = y - (y%25);

            NoteRect noteRect = new NoteRect((int)y/25, (long) 12, (long)x);
            Rectangle rect = noteRect.getRect();
            System.out.println(noteRect.getnoteLength());
            AnchorPane.setLeftAnchor(rect, x);
            AnchorPane.setTopAnchor( rect, y);

            this.notes.add(noteRect);
            this.spase.getChildren().add(rect);
        }

    }

    // 多分使ってない
    public void play(long startTick){
        long noteLength,noteStartTick;
        int volume,notePich;
        this.midiConductor.removeAllNoteFromeTrack();
        for(NoteRect note:this.notes){
            notePich = note.getNotePich();
            noteLength = note.getnoteLength();
            noteStartTick = note.getnoteStartTick();
            volume = 127;


            //System.out.println("seted length = " +  noteLength);
            this.midiConductor.setNote(notePich, volume, noteStartTick, noteLength);
        }
        this.midiConductor.play(0);
    }

    public ScrollPane getEditSpaseRoot(){
        return this.editSpaseRoot;
    }

    public ArrayList<NoteRect> getNoteRects(){
        return this.notes;
    }

    public void removeNoteRect(long noteId){
        for(int i=0;i<this.notes.size();i++){
            if(this.notes.get(i).getNoteId() == noteId){
                Rectangle rect = notes.get(i).getRect();
                this.spase.getChildren().remove(rect);
                this.notes.remove(i);
            }
        }
    }
}
