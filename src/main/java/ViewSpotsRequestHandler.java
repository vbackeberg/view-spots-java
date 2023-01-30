import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;

import java.util.*;
import java.util.stream.Stream;


// nodes
// [{id: 0, x: 0.0, y: 0.0}, ...]
// elements
// [{id: 0, nodes: [0,1,12]}, ...]
// values
// [{element_id: 0, value: 0.151}, ...]

@SuppressWarnings("UnstableApiUsage")
public class ViewSpotsRequestHandler implements RequestHandler<ViewSpotsRequest, String> {

    @Override
    public String handleRequest(ViewSpotsRequest viewSpotsRequest, Context context) {

        // long start = System.currentTimeMillis();

        var objectMapper = new ObjectMapper();
        Mesh mesh;

        try {
            mesh = objectMapper.readValue(viewSpotsRequest.meshAsJsonString, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ElementWithValue[] viewSpotCandidates = Streams
                .zip(
                        Arrays.stream(mesh.elements),
                        Arrays.stream(mesh.values),
                        (ElementWithValue::new)
                )
                .sorted(Comparator.comparingDouble(ElementWithValue::getValue).reversed())
                .toArray(ElementWithValue[]::new);

        var viewSpots = new ArrayList<ElementWithValue>(Collections.emptyList());
        var higherNodes = new HashSet<Integer>();

        var i = 0;
        while (viewSpots.size() < viewSpotsRequest.requestedNumberOfViewSpots) {
            var element = viewSpotCandidates[i];

            if (Sets.intersection(element.getNodes(), higherNodes).isEmpty()) {
                higherNodes.addAll(element.getNodes());
                viewSpots.add(element);
            }

            ++i;

            if (i == viewSpotCandidates.length) {
                System.err.println("More view spots requested than available.");
                break;
            }
        }

        // long time = System.currentTimeMillis() - start;
        // System.out.println("took: " + time);

        return Arrays.toString(viewSpots.stream().flatMap(e ->
                Stream.of("\n{element_id: " + e.getId() + ", value: " + e.getValue() + "}")).toArray());
    }
}
