package weatherapp.api.weathercommandapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import weatherapp.api.weathercommandapp.dto.WeatherDto;
import weatherapp.api.weathercommandapp.mapper.WeatherMapper;
import weatherapp.api.weathercommandapp.model.Weather;
import weatherapp.api.weathercommandapp.repo.WeatherDataRepository;


@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final WeatherDataRepository weatherDataRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;

    @Bean
    public FlatFileItemReader<WeatherDto> reader() {
        FlatFileItemReader<WeatherDto> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("/home/andrewpraskala/Programming/WeatherCLI/WeatherCommandApp/output.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    public WeatherProcessor processor() {
        return new WeatherProcessor();
    }

    @Bean
    public RepositoryItemWriter<Weather> writer() {
        RepositoryItemWriter<Weather> writer = new RepositoryItemWriter<>();
        writer.setRepository(weatherDataRepository);
        writer.setMethodName("save"); // TODO write custom query in repo
        return writer;
    }

    @Bean
    public Step importStep() {
        return new StepBuilder("csvImport", jobRepository)
                .<WeatherDto, Weather>chunk(10, platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job executeJob() {
        return new JobBuilder("importWeather", jobRepository)
                .start(importStep())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

    private LineMapper<WeatherDto> lineMapper() {
        DefaultLineMapper<WeatherDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        String[] tokens = { "location", "temp" };
        lineTokenizer.setNames(tokens);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new WeatherMapper());

        return lineMapper;
    }

}
