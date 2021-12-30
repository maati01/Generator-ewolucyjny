package agh.ics.generator.interfaces;

import agh.ics.generator.simulation.EpochStatistic;

public interface IAnimalMoveObserver {

    void animalMove(EpochStatistic epochStatisticWrappedMap, EpochStatistic epochStatisticBoundedMap);
}

