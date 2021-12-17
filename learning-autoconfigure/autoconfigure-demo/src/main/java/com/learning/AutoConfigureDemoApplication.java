package com.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.loader.ExecutableArchiveLauncher;
import org.springframework.boot.loader.Launcher;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.JarFileArchive;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.List;

@SpringBootApplication
public class AutoConfigureDemoApplication {

    public static void main(String[] args) throws URISyntaxException, IOException {
        ProtectionDomain protectionDomain = Launcher.class.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URI location = (codeSource != null) ? codeSource.getLocation().toURI() : null;
        String path = (location != null) ? location.getSchemeSpecificPart() : null;
        if (path == null) {
            throw new IllegalStateException("Unable to determine code source archive");
        }
        File root = new File(path);
        if (!root.exists()) {
            throw new IllegalStateException("Unable to determine code source archive from " + root);
        }
        System.out.println(root);
        JarFileArchive archive = new JarFileArchive(root);
        Archive.EntryFilter filter = entry -> {
            if (entry.isDirectory()) {
                return entry.getName().equals("BOOT-INF/classes/");
            }
            return entry.getName().startsWith("BOOT-INF/lib/");
        };
        List<Archive> nestedArchives = archive.getNestedArchives(filter);
        for (Archive nestedArchive : nestedArchives) {
            System.out.println(nestedArchive.getUrl());
        }
        SpringApplication.run(AutoConfigureDemoApplication.class, args);
    }
}
