package sample;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {
    public NumberTextField()
    {
        this.setPromptText("0");
    }

    @Override
    public void replaceText(int i, int i1, String s) {
        if (s.matches("[0-99]")||s.isEmpty())
        super.replaceText(i, i1, s);
    }

    @Override
    public void replaceSelection(String s) {
        super.replaceSelection(s);
    }
}
