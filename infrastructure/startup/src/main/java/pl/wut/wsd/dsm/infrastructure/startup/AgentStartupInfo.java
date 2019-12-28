package pl.wut.wsd.dsm.infrastructure.startup;

public interface AgentStartupInfo {
    String platformId();

    String containerName();

    String mainContainerHost();

    int mainContainerPort();
}
