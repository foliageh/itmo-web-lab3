package web.twillice.lab3.ducky;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Named @ViewScoped
@Getter @Setter
public class DuckyBean implements Serializable {
    private int minValue = 1;
    private boolean ducksShown;
}
