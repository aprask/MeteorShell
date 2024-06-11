package weatherapp.api.weathercommandapp.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import weatherapp.api.weathercommandapp.service.WeatherService;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

@ShellComponent
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class WeatherDataCommands {

    private final WeatherService weatherService;

    @ShellMethod(key = "rj", value = "I will run the batch job procedure", group = "batch")
    private String runJob() {
        return weatherService.runJob();
    }

    @ShellMethod(key = "gw", value = "Enter a location to get the weather for that location", group = "weather")
    public void getWeatherResponse(@ShellOption(help = "The location of interest") String location) {

        String scriptPath = "src/main/resources/scripts/weather-scraper.sh";
        String outputFile = "src/main/resources/scripts/unclean-output.txt";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(scriptPath, location);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            FileWriter writer = new FileWriter(outputFile);
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write('\n');
            }
            process.waitFor();
            writer.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
