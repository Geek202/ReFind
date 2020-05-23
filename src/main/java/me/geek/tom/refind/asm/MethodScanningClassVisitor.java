package me.geek.tom.refind.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.function.Consumer;

import static org.objectweb.asm.Opcodes.ASM6;

public class MethodScanningClassVisitor extends ClassVisitor {

    private String search;
    private Consumer<String> handler;
    private String className = "undefined";
    private static boolean flag = false;

    public MethodScanningClassVisitor(Consumer<String> handler, String search) {
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
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String ret = descriptor.split("\\)")[1];
        if (ret.contains(search))
            handler.accept(className + "#" + name + descriptor);
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
