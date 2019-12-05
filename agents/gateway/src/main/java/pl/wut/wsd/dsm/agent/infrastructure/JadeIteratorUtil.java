package pl.wut.wsd.dsm.agent.infrastructure;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.util.leap.Iterator;

import java.util.ArrayList;
import java.util.List;

public class JadeIteratorUtil {

    public static List<Property> getProperties(final ServiceDescription serviceDescription) {
        return iteratorToList(serviceDescription.getAllProperties(), Property.class);
    }

    public static List<ServiceDescription> getAllServices(final DFAgentDescription dfAgentDescription) {
        return iteratorToList(dfAgentDescription.getAllServices(), ServiceDescription.class);
    }

    private static <T> List<T> iteratorToList(final Iterator jadeIterator, final Class<T> expectedClass) {
        final List<T> results = new ArrayList<>();
        while (jadeIterator.hasNext()) {
            final Object next = jadeIterator.next();
            if (expectedClass.isAssignableFrom(next.getClass())) {
                results.add((T) next);
            }
        }

        return results;
    }
}
