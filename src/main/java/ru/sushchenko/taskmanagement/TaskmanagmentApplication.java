package ru.sushchenko.taskmanagement;

import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.sushchenko.taskmanagement.utils.mapper.CustomModelMapper;

@SpringBootApplication
public class TaskmanagmentApplication {
	@Bean
	public CustomModelMapper modelMapper() {
		CustomModelMapper modelMapper = new CustomModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}
	public static void main(String[] args) {
		SpringApplication.run(TaskmanagmentApplication.class, args);
	}

}
