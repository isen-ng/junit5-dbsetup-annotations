package com.github.isengrim613.junit5;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is the main annotation to use. If this is not specified, the all other annotations will do nothing
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@ExtendWith(DbSetupExtension.class)
public @interface DbSetup {
}
