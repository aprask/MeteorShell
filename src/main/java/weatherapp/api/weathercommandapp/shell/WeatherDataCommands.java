package weatherapp.api.weathercommandapp.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import weatherapp.api.weathercommandapp.service.JobService;

@ShellComponent
@RequiredArgsConstructor
public class WeatherDataCommands {

    private final JobService job;

    @SuppressWarnings("unused")
    @ShellMethod(key = "runJob", value = "I will run the batch job procedure", group = "batch")
    private String runJob() {
        return job.runJob();
    }

}
