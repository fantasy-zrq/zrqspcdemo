package com.example.nettydemo.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author zrq
 * @time 2025/8/7 21:27
 * @description
 */
public class WalkFileTreeTest {
    public static void main(String[] args) throws IOException {
        Files.walkFileTree(Path.of("nettydemo/target/classes/com/example/nettydemo"),
                new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        String path = file.toString();
                        if (path.endsWith(".class")) {
                            System.out.println("path = " + path);
                        }
                        return super.visitFile(file, attrs);
                    }
                });
    }
}
