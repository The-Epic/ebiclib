package xyz.epicebic.ebiclib.config;

import java.io.File;
import java.lang.reflect.Method;

public class ReloadableClass<T> extends ConfigReloadable<T> {

    public ReloadableClass(ConfigurationManager manager, File file, Class<T> clazz) {
        super(manager, file, clazz);
    }

    @Override
    protected void postLoad(Method postLoadMethod) {
        try {
            boolean access = postLoadMethod.canAccess(null);
            if (!access) {
                postLoadMethod.trySetAccessible();
            }

            postLoadMethod.invoke(null);

            if (!access) {
                postLoadMethod.setAccessible(false);
            }
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void setField(ConfigField field, Object value) throws ReflectiveOperationException {
        if (field.isStatic()) {
            field.getField().set(null, value);
        }
    }

    @Override
    protected Object getField(ConfigField field) {
        if (field.isStatic()) {
            try {
                return field.getField().get(null);
            } catch (ReflectiveOperationException ex) {
                return null;
            }
        }
        return null;
    }
}
