package org.tmt.apsproceduredataservice.impl;

import esw.http.template.wiring.JCswServices;
import org.tmt.apsproceduredataservice.core.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class JApsproceduredataserviceImpl {

    JCswServices jCswServices;

    // In-memory store keyed by StoreKey.
    // Prototype implementation — does not persist to database.
    private final ConcurrentHashMap<StoreKey, ComputationKeyValuePair> store = new ConcurrentHashMap<>();

    private record StoreKey(
            int procedureRunId,
            String computationName,
            String fieldName,
            Optional<Integer> iterationNumber
    ) {}
    public JApsproceduredataserviceImpl(JCswServices jCswServices) {

        this.jCswServices = jCswServices;

        // initialize hashmap

        String centroidOffsetsX = "[-0.172f, -0.783f, -0.736f, 1.541f, 0.937f, 0.688f, -0.573f, -1.473f, -1.925f, -1.406f, " +
                "-1.861f, -0.151f, 0.814f, 0.688f, 2.336f, 2.152f, 2.830f, 1.567f, -0.722f, -2.009f, -2.523f, -3.187f, " +
                "-2.840f, -2.990f, -3.044f, -1.716f, 5.332f, -5.395f, 1.456f, 1.453f, 2.334f, 2.370f, 1.381f, 2.058f, 2.213f, 1.357f]";

        String centroidOffsetsY = "[0.770f, 0.790f, -0.227f, -0.840f, -0.596f, -0.002f, 2.184f, 1.379f, 1.673f, 0.891f, -1.017f, " +
               " -1.913f, -2.428f, -1.720f, -1.569f, -0.760f, -1.677f, 1.239f, 3.146f, 3.277f, 2.767f, 2.677f, 1.470f, -0.154f, " +
               " -1.825f, -2.638f, 3.006f, -9.161f, -2.118f, -2.004f, 0.162f, -2.130f, -0.984f, 3.866f, 1.741f, 2.723f]";

        StoreKey keyX = new StoreKey(
                1,
                "centroidOffsets",
                "centroidOffsetsX",
                Optional.empty()
        );

        GenericValue valueX = new GenericValue("float[]", 36, 0, centroidOffsetsX);
        ComputationKeyValuePairKey kvpkX = new ComputationKeyValuePairKey(Optional.of(1), Optional.empty(), "centroidOffsets", "centroidOffsetsX");
        ComputationKeyValuePair kvpX = new ComputationKeyValuePair(kvpkX, valueX);
        store.put(keyX, kvpX);


        StoreKey keyY = new StoreKey(
                1,
                "centroidOffsets",
                "centroidOffsetsY",
                Optional.empty()
        );

        GenericValue valueY = new GenericValue("float[]", 36, 0, centroidOffsetsY);
        ComputationKeyValuePairKey kvpkY = new ComputationKeyValuePairKey(Optional.of(1), Optional.empty(), "centroidOffsets", "centroidOffsetsY");
        ComputationKeyValuePair kvpY = new ComputationKeyValuePair(kvpkY, valueY);
        store.put(keyY, kvpY);








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
