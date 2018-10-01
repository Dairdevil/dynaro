package dynaro.messages.service.adapter;

import com.google.inject.ImplementedBy;
import dynaro.microtypes.EndpointPath;

@ImplementedBy(DefaultAdapterFactory.class)
public interface AdapterFactory {

    Adapter getAdapter(EndpointPath path);
}
