package xyz.epicebic.ebiclib.config.adapter;

@SuppressWarnings("rawtypes")
public class EnumAdapter<T extends Enum> implements StringAdapter<T> {
    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    public EnumAdapter(Class<?> clazz) {
        this.clazz = (Class<T>) clazz;
    }

    @Override
    public String toString(T value) {
        return value.name();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T fromString(String value) {
        return (T) Enum.valueOf(clazz, value);
    }
}
