package pl.wut.wsd.dsm.service;

import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Arrays;
import java.util.Map;

public class ServiceDescriptionFactory {

    public final ServiceDescription nameAndProperties(final String name, final Map<String, String> properties) {
        final ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(name);
        properties.forEach((k, v) -> serviceDescription.addProperties(new Property(k, v)));

        return serviceDescription;
    }

    public final ServiceDescription nameAndProperties(final String name, final Property... properties) {
        final ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(name);
        serviceDescription.setType(name + "Type");
        Arrays.stream(properties).forEach(serviceDescription::addProperties);

        return serviceDescription;
    }
}
