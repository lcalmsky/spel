package io.lcalmsky.spel;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Properties {
    private int number = 1;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
