package me.geek.tom.refind;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import me.geek.tom.refind.search.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Refind {

    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();

        OptionSpec<File> inputArg = parser.accepts("input").withRequiredArg().ofType(File.class);
        OptionSpec<String> searchArg = parser.accepts("term").withRequiredArg();

        parser.accepts("fieldtype");
        parser.accepts("classname");
        parser.accepts("getter");
        parser.accepts("constructor");
        parser.accepts("locals");

        parser.accepts("help");

        OptionSet options = parser.parse(args);

        if (options.has("help")) {
            parser.printHelpOn(System.out);
            return;
        }

        File input = options.valueOf(inputArg);
        String search = options.valueOf(searchArg);

        if (!input.exists()) {
            System.err.println("Input file: " + input.toString() + " does not exist!");
            System.exit(1);
        }

        JarFile jar = new JarFile(input);

        List<Search> s = new ArrayList<>();
        if (options.has("fieldtype"))
            s.add(new FieldTypeSearch());
        if (options.has("classname"))
            s.add(new ClassNameSearch());
        if (options.has("getter"))
            s.add(new GetterSearch());
        if (options.has("constructor"))
            s.add(new ConstructorSearch());
        if (options.has("locals"))
            s.add(new LocalVarTypeSearch());

        doSearch(search, jar, s, System.out::println);
    }

    private static void doSearch(String term, JarFile jar, List<Search> searches, Consumer<String> handler) throws IOException {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {

            JarEntry entry = entries.nextElement();

            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                for (Search search : searches)
                    search.search(jar, entry, term, s -> handler.accept("["+search.getClass().getSimpleName()+"] "+s));
            }
        }
    }

    public interface Search {
        void search(JarFile jar, JarEntry entry, String search, Consumer<String> handler) throws IOException;
    }

}
