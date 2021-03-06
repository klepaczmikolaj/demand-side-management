package pl.wut.wsd.dsm.service;

import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ServiceDescriptionFactory {

    public final ServiceDescription name(final String name) {
        return nameAndProperties(name);
    }

    public final ServiceDescription nameAndProperties(final String name, final Property... properties) {
        final Map<String, Object> map = Arrays.stream(properties).collect(Collectors.toMap(Property::getName, Property::getValue));

        return nameAndProperties(name, map);
    }

    private final ServiceDescription nameAndProperties(final String name, final Map<String, Object> properties) {
        final ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(name);
        serviceDescription.setType(name + "Type");
        properties.forEach((k, v) -> serviceDescription.addProperties(new Property(k, v)));

        return serviceDescription;
    }
}
