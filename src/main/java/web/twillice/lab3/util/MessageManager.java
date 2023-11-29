package web.twillice.lab3.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public class MessageManager {
    public static void message(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public static void error(String error) {
        message(FacesMessage.SEVERITY_ERROR, "Error", error);
    }
}
