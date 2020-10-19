package IO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class Input extends JComponent {
    private boolean[] map;

    public Input() {
        map = new boolean[256];
        for (int i = 0; i < map.length; i++) {
            //чтобы ява не выкидывала ошибку
            final int KEY_CODE = i;
            //возращает map в которую мы можем добавлять какие-то значения в кажду/ кнопку и давать им параметр
            //первый аргумент в put отвечает за нажатие кнопки по ключуб
            //второй агрумент это унивкальное название
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(i , 0 , false) , i * 2);
            //каждый раз когда вызываем это имя, то просиходит следущее
            getActionMap().put(i * 2 , new AbstractAction() {
                @Override
                //будет бежать, когда кнопка нажата
                public void actionPerformed(ActionEvent e) {
                    map[KEY_CODE] = true;
                    //когда нажата это кнопка в нашей карте по этому индексу клавиши превтатится в true

                }
            });
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(i , 0 , true) , i * 2 + 1);
            getActionMap().put(i * 2 + 1 , new AbstractAction() {
                @Override
                //будет бежать, когда кнопка нажата
                public void actionPerformed(ActionEvent e) {
                    map[KEY_CODE] = false;
                    //когда нажата это кнопка в нашей карте по этому индексу клавиши превтатится в false

                }
            });


        }

    }

    //чтобы сохранить инкапсуляцию мы возращаем копию этого map
    public boolean[] getMap() {
        return Arrays.copyOf(map , map.length);
    }

    public boolean getKey(int keyCode) {
        return map[keyCode];
    }
}
