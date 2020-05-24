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
    private MVSupplier visitorSupplier;

    public MethodScanningClassVisitor(Consumer<String> handler, String search, MVSupplier visitorSupplier) {
        super(ASM6);
        this.search = search;
        this.handler = handler;
        this.visitorSupplier = visitorSupplier;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String ret = descriptor.split("\\)")[1];
        if (ret.contains(search) && visitorSupplier == null)
            handler.accept(className + "#" + name + descriptor);
        if (visitorSupplier != null)
            return visitorSupplier.get(super.visitMethod(access, name, descriptor, signature, exceptions), handler, className + "#" + name + descriptor, search);
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    public interface MVSupplier {
        MethodVisitor get(MethodVisitor mv, Consumer<String> handler, String method, String term);
    }
}
