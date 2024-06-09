package weatherapp.api.weathercommandapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
public class Weather {

    @Id
    @GeneratedValue
    private Long id;

    private String location;

    private LocalDate date;

    private int temp;
}
