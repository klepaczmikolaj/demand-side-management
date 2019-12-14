package pl.wut.wsd.dsm.infrastructure.handle;

import jade.lang.acl.ACLMessage;

public interface Handler {
    void handle(ACLMessage aclMessage);
}
