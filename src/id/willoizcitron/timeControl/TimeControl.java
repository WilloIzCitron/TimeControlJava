package id.willoizcitron.timeControl;

import id.willoizcitron.timeControl.ui.*;
import mindustry.*;
import mindustry.mod.*;

public class TimeControl extends Mod{
    @Override
    public void loadContent(){
        Settings.load();
        Controller.init();
    }
}
