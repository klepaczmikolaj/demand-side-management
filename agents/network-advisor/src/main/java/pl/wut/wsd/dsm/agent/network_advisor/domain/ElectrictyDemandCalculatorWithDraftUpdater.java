package pl.wut.wsd.dsm.agent.network_advisor.domain;

import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.handle.ParsingHandler;
import pl.wut.wsd.dsm.ontology.network.DraftSummary;
import pl.wut.wsd.dsm.protocol.SystemDraftProtocol;
import pl.wut.wsd.dsm.protocol.TargetedStep;

public class ElectrictyDemandCalculatorWithDraftUpdater extends ParsingHandler<DraftSummary, TargetedStep<SystemDraftProtocol, DraftSummary>> {

    private final ElectricityDemandProfileCalculator calculator;

    public ElectrictyDemandCalculatorWithDraftUpdater(final Codec codec,
                                                      final TargetedStep<SystemDraftProtocol, DraftSummary> protocolStep,
                                                      final ElectricityDemandProfileCalculator calculator) {
        super(codec, protocolStep);
        this.calculator = calculator;
    }

    @Override
    protected void handle(final DraftSummary draftSummary) {
        calculator.registerDraft(draftSummary);
    }
}
