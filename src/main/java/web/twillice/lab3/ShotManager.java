package web.twillice.lab3;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import web.twillice.lab3.model.Shot;
import web.twillice.lab3.repository.ShotRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named @SessionScoped
public class ShotManager implements Serializable {
    @Inject
    private ShotRepository repository;
    @Getter
    private final Shot shot = new Shot();
    @Getter
    private final List<Shot> shots = new ArrayList<>();
    @Getter
    private final List<Double> availableR = List.of(1.0, 1.5, 2.0, 2.5, 3.0);
    @Getter @Setter
    private List<Double> selectedR = new ArrayList<>();

    public void shoot() {
        for (Double r : selectedR) {
            shot.setR(r);
            Shot newShot = repository.create(shot);
            shots.add(newShot);
        }
    }
}
