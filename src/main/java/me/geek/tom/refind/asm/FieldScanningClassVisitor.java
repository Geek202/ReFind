package me.geek.tom.refind.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

import java.util.function.Consumer;

import static org.objectweb.asm.Opcodes.ASM6;

public class FieldScanningClassVisitor extends ClassVisitor {

    private String search;
    private Consumer<String> handler;
    private String className = "undefined";

    public FieldScanningClassVisitor(Consumer<String> handler, String search) {
        super(ASM6);
        this.search = search;
        this.handler = handler;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (descriptor.contains(search))
            handler.accept(className + "#" + name + "(" + descriptor + ")");
        return super.visitField(access, name, descriptor, signature, value);
    }
}
