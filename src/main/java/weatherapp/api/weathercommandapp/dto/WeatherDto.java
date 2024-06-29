package weatherapp.api.weathercommandapp.dto;

import lombok.Data;

@Data
public class WeatherDto {

    private String location;

    private int temp;

}
