package web.twillice.lab3.converters;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;

import java.util.regex.Pattern;


@FacesConverter(value = "XCoordinateConverter", managed = true)
public class XCoordinateConverter implements Converter<Double> {
    @Override
    public Double getAsObject(FacesContext facesContext, UIComponent uiComponent, String x) {
        if (x == null || x.isEmpty()) return null;
        if (!Pattern.matches("(?:-5|\\+?3)(?:[.,]0{1,15})?|(?:-[43210]|\\+?[012])(?:[.,]\\d{1,15})?", x))
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "X: Value doesn't match the pattern.", ""));
        return Double.parseDouble(x.replace(",", "."));
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Double x) {
        return x == null ? "" : x.toString();
    }
}
