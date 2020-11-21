package com.task.fbresult.generators;

import com.task.fbresult.model.Role;

import java.util.Arrays;
import java.util.List;

public class RoleGenerator {
    public static List<Role> generate() {
        return Arrays.asList(
                new Role("ADMIN"),
                new Role("USER")
        );
    }
}
