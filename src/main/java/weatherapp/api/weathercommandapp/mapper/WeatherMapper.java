package weatherapp.api.weathercommandapp.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import weatherapp.api.weathercommandapp.dto.WeatherDto;
import weatherapp.api.weathercommandapp.model.Weather;

import java.time.LocalDate;

public class WeatherMapper implements FieldSetMapper<WeatherDto> {

    @Override
    public WeatherDto mapFieldSet(FieldSet fieldSet) {
        WeatherDto weatherDto = new WeatherDto();
        weatherDto.setLocation(fieldSet.readString("location"));
        weatherDto.setTemp(fieldSet.readInt("temp"));
        return weatherDto;
    }

}
