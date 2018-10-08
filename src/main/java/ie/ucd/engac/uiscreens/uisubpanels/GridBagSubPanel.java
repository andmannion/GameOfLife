package ie.ucd.engac.uiscreens.uisubpanels;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class GridBagSubPanel extends UISubPanel {
    public GridBagSubPanel(GridBagLayout gridBagLayout,List<Component> componentList,List<GridBagConstraints> constraintList){
        JPanel jPanel = new JPanel(gridBagLayout);
        for (Component component:componentList){
            //jPanel.add(component,component.c)
        }

    }

}
