package xyz.epicebic.ebiclib.config;

import java.lang.reflect.Field;

public class ReflectionHelper {
    public static <T> void setField(ConfigField field, Object value, T wrapped) throws ReflectiveOperationException {
        Field internal = field.getField();
        if (field.isStatic()) {
            internal.set(null, value);
        } else {
            boolean access = internal.canAccess(wrapped);
            if (!access) {
                internal.trySetAccessible();
            }

            internal.set(wrapped, value);

            if (!access) {
                internal.setAccessible(false);
            }
        }
    }

    public static <T> Object getField(ConfigField field, T wrapped) {
        Field internal = field.getField();
        if (field.isStatic()) {
            try {
                return internal.get(null);
            } catch (ReflectiveOperationException ex) {
                return null;
            }
        } else {
            boolean access = internal.canAccess(wrapped);
            if (!access) {
                internal.trySetAccessible();
            }

            try {
                return internal.get(wrapped);
            } catch (ReflectiveOperationException ex) {
                return null;
            } finally {
                if (!access) {
                    internal.setAccessible(false);
                }
            }
        }
    }
}
