package com.api.data;

import com.api.entity.User;
import net.datafaker.Faker;

public class UserBuilder {
    public static User getUserData() {
        final Faker faker = new Faker();
        return User.builder().email(faker.internet().emailAddress()).password(faker.internet().password())
                .build();
    }
}
