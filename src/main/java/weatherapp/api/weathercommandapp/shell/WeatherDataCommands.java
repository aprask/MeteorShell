package weatherapp.api.weathercommandapp.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import weatherapp.api.weathercommandapp.service.WeatherService;

import java.io.*;

@ShellComponent
@RequiredArgsConstructor
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
            process.waitFor();
            writer.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        cleanData();
        convertData();
    }

    public void cleanData() {
        String cleaningScriptPath = "src/main/resources/scripts/clean-weather-data.sh";
        String cleaningScriptOutput = "src/main/resources/scripts/output.txt";

        try {
            File scriptFile = new File(cleaningScriptPath);
            if (!scriptFile.exists() || !scriptFile.canExecute()) {
                throw new RuntimeException("Script file does not exist or is not executable: " + cleaningScriptPath);
            }
            ProcessBuilder processBuilder = new ProcessBuilder(cleaningScriptPath, cleaningScriptOutput);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while executing the script.", e);
        }
    }

    public void convertData() {
        String outputPath = "output.txt";
        String conversionScriptPath = "src/main/resources/scripts/csv-convert.sh";
        String conversionScriptOutput = "src/main/resources/scripts/output.csv";

        try {
            File scriptFile = new File(conversionScriptPath);
            if (!scriptFile.exists() || !scriptFile.canExecute()) {
                throw new RuntimeException("Script file does not exist or is not executable: " + conversionScriptPath);
            }
            ProcessBuilder processBuilder = new ProcessBuilder(conversionScriptPath, outputPath);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            try (BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(conversionScriptOutput))) {

                String line;
                while ((line = stdOutput.readLine()) != null) {
                    writer.write(line);
                    writer.write('\n');
                }
                writer.close();
                process.waitFor();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("An error occurred while executing the script.", e);
        }
    }

}
