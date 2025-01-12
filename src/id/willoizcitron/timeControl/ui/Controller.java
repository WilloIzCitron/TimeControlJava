package id.willoizcitron.timeControl.ui;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.scene.event.EventListener;
import arc.scene.event.SceneEvent;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import blui.ui.*;
import mindustry.Vars;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;

public class Controller {
    private static Color[] cols = {Pal.lancerLaser, Pal.accent, Pal.redDust};
    private static Slider timeSlider = null;
    private static float curSpeed = 0;


    public static void init() {
        if (!Vars.headless) {
            Events.on(EventType.ClientLoadEvent.class, e -> {
                if(Vars.mods.getMod("time-control") != null){
                    Vars.ui.showConfirm("Conflicting Mod", "You just enabled Time Control (by sk7725) and Time Control Java at same time. You must disable Time Control mod or press continue to dispose it.", ()-> {Vars.mods.getMod("time-control").dispose();});
                }
                sliderTable();
            });
        }
    }
    public static void sliderTable(){
        BLSetup.addTable(table -> {
            table.table(Tex.buttonEdge3, t -> {
                t.name = "tcj-slidertable";
                timeSlider = new Slider(-8, 8, 1, false);
                timeSlider.setValue(0);

                Label l = t.labelWrap("[accent]x1").grow().width((float) (10.5 * 10)).get();
                l.addListener(event -> {
                    curSpeed = Mathf.clamp(curSpeed, -Core.settings.getInt("multiplier"), Core.settings.getInt("multiplier")) - 1;
                    return false;
                });
                l.setAlignment(0);
                Label.LabelStyle lStyle = l.getStyle();
                lStyle.background = Tex.underline;

                ImageButton b = t.button(new TextureRegionDrawable(Icon.refresh), 24, () -> {timeSlider.setValue(0); curSpeed = 1;}).padLeft(6).get();
                b.getStyle().imageUpColor = Pal.accent;
                t.add(timeSlider).padLeft(6).minWidth(200);
                timeSlider.moved(v -> {
                        curSpeed = v;
                float speed = Mathf.pow(Core.settings.getInt("multiplier"), v);
                Time.setDeltaProvider(() -> Math.min(Core.graphics.getDeltaTime() * 60 * speed, 3 * speed));

                Tmp.c1.lerp(cols, (timeSlider.getValue() + 8) / 16);

                l.setText(speedText((int)v));
        });


                if (Vars.mobile) {
                    table.moveBy(0, Scl.scl(46));
                }
            });
        }, Controller::visibility);
    }


    private static boolean visibility(){
        if(!Vars.ui.hudfrag.shown || Vars.ui.minimapfrag.shown()) return false;
        if(!Vars.mobile) return true;

        InputHandler input = Vars.control.input;
        return input.lastSchematic == null || input.selectPlans.isEmpty();
    }

    private static String speedText(float speed){
        Tmp.c1.lerp(cols, (speed + 8) / 16);
        String text = "[#" + Tmp.c1.toString() + "]";
        if(speed >= 0){
            text += "x" + (int) Mathf.pow(Core.settings.getInt("multiplier"), speed);
        }else{
            text += "x1/" + (int) Mathf.pow(Core.settings.getInt("multiplier"), Math.abs(speed));
        }
        return text;
    }

}
