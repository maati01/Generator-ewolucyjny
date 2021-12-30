package agh.ics.generator.interfaces;

import agh.ics.generator.EpochStatistic;

public interface IAnimalMoveObserver {

    void animalMove(EpochStatistic epochStatisticWrappedMap, EpochStatistic epochStatisticBoundedMap);
}

