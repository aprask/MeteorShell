package weatherapp.api.weathercommandapp.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import weatherapp.api.weathercommandapp.service.WeatherService;

@ShellComponent
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class WeatherDataCommands {

    private final WeatherService weatherService;

    @ShellMethod(key = "runJob", value = "I will run the batch job procedure", group = "batch")
    private String runJob() {
        return weatherService.runJob();
    }

    @ShellMethod(key = "getWeather", value = "Enter a location to get the weather for that location", group = "live")
    private void getWeather(@ShellOption(help = "The location of interest") String location) {
        weatherService.retrieveWeather(location);
    }


}
