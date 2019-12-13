package pl.wut.wsd.dsm.agent.customer_agent.rest.model.device;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeviceBrief {
    private final Long id;
    private final String name;
}
