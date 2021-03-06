package java_basic_course.day_26_04_2021.task2;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ObjectFactory {
    private static ObjectFactory objectFactory = new ObjectFactory();
    private Config config = new JavaConfig();
    private Reflections scanner = new Reflections("java_basic_course.day_26_04_2021.task2");

    private List<ObjectConfigurator> configurators = new ArrayList<ObjectConfigurator>();

    public static ObjectFactory getInstance() {
        return objectFactory;
    }

    @SneakyThrows
    private ObjectFactory() {

        Set<Class<? extends ObjectConfigurator>> classes = scanner.getSubTypesOf(ObjectConfigurator.class);

        for (Class<? extends ObjectConfigurator> aClass : classes) {
            if (!Modifier.isAbstract(aClass.getModifiers())) {
                configurators.add(aClass.getDeclaredConstructor().newInstance());
            }
        }
    }


    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        type = resolveRealImpl(type);
        T t = create(type);
        configure(t);

        return t;
    }

    private <T> T create(Class<T> type) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        return type.getDeclaredConstructor().newInstance();
    }

    private <T> void configure(T t) {
        for (ObjectConfigurator configurator : configurators) {
            configurator.configure(t);
        }
    }

    private <T> Class<T> resolveRealImpl(Class<T> type) {
        if (type.isInterface()) {
            Class<T> implClass = config.getImplClass(type);

            if (implClass == null) {
                Set<Class<? extends T>> classes = scanner.getSubTypesOf(type);

                if (classes.size() != 1) {
                    throw new IllegalStateException("0 or more than one impl found for type " + type);
                }
                type = (Class<T>) classes.iterator().next();
            }else {
                type = implClass;
            }
        }

        return type;
    }


}