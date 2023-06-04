package de.geolykt.micromixin.api;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;

import de.geolykt.micromixin.SimpleRemapper;

/**
 * A constraint that is applied on every injection point before it can be considered a valid injection point.
 * Corresponds to the @At(target = [...]) or @At(desc = [...]) values. As such, this constraint is blind to
 * the ordinal position of the injection point.
 */
public interface InjectionPointTargetConstraint {

    /**
     * Check whether the specified instruction is applicable to this constraint.
     *
     * @param insn The instruction to check.
     * @param remapper The remapper instance to make use of. This is used to remap any references of the mixin class to the target class when applying injection point constraints.
     * @param sharedBuilder Shared {@link StringBuilder} instance to reduce {@link StringBuilder} allocations.
     * @param parent The class that owns the method that owns the instruction.
     */
    boolean isValid(@NotNull AbstractInsnNode insn, @NotNull SimpleRemapper remapper, @NotNull StringBuilder sharedBuilder);
}
