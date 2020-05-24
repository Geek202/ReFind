package me.geek.tom.refind.asm;

import org.objectweb.asm.MethodVisitor;

import java.util.function.Consumer;

import static org.objectweb.asm.Opcodes.*;

public class ConstructorScanningMethodVisitor extends MethodVisitor {

    private final Consumer<String> handler;
    private final String method;
    private final String term;
    private int count = 0;

    public ConstructorScanningMethodVisitor(MethodVisitor mv, Consumer<String> handler, String method, String term) {
        super(ASM6, mv);
        this.handler = handler;
        this.method = method;
        this.term = term;
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        if (opcode == NEW && type.contains(term))
            count++;
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitEnd() {
        if (count != 0)
            handler.accept(method + " contructs " + term + " " + count + " times.");
        super.visitEnd();
    }
}
