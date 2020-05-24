package me.geek.tom.refind.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.function.Consumer;

import static org.objectweb.asm.Opcodes.ASM6;

public class LocalVarTypeScanningMethodVisitor extends MethodVisitor {

    private final Consumer<String> handler;
    private final String method;
    private final String term;

    public LocalVarTypeScanningMethodVisitor(MethodVisitor mv, Consumer<String> handler, String method, String term) {
        super(ASM6, mv);
        this.handler = handler;
        this.method = method;
        this.term = term;
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        if (descriptor.contains(term))
            handler.accept(method + " contains local variable of type "+descriptor+" called "+name);

        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }
}
