package weatherapp.api.weathercommandapp.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class WeatherService {

    private final JobLauncher jobLauncher;
    private final Job job;

    public String runJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            if (jobExecution.getStatus().isUnsuccessful()) {
                return "Job Failed!";
            } else {
                return "Job Succeeded!";
            }
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }

    public void retrieveWeather(String location) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\scripts\\weather-scraper.sh");
        } else {
            builder.command("sh", "-c", System.getProperty("user.dir") +
                    "src/main/resources/scripts/weather-scraper.sh");
        }

        ExecutorService pool = Executors.newSingleThreadExecutor();

        try {
            Process process = builder.start();
            ProcessReader task = new ProcessReader(process.getInputStream());
            Future<List<String>> future = pool.submit(task);

            List<String> results = future.get();

            for(String res : results) {
                System.out.println(res);
            }
            int exitCode = process.waitFor();
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            pool.shutdown();
        }
    }

    @AllArgsConstructor
    private static class ProcessReader implements Callable {
        private InputStream inputStream;

        @Override
        public Object call() {
            return new BufferedReader
                    (new InputStreamReader(inputStream)).lines().collect(Collectors.toList());
        }
    }
}
