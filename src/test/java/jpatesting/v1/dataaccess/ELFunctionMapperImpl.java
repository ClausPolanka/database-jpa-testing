package jpatesting.v1.dataaccess;

import javax.el.FunctionMapper;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ELFunctionMapperImpl extends FunctionMapper {

    private static final Map<String, Method> METHODS = new HashMap<String, Method>();

    private static final Map<String, Long> IDS = new HashMap<String, Long>();

    private static final int INITIAL_ID = 1;

    static {
        for (Method method : ELFunctionMapperImpl.class.getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            String name = method.getName();
            if (Modifier.isStatic(modifiers) && name.startsWith("db_")) {
                METHODS.put(name.replace('_', ':'), method);
            }
        }
    }

    @Override
    public Method resolveFunction(String prefix, String localName) {
        return METHODS.get(prefix + ":" + localName);
    }

    public static long db_id(String className) {
        long id;
        if (IDS.containsKey(className)) {
            id = IDS.get(className);
        } else {
            id = INITIAL_ID;
        }
        return id;
    }

    public static void setId(String className, long newId) {
        // each class' id must be set only once per method
        if (!IDS.containsKey(className)) {
            IDS.put(className, newId);
        }
    }

    public static void resetIds() {
        IDS.clear();
    }

    public static long getId(Class<?> clazz) {
        return db_id(clazz.getSimpleName());
    }

}
