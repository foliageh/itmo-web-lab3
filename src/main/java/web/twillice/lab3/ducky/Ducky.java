package web.twillice.lab3.ducky;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;

@FacesComponent(Ducky.COMPONENT_TYPE)
public class Ducky extends UIComponentBase {
    public static final String COMPONENT_FAMILY = "twillice.Ducky";
    public static final String COMPONENT_TYPE = "twillice.Ducky";

    public Ducky() {
        setRendererType(DuckyRenderer.RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public boolean isDucksShown() {
        return (Boolean) getStateHelper().eval(PropertyKeys.ducksShown, false);
    }

    public void setDucksShown(boolean ducksShown) {
        getStateHelper().put(PropertyKeys.ducksShown, ducksShown);
    }

    public int getMinValue() {
        return (Integer) getStateHelper().eval(PropertyKeys.minValue, 0);
    }

    public void setMinValue(int minValue) {
        getStateHelper().put(PropertyKeys.minValue, minValue);
    }

    enum PropertyKeys {
        ducksShown, minValue
    }
}
