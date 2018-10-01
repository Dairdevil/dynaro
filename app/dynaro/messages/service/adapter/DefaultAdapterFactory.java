package dynaro.messages.service.adapter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import dynaro.actors.EndpointWorker;
import dynaro.microtypes.EndpointPath;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class DefaultAdapterFactory
        implements AdapterFactory {

    @Inject(optional = true)
    @Named("dynaro.adapters.package")
    private static String pathToAdapters = "akka.messages.adapters";

    private static DefaultAdapterFactory INSTANCE = new DefaultAdapterFactory();

    private Map<EndpointPath, Adapter> adapterMap = new HashMap<>();

    private DefaultAdapterFactory() {

        Reflections reflections = new Reflections(
                ClasspathHelper.forPackage(pathToAdapters), new SubTypesScanner());
        Set<Class<? extends Adapter>> implementingTypes =
                reflections.getSubTypesOf(Adapter.class);

        implementingTypes.forEach(t -> {
            try {

                Class<? extends EndpointWorker> workerClass = getWorkerClass(t);

                Method m = workerClass.getMethod("path");

                EndpointPath path = (EndpointPath) m.invoke(null);

                adapterMap.put(path, t.newInstance());
            }
            catch (NoSuchMethodException nsme) {
                // TODO do nothing? getPath is part of the EndpointWorker interface
            }
            catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                // TODO handle
            }
        });
    }

    public Adapter getAdapter(EndpointPath path) {
        return path == null ? null : adapterMap.get(path);
    }

    private Class<? extends EndpointWorker> getWorkerClass(Class t) {
        return ((Class<? extends EndpointWorker>) ((ParameterizedType) t
                .getGenericInterfaces()[0])
                .getActualTypeArguments()[1]);
    }

    public static DefaultAdapterFactory getInstance() {
        return INSTANCE;
    }
}
