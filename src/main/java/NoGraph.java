import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class NoGraph {

    static void run() {
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

        var viewSpotCandidates = Streams
                .zip(
                        Arrays.stream(mesh.elements),
                        Arrays.stream(mesh.values),
                        (ElementWithValue::new)
                )
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingDouble(ElementWithValue::getValue))));

        System.out.println(viewSpotCandidates);


        var viewSpots = new ArrayList<>(Collections.emptyList());
        var deleted = new HashSet<ElementWithValue>();

        while (viewSpotCandidates.size() > 0) {
            ElementWithValue currentElement = viewSpotCandidates.first();

            viewSpots.add(currentElement);
            deleted.add(currentElement);

            removeSameOrLowerValue(currentElement, deleted, viewSpotCandidates);

            viewSpotCandidates.removeAll(deleted);
        }

        System.out.println(viewSpots);
    }

    private static void removeSameOrLowerValue(
            ElementWithValue currentElement,
            Set<ElementWithValue> deleted,
            TreeSet<ElementWithValue> viewSpotCandidates
    ) {
        if (deleted.contains(currentElement)) return;

        var recursionStack = new Stack<ElementWithValue>();

        recursionStack.push(currentElement);

        while (!recursionStack.empty()) {
            for (ElementWithValue element : viewSpotCandidates) {
                if (
                        !Sets.intersection(currentElement.getNodes(), element.getNodes()).isEmpty()
                                && currentElement.getValue() > element.getValue()
                ) {
                    deleted.add(element);
                    recursionStack.push(element);
                }
            }
        }
    }
}
