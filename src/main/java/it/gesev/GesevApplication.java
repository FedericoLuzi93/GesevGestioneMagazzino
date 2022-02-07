package it.gesev;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
public class GesevApplication 
{
	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
		
	}

	public static void main(String[] args) 
	{
		SpringApplication.run(GesevApplication.class, args);
	}

}
