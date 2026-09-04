package com.personalplatform;

import com.personalplatform.model.TestData;
import com.personalplatform.repos.TestDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PersonalPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalPlatformApplication.class, args);
    }

    @Bean
    CommandLineRunner test(TestDataRepository repository) {
        return args -> {

            TestData data = new TestData();
            data.setKey("test");
            data.setValue("Hello Render!");

            repository.save(data);

            System.out.println("Saved: " + data.getId());

            repository.findAll().forEach(item ->
                    System.out.println(
                            item.getKey() + " = " + item.getValue()
                    )
            );
        };
    }
}


