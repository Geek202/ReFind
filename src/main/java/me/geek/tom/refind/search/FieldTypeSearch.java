package me.geek.tom.refind.search;

import me.geek.tom.refind.Refind;
import me.geek.tom.refind.asm.FieldScanningClassVisitor;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FieldTypeSearch implements Refind.Search {
    @Override
    public void search(JarFile jar, JarEntry entry, String search, Consumer<String> handler) throws IOException {
        ClassReader reader = new ClassReader(jar.getInputStream(entry));
        reader.accept(new FieldScanningClassVisitor(handler, search), 0);
    }
}
