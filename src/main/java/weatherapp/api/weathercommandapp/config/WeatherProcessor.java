package weatherapp.api.weathercommandapp.config;

import org.springframework.batch.item.ItemProcessor;
import weatherapp.api.weathercommandapp.dto.WeatherDto;
import weatherapp.api.weathercommandapp.model.Weather;

import java.util.Random;

public class WeatherProcessor implements ItemProcessor<WeatherDto, Weather> {

    @Override
    public Weather process(WeatherDto weatherDto) {
        Weather weather = new Weather();
        weather.setId(new Random().nextLong());
        weather.setTemp(weatherDto.getTemp());
        weather.setLocation(weatherDto.getLocation());
        return weather;
    }
}
