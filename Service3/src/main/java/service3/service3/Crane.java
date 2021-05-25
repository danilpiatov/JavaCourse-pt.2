package service3.service3;

import service3.entity.Ship;

public class Crane implements Runnable{
    private volatile Ship ship;

    Crane(Ship ship){
        this.ship = ship;
    }

    @Override
    public void run() {
        ship.decTOU();
    }
}
