package weatherapp.api.weathercommandapp.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import weatherapp.api.weathercommandapp.model.Weather;

import java.time.LocalDate;

public class WeatherMapper implements FieldSetMapper<Weather> {

    @Override
    public Weather mapFieldSet(FieldSet fieldSet) {
        Weather weather = new Weather();
        weather.setId(fieldSet.readLong("id"));
        weather.setLocation(fieldSet.readString("location"));
        weather.setDate(LocalDate.parse(fieldSet.readString("date")));
        weather.setTemp(fieldSet.readInt("temp"));
        return weather;
    }

}
