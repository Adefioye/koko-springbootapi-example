package com.koko;

import com.github.javafaker.Faker;
import com.koko.customer.Customer;
import com.koko.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import java.util.Random;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();

//        for (String beanDefinitionName: beanDefinitionNames) {
//            System.out.println(beanDefinitionName);
//        }

    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
           Faker faker = new Faker();
           String firstName = faker.name().firstName();
           String lastName = faker.name().lastName();
           Random random = new Random();
           Customer customer = new Customer(
                   "%s %s".formatted(firstName, lastName),
                   "%s.%s@example.com".formatted(firstName.toLowerCase(), lastName.toLowerCase( )),
                   random.nextInt(16, 99)
                   );
           customerRepository.save(customer);
        };

    }


//    @GetMapping("/greet")
//    public GreetResponse greet(
//            @RequestParam(value = "name", required = false) String name) {
//
//        String greeting = name == null || name.isBlank() ? "Hello" : "Hello " + name;
//        return new GreetResponse(
//                greeting,
//                List.of("Java", "Javascript", "Python"),
//                new Person("Koko", 30, 30000)
//        );
//    }
//
//    record Person(String name, int age, double savings) {};
//
//    record GreetResponse(
//            String greeting,
//            List<String> favProgLanguages,
//            Person person
//    ) {}
}


