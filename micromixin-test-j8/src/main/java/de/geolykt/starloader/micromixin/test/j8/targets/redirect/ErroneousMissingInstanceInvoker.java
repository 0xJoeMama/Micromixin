package de.geolykt.starloader.micromixin.test.j8.targets.redirect;

public class ErroneousMissingInstanceInvoker {

    public static void invoke() {
        new GenericInvokeTarget().return0Instanced();
    }
}
