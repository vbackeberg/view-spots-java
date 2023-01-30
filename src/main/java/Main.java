import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

// nodes
// [{id: 0, x: 0.0, y: 0.0}, ...]
// elements
// [{id: 0, nodes: [0,1,12]}, ...]
// values
// [{element_id: 0, value: 0.151}, ...]

@SuppressWarnings("UnstableApiUsage")
public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        if (args.length < 2) {
            throw new RuntimeException("Missing arguments");
        }

        File file;
        try {
            file = new File(args[0]);
        } catch (NullPointerException e) {
            throw new RuntimeException("Invalid file path: " + args[0], e);
        }

        int requestedNumberOfViewSpots;
        try {
            requestedNumberOfViewSpots = Integer.parseInt(args[1]);
        } catch (NullPointerException e) {
            throw new RuntimeException("Cannot parse as Integer: " + args[1], e);
        }

        var objectMapper = new ObjectMapper();
        Mesh mesh;

        try {
            mesh = objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
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
        while (viewSpots.size() < requestedNumberOfViewSpots) {
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

        long time = System.currentTimeMillis() - start;
        System.out.println("took: " + time);

        System.out.println("view spots: ");
        System.out.println(
                Arrays.toString(viewSpots.stream().flatMap(e -> Stream.of("\n{element_id: " + e.getId() + ", value: " + e.getValue() + "}")).toArray())
        );
    }
}