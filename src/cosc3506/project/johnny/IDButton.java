package cosc3506.project.johnny;


import javafx.scene.control.Button;

class IDButton extends Button {

    int id;

    IDButton(int id, String msg) {
        super(msg);
        this.id = id;
    }
}
