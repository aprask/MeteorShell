package weatherapp.api.weathercommandapp.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import weatherapp.api.weathercommandapp.service.WeatherService;

import java.io.*;

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

        String scrapingScriptPath = "src/main/resources/scripts/weather-scraper.sh";
        String scrapingScriptOutput = "src/main/resources/scripts/output.txt";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(scrapingScriptPath, location);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            FileWriter writer = new FileWriter(scrapingScriptOutput);
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write('\n');
            }
            cleanData(new File("src/main/resources/scripts/output.txt"));
            process.waitFor();
            writer.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @ShellMethod(key = "cln", value = "Clean data", group = "util")
    private void cleanData(@ShellOption(help = "Passed in file for cleaning") File file) {

        String cleaningScriptPath = "src/main/resources/scripts/clean-weather-data.sh";
        String cleaningScriptOutput = "src/main/resources/scripts/output.txt";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(cleaningScriptPath, file.getPath());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            FileWriter writer = new FileWriter(cleaningScriptOutput);
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
