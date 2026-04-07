package org.tmt.apsproceduredataservice.impl;

import csw.event.api.javadsl.IEventService;
import csw.event.api.javadsl.IEventSubscription;
import csw.params.core.generics.Key;
import csw.params.core.generics.Parameter;
import csw.params.events.Event;
import csw.params.events.EventKey;
import csw.params.events.EventName;
import csw.params.events.SystemEvent;
import csw.params.javadsl.JKeyType;
import csw.prefix.javadsl.JSubsystem;
import csw.prefix.models.Prefix;
import esw.http.template.wiring.JCswServices;
import org.tmt.apsproceduredataservice.core.models.ComputationKeyValuePair;
import org.tmt.apsproceduredataservice.core.models.ComputationKeyValuePairList;
import org.tmt.apsproceduredataservice.core.models.GetProcedureResultDataRequest;
import org.tmt.apsproceduredataservice.core.models.GreetResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class JApsproceduredataserviceImpl {

    private final JCswServices jCswServices;

    // In-memory store keyed by StoreKey.
    // Prototype implementation — does not persist to database.
    private final ConcurrentHashMap<StoreKey, ComputationKeyValuePair> store = new ConcurrentHashMap<>();

    // Held so the subscription is not garbage collected
    private IEventSubscription exposureStoreCompletedSubscription;

    private record StoreKey(
            int procedureRunId,
            String computationName,
            String fieldName,
            Optional<Integer> iterationNumber
    ) {}

    // ── Event constants ───────────────────────────────────────────────────────

    // The prefix of the component that PUBLISHES exposureStoreCompleted.
    // The Exposure Service subscribes to this event — it does not publish it.
    // Update this prefix to match the actual publishing component once known.
    private static final Prefix EXPOSURE_STORE_PUBLISHER_PREFIX =
            new Prefix(JSubsystem.APS, "exposureAssembly");

    private static final EventName EXPOSURE_STORE_COMPLETED =
            new EventName("exposureStoreCompleted");

    private static final Key<String> FILENAME_KEY =
            JKeyType.StringKey().make("filename");

    // ── Constructor ───────────────────────────────────────────────────────────

    public JApsproceduredataserviceImpl(JCswServices jCswServices) {
        this.jCswServices = jCswServices;
    }

    // ── Event subscription ────────────────────────────────────────────────────

    /**
     * Subscribes to the exposureStoreCompleted SystemEvent.
     *
     * Note: this is example code for the Exposure Service which will need to subscribe
     * to the exposureStoreCompleted event.  This code section merely demonstrates how this
     * can be done.
     * This service (Procedure Data Service) does not use this code, and it would be removed when
     * building production code.
     *
     * Called once from ApsproceduredataserviceWiring after construction.
     */
    public void subscribeToExposureEvents() {
        IEventService eventService = jCswServices.eventService();

        EventKey exposureStoreCompletedKey = new EventKey(
                EXPOSURE_STORE_PUBLISHER_PREFIX,
                EXPOSURE_STORE_COMPLETED
        );

        exposureStoreCompletedSubscription = eventService
                .defaultSubscriber()
                .subscribeAsync(
                        Collections.singleton(exposureStoreCompletedKey),
                        this::handleExposureStoreCompleted
                );
    }

    private CompletableFuture<String> handleExposureStoreCompleted(Event event) {
        if (!(event instanceof SystemEvent systemEvent)) {
            return CompletableFuture.completedFuture("ignored — not a SystemEvent");
        }

        Optional<Parameter<String>> filenameParam = systemEvent.jGet(FILENAME_KEY);

        if (filenameParam.isEmpty()) {
            return CompletableFuture.completedFuture("ignored — filename parameter missing");
        }

        String filename = filenameParam.get().head();

        // TODO: store or act on the filename as the integration requires.
        // For now, log it via the CSW logging service.
        jCswServices.loggerFactory()
                .getLogger(getClass())
                .info("exposureStoreCompleted received: filename = " + filename);

        return CompletableFuture.completedFuture(filename);
    }

    // ── Existing ──────────────────────────────────────────────────────────────

    public CompletableFuture<GreetResponse> sayBye() {
        return CompletableFuture.completedFuture(new GreetResponse("Bye!!!"));
    }

    // ── POST /storeProcedureComputationResults ────────────────────────────────

    public CompletableFuture<Void> storeProcedureComputationResults(ComputationKeyValuePairList request) {
        for (ComputationKeyValuePair kvp : request.keyValuePairList()) {
            int runId = kvp.key().procedureRunId().orElse((Integer) request.procedureRunId());
            StoreKey key = new StoreKey(
                    runId,
                    kvp.key().computationName(),
                    kvp.key().fieldName(),
                    kvp.key().iterationNumber()
            );
            store.put(key, kvp);
        }
        return CompletableFuture.completedFuture(null);
    }

    // ── POST /getProcedureResultData ──────────────────────────────────────────

    public CompletableFuture<List<ComputationKeyValuePair>> getProcedureResultData(GetProcedureResultDataRequest request) {
        List<ComputationKeyValuePair> results = new ArrayList<>();
        for (var resultKey : request.computationResultKeys()) {
            StoreKey key = new StoreKey(
                    request.procedureRunId(),
                    resultKey.computationName(),
                    resultKey.fieldName(),
                    resultKey.iterationNumber()
            );
            ComputationKeyValuePair found = store.get(key);
            if (found != null) results.add(found);
        }
        return CompletableFuture.completedFuture(results);
    }
}