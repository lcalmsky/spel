package io.lcalmsky.spel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpelApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpelApplication.class, args);
    }

    @Value("#{1 + 1}")
    private int value;

    @Value("#{'heungmin' + ' ' + 'Son'}")
    private String name;

    @Value("#{1 + 1 eq 2 }")
    private boolean equals;

    @Value("${foo.bar}")
    private int bar;
    @Value("${foo.baz}")
    private String baz;
    @Value("${foo.qux}")
    private boolean qux;
    @Value("${foo.quux:default value}")
    private String quux;
    @Value("#{${foo.quux:'default value'}.replace(' ', '')}")
    private String quuxWithoutSpace;

    @Value("#{properties.number}")
    private int number;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("expressions");
        System.out.println("value = " + value);
        System.out.println("name = " + name);
        System.out.println("equals = " + equals);

        System.out.println("references");
        System.out.println("bar = " + bar);
        System.out.println("baz = " + baz);
        System.out.println("qux = " + qux);
        System.out.println("quux = " + quux);
        System.out.println("quuxWithoutSpace = " + quuxWithoutSpace);

        System.out.println("number from bean = " + number);
    }
}
