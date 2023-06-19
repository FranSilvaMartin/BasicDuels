package dev.ghost.basicduels.manager.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String name();

    String desc();

    String perm() default "";

    String[] usage() default {};

    String[] aliases() default {};

    boolean isSubCommand() default false;
}