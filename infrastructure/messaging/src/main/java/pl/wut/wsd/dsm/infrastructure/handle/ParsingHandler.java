package pl.wut.wsd.dsm.infrastructure.handle;

import jade.lang.acl.ACLMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.codec.DecodingError;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.protocol.ProtocolStep;

@Slf4j
@RequiredArgsConstructor
public abstract class ParsingHandler<C, S extends ProtocolStep<?, C>> implements Handler {

    protected final Codec codec;
    protected final S protocolStep;

    @Override
    public void handle(final ACLMessage aclMessage) {
        final Class<C> clazz = protocolStep.getMessageClass();
        final Result<C, DecodingError> decoded = codec.decode(aclMessage.getContent(), clazz);

        if (decoded.isValid()) {
            handle(decoded.result());
        } else {
            log.error("Error decoding {} from protocol {} and step {}", clazz.getSimpleName(),
                    protocolStep.getProtocol().getName(), protocolStep.getStepName());
        }
    }

    protected abstract void handle(C c);
}
