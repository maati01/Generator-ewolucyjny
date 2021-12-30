package agh.ics.generator;

public class EpochStatistic {
    private final AbstractWorldMap wrappedMap;
    private final AbstractWorldMap boundedMap;

    public EpochStatistic(AbstractWorldMap wrappedMap, AbstractWorldMap boundedMap){
        this.wrappedMap = wrappedMap;
        this.boundedMap = boundedMap;
    }

    public AbstractWorldMap getBoundedMap() {
        return boundedMap;
    }

    public AbstractWorldMap getWrappedMap() {
        return wrappedMap;
    }


}
