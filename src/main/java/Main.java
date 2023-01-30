import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

// nodes
// [{id: 0, x: 0.0, y: 0.0}, ...]
// elements
// [{id: 0, nodes: [0,1,12]}, ...]
// values
// [{element_id: 0, value: 0.151}, ...]

@SuppressWarnings("UnstableApiUsage")
public class Main {
    public static void main(String[] args) {
        var objectMapper = new ObjectMapper();

        Mesh mesh;

        var file = new File("/Users/Valerian/repositories/view-spots-java/input/mesh.json");

        System.out.println("File: " + file);

        try {
            mesh = objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(mesh);

        HashSet<ElementWithValue> viewSpotCandidates = Streams
                .zip(
                        Arrays.stream(mesh.elements),
                        Arrays.stream(mesh.values),
                        (ElementWithValue::new)
                )
                .collect(Collectors.toCollection(HashSet::new));

        System.out.println(viewSpotCandidates);

        var viewSpots = new ArrayList<>(Collections.emptyList());

        for (ElementWithValue viewSpotCandidate : viewSpotCandidates) {
            viewSpotCandidates.remove(viewSpotCandidate);
            var allLowerAndToBeRemovedPair = removeSameOrLowerValueNeighbors(viewSpotCandidate, viewSpotCandidates);

            if (allLowerAndToBeRemovedPair.allLower) viewSpots.add(viewSpotCandidate);
            viewSpotCandidates.removeAll(allLowerAndToBeRemovedPair.elementsToBeRemoved);
        }

        System.out.println(viewSpots);
    }

    private static AllLowerAndToBeRemovedPair removeSameOrLowerValueNeighbors(
            ElementWithValue currentElement,
            HashSet<ElementWithValue> viewSpotCandidates
    ) {
        var toBeRemoved = new HashSet<ElementWithValue>();
        AtomicBoolean allLower = new AtomicBoolean(true);

        viewSpotCandidates.forEach( element -> {
            if (!Sets.intersection(currentElement.getNodes(), element.getNodes()).isEmpty()
                    && element.getValue() <= currentElement.getValue()) {
                toBeRemoved.add(element);
            } else {
                allLower.set(false);
            }
        });
        return new AllLowerAndToBeRemovedPair(allLower.get(), toBeRemoved);
    }
}