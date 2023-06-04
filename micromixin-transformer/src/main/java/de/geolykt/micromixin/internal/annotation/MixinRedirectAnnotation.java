package de.geolykt.micromixin.internal.annotation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import de.geolykt.micromixin.MixinTransformer;
import de.geolykt.micromixin.SimpleRemapper;
import de.geolykt.micromixin.internal.HandlerContextHelper;
import de.geolykt.micromixin.internal.MixinMethodStub;
import de.geolykt.micromixin.internal.MixinParseException;
import de.geolykt.micromixin.internal.MixinStub;
import de.geolykt.micromixin.internal.selectors.DescSelector;
import de.geolykt.micromixin.internal.selectors.MixinTargetSelector;
import de.geolykt.micromixin.internal.selectors.StringSelector;
import de.geolykt.micromixin.internal.util.ASMUtil;
import de.geolykt.micromixin.internal.util.CodeCopyUtil;
import de.geolykt.micromixin.internal.util.Objects;
import de.geolykt.micromixin.supertypes.ClassWrapperPool;

public final class MixinRedirectAnnotation extends MixinAnnotation<MixinMethodStub> {

    @NotNull
    public final MixinAtAnnotation at;
    @NotNull
    public final Collection<MixinTargetSelector> selectors;
    @NotNull
    private final MethodNode injectSource;
    private final int require;
    private final int expect;
    @NotNull
    private final ClassWrapperPool pool;

    private MixinRedirectAnnotation(@NotNull MixinAtAnnotation at, @NotNull Collection<MixinTargetSelector> selectors,
            @NotNull MethodNode injectSource, int require, int expect, @NotNull ClassWrapperPool pool) {
        this.at = at;
        this.selectors = selectors;
        this.injectSource = injectSource;
        this.require = require;
        this.expect = expect;
        this.pool = pool;
    }

    @NotNull
    public static MixinRedirectAnnotation parse(@NotNull ClassNode node, @NotNull MethodNode method, @NotNull AnnotationNode annot, @NotNull MixinTransformer<?> transformer, @NotNull StringBuilder sharedBuilder) throws MixinParseException {
        MixinAtAnnotation at = null;
        Collection<MixinDescAnnotation> target = null;
        String[] targetSelectors = null;
        int require = -1;
        int expect = -1;
        for (int i = 0; i < annot.values.size(); i += 2) {
            String name = (String) annot.values.get(i);
            Object val = annot.values.get(i + 1);
            if (name.equals("at")) {
                try {
                    if (val == null) {
                        throw new MixinParseException("Null annotation node");
                    }
                    at = MixinAtAnnotation.parse(node, (AnnotationNode) val, transformer.getInjectionPointSelectors());
                } catch (MixinParseException mpe) {
                    throw new MixinParseException("Unable to parse @At annotation defined by " + node.name + "." + method.name + method.desc, mpe);
                }
            } else if (name.equals("target")) {
                if (target != null) {
                    throw new MixinParseException("Duplicate \"target\" field in @Redirect " + node.name + "." + method.name + method.desc);
                }
                target = new ArrayList<MixinDescAnnotation>();
                @SuppressWarnings("unchecked")
                List<AnnotationNode> atValues = ((List<AnnotationNode>) val);
                for (AnnotationNode atValue : atValues) {
                    if (atValue == null) {
                        throw new NullPointerException();
                    }
                    MixinDescAnnotation parsed = MixinDescAnnotation.parse(node, "()V", atValue);
                    target.add(parsed);
                }
                target = Collections.unmodifiableCollection(target);
            } else if (name.equals("method")) {
                if (targetSelectors != null) {
                    throw new MixinParseException("Duplicate \"method\" field in @Redirect " + node.name + "." + method.name + method.desc);
                }
                @SuppressWarnings("all")
                @NotNull String[] hack = (String[]) ((List) val).toArray(new String[0]);
                targetSelectors = hack;
            } else if (name.equals("require")) {
                require = ((Integer) val).intValue();
            } else if (name.equals("expect")) {
                expect = ((Integer) val).intValue();
            } else {
                throw new MixinParseException("Unimplemented key in @Redirect" + node.name + "." + method.name + method.desc + ": " + name);
            }
        }
        List<MixinTargetSelector> selectors = new ArrayList<MixinTargetSelector>();
        if (target != null) {
            for (MixinDescAnnotation desc : target) {
                selectors.add(new DescSelector(Objects.requireNonNull(desc)));
            }
        }
        if (targetSelectors != null) {
            for (String s : targetSelectors) {
                selectors.add(new StringSelector(Objects.requireNonNull(s)));
            }
        }
        if (selectors.isEmpty()) {
            // IMPLEMENT what about injector groups?
            throw new MixinParseException("No available selectors: Mixin " + node.name + "." + method.name + method.desc + " does not match anything and is not a valid mixin.");
        }
        if (at == null) {
            throw new MixinParseException("Redirector Mixin " + node.name + "." + method.name + method.desc + " should define the at-value but does not. The mixin may be compiled for a future version of mixin.");
        }
        return new MixinRedirectAnnotation(at, Collections.unmodifiableCollection(selectors), method, require, expect, transformer.getPool());
    }

    @Override
    public void apply(@NotNull ClassNode to, @NotNull HandlerContextHelper hctx, @NotNull MixinStub sourceStub,
            @NotNull MixinMethodStub source, @NotNull SimpleRemapper remapper, @NotNull StringBuilder sharedBuilder) {
        if ((this.injectSource.access & Opcodes.ACC_STATIC) != 0 && (this.injectSource.access & Opcodes.ACC_PRIVATE) == 0) {
            throw new MixinParseException("The redirect handler method " + sourceStub.sourceNode.name + "." + this.injectSource.name + this.injectSource.desc + " is static, but isn't private. Consider making the method private, as both access modifiers cannot be present at the same time.");
        }
        if (!this.at.supportsRedirect()) {
            throw new IllegalStateException("Illegal mixin: " + sourceStub.sourceNode.name + "." + this.injectSource.name + this.injectSource.desc + " uses selector @At(\"" + at.value + "\") which does not support usage within a @Redirect context.");
        }
        MethodNode handlerNode = CodeCopyUtil.copyHandler(this.injectSource, sourceStub, to, hctx.handlerPrefix + hctx.handlerCounter++ + "$redirect$" + this.injectSource.name, remapper, hctx.lineAllocator);
        Map<LabelNode, MethodNode> labels = new HashMap<LabelNode, MethodNode>();
        for (MixinTargetSelector selector : selectors) {
            MethodNode targetMethod = selector.selectMethod(to, sourceStub);
            if (targetMethod != null) {
                // TODO ACC_STATIC is mandated in the constructor before the super() call even though the constructor itself is not ACC_STATIC.
                if ((targetMethod.access & Opcodes.ACC_STATIC) != 0) {
                    if ((this.injectSource.access & Opcodes.ACC_STATIC) == 0) {
                        throw new IllegalStateException("Illegal mixin: " + sourceStub.sourceNode.name + "." + this.injectSource.name + this.injectSource.desc + " targets " + to.name + "." + targetMethod.name + targetMethod.desc + ". Target is static, but the mixin is not.");
                    }
                } else if ((this.injectSource.access & Opcodes.ACC_STATIC) != 0) {
                    // Technically that one could be doable, but it'd be nasty.
                    throw new IllegalStateException("Illegal mixin: " + sourceStub.sourceNode.name + "." + this.injectSource.name + this.injectSource.desc + " targets " + to.name + "." + targetMethod.name + targetMethod.desc + " target is not static, but the callback handler is.");
                }
                for (LabelNode label : this.at.getLabels(targetMethod, remapper, sharedBuilder)) {
                    labels.put(label, targetMethod);
                }
            }
        }
        if (labels.size() < this.require) {
            throw new IllegalStateException("Illegal mixin: " + sourceStub.sourceNode.name + "." + this.injectSource.name + this.injectSource.desc + " requires " + this.require + " injection points but only found " + labels.size() + ".");
        }
        if (labels.size() < this.expect) {
            // IMPLEMENT proper logging mechanism
            System.err.println("[WARNING:MM/MRA] Potentially outdated mixin: " + sourceStub.sourceNode.name + "." + this.injectSource.name + this.injectSource.desc + " expects " + this.expect + " injection points but only found " + labels.size() + ".");
        }

        // IMPLEMENT @Redirect-chaining. The main part could be done through annotations.
        for (Map.Entry<LabelNode, MethodNode> entry : labels.entrySet()) {
            AbstractInsnNode insn = entry.getKey().getNext();
            while (insn.getOpcode() == -1) {
                insn = insn.getNext();
            }
            if (!(insn instanceof MethodInsnNode)) {
                throw new IllegalStateException("Illegal mixin: " + sourceStub.sourceNode.name + "." + this.injectSource.name + this.injectSource.desc + " selects an instruction that isn't a MethodInsnNode (should be any of [INVOKESTATIC, INVOKEVIRTUAL, INVOKESPECIAL]) but rather is a " + insn.getClass().getName() + ". This issue is most likely caused by an erroneous @At-value (or an invalid shift). Using @At(" + this.at.value + ")");
            }
            // IMPLEMENT verify arguments.
            // TODO test whether argument capture is a thing with @Redirect
            MethodNode targetMethod = entry.getValue();
            InsnList instructions =  targetMethod.instructions;
            int insertedOpcode;
            if ((this.injectSource.access & Opcodes.ACC_STATIC) == 0) {
                VarInsnNode preInsert = new VarInsnNode(Opcodes.ALOAD, 0);
                instructions.insertBefore(insn, preInsert);
                ASMUtil.shiftDownByDesc(handlerNode.desc, false, to, targetMethod, preInsert, this.pool);
                insertedOpcode = Opcodes.INVOKEVIRTUAL;
            } else {
                insertedOpcode = Opcodes.INVOKESTATIC;
            }
            MethodInsnNode inserted = new MethodInsnNode(insertedOpcode, to.name, handlerNode.name, handlerNode.desc);
            instructions.insertBefore(insn, inserted);
            instructions.remove(insn);
        }
    }

    @Override
    public void collectMappings(@NotNull MixinMethodStub source, @NotNull ClassNode target, @NotNull SimpleRemapper remapper,
            @NotNull StringBuilder sharedBuilder) {
        // NOP
    }
}
