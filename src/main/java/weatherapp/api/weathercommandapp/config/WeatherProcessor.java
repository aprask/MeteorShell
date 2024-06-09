package weatherapp.api.weathercommandapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import weatherapp.api.weathercommandapp.model.Weather;

public class WeatherProcessor implements ItemProcessor<Weather, Weather> {

    private static final Logger logger = LoggerFactory.getLogger(WeatherProcessor.class);

    @Override
    public Weather process(Weather weather) {
        logger.info("Processing weather data: {}", weather);
        weather.setId(null);
        return weather;
    }
}
