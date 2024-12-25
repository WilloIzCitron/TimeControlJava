package id.willoizcitron.timeControl.ui;


import arc.*;
import arc.scene.ui.*;
import mindustry.*;
import mindustry.game.EventType;

public class Settings {
    public static void load(){
        Events.on(EventType.ClientLoadEvent.class, e -> {
            Vars.ui.settings.addCategory("Time Control", t -> {
                t.sliderPref("multiplier",0,2, 10, 1, i ->  i+"x");
            });
        });
    }
}
