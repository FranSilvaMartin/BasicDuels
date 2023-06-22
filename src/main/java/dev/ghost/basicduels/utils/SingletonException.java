package dev.ghost.basicduels.utils;

public class SingletonException extends RuntimeException {

    public SingletonException(String className) {
        super("Two instances of class " + className + " created! Please create only one instance!");
    }
}