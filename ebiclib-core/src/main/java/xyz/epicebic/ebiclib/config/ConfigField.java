package xyz.epicebic.ebiclib.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import xyz.epicebic.ebiclib.config.annotation.Comment;
import xyz.epicebic.ebiclib.config.annotation.ConfigEntry;

public class ConfigField {

    private final Field field;
    private final boolean isStatic;
    private String path;
    private List<String> comments;

    public ConfigField(Field field) {
        this.field = field;
        this.isStatic = Modifier.isStatic(field.getModifiers());

        initalize();
    }

    public Field getField() {
        return this.field;
    }

    public String getPath() {
        return this.path;
    }

    public Class<?> getType() {
        return this.field.getType();
    }

    public boolean isStatic() {
        return isStatic;
    }

    public List<String> getComments() {
        return this.comments;
    }

    private void initalize() {
        this.path = this.field.getAnnotation(ConfigEntry.class).value();

        for (Comment comment : this.field.getAnnotationsByType(Comment.class)) {
            if (this.comments == null) {
                this.comments = new ArrayList<>();
            }
            this.comments.add(comment.value());
        }
    }

}
