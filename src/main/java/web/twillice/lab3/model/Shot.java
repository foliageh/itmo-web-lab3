package web.twillice.lab3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor @Getter @ToString
public class Shot implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) @Setter
    private @NotNull Double x;
    @Column(nullable = false) @Setter
    private @NotNull Double y;
    @Column(nullable = false) @Setter
    private @NotNull Double r;
    @Column(nullable = false)
    private boolean inArea;
    @Column(nullable = false)
    private String shotTime;

    public Shot(Shot shot) {
        this.x = shot.x;
        this.y = shot.y;
        this.r = shot.r;
    }

    @PrePersist
    protected void prePersist() {
        this.shotTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.inArea = checkHit();
    }

    private boolean checkHit() {
        boolean area1_hit = x >= 0 && y >= 0 && x <= r && y <= r / 2;
        boolean area2_hit = x >= 0 && y <= 0 && y >= x - r / 2;
        boolean area3_hit = x <= 0 && y >= 0 && x * x + y * y <= r * r;
        return area1_hit || area2_hit || area3_hit;
    }
}
