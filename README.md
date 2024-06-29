
# MeteorShell
MeteorShell is a CLI application powered by Spring Shell and Spring Batch. Its primary function is to fetch weather data via shell (bash) commands. The application processes and refines this data, persisting it into a datasource using a batch job. This project is currently ongoing with updates.

## System Requirements

- JDK 17
- Spring 6
- Maven 3.9.8
- Spring Shell
- Spring Batch
- Spring JPA
- Bash

## Setup

1. **Clone the repository**:
   ```bash
   git clone [repository-url]
   cd MeteorShell-main
   ```

2. **Build the project**:
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

## Usage

MeteorShell provides the following main commands:

- **Run a job**:
  ```bash
  rj
  ```
  This command triggers the batch job to process and store the weather data.

- **Get weather data**:
  ```bash
  gw <weather-location>
  ```
  Replace `<weather-location>` with the desired location to fetch weather data. This command will execute the shell commands necessary to retrieve and prepare the data.

## Logs

The application logs are stored in `spring-shell.log` and `user-service.log`. These can be consulted for detailed operational traces and debugging.

## Contributing

Contributions are welcome. Please fork the repository and submit pull requests to the main branch.

## License

Copyright 2024 Andrew Terrence Praskala

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
