package dogapi;

import java.util.*;

public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher underlyingFetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.underlyingFetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        try {
            List<String> subBreeds = underlyingFetcher.getSubBreeds(breed);
            cache.put(breed, subBreeds);
            callsMade++;
            return subBreeds;
        } catch (BreedNotFoundException e) {
            // Do not cache failed lookups
            callsMade++;
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}