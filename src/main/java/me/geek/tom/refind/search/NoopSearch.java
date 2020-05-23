package me.geek.tom.refind.search;

import me.geek.tom.refind.Refind;

import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class NoopSearch implements Refind.Search {
    @Override
    public void search(JarFile jar, JarEntry entry, String search, Consumer<String> handler) {

    }
}
