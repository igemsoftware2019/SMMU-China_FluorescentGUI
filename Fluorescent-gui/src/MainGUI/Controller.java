package MainGUI;

public class Controller {
    public static void controller(Integer percentage, String dialog) {
        Pmonitor.setvalue(percentage);
        Pmonitor.alert.setText(dialog);
    }
}
