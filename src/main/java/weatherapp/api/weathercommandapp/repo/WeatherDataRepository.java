package weatherapp.api.weathercommandapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weatherapp.api.weathercommandapp.model.Weather;

@Repository
public interface WeatherDataRepository extends JpaRepository<Weather, Long> {
}
