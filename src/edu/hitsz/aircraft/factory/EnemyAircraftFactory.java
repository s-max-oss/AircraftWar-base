package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;

public interface EnemyAircraftFactory {
    AbstractAircraft createAircraft(int locationX, int locationY, int difficultyLevel);
}