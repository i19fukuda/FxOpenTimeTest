import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import midi.MidiConductor;
import views.EditSpase;
import views.NoteRect;

public class Main extends Application{
    MidiConductor midiConductor = new MidiConductor(120);
    EditSpase editSpase;

    public static void main(String[] args){
        Application.launch(args);
    }

    public void start(Stage stage){

        this.midiConductor = new MidiConductor(120);

        stage.setTitle("second");
        stage.setWidth(1900);
        stage.setHeight(1040);

        VBox root = new VBox();

        Rectangle previewSpase = new Rectangle(1920,400,Color.BLUE);

        //再生ボタン テスト
        Button playButton = new Button("play");
        playButton.setOnAction(event -> playEventHandler(event));


        this.editSpase = new views.EditSpase(this.midiConductor);

        ScrollPane editRoot = editSpase.getEditSpaseRoot();

        root.getChildren().addAll(previewSpase, playButton,editRoot);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }

    public void playEventHandler(ActionEvent event){
        this.midiConductor = new MidiConductor(120);
        ArrayList<NoteRect> rects = new ArrayList<>();
        rects = this.editSpase.getNoteRects();

        long noteLength,noteStartTick;
        int volume,notePich;
        for(NoteRect note:rects){
            System.out.println(note.getNoteId());
            notePich =      note.getNotePich();
            noteLength =    note.getnoteLength();
            noteStartTick = note.getnoteStartTick();
            volume = 127;

            System.out.println("set length = " +  noteLength);

            this.midiConductor.setNote(notePich, volume, noteStartTick, noteLength);
        }
        this.midiConductor.play(0);
    }
}