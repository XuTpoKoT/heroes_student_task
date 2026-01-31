package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        while (hasAliveUnits(playerArmy) && hasAliveUnits(computerArmy)) {
            List<Unit> turnQueue = new ArrayList<>();

            for (Unit unit : playerArmy.getUnits()) {
                if (unit != null && unit.isAlive()) {
                    turnQueue.add(unit);
                }
            }

            for (Unit unit : computerArmy.getUnits()) {
                if (unit != null && unit.isAlive()) {
                    turnQueue.add(unit);
                }
            }

            // если никто не может ходить — конец боя
            if (turnQueue.isEmpty()) {
                return;
            }

            // сортировка по убыванию атаки
            turnQueue.sort(
                    Comparator.comparingInt(Unit::getBaseAttack).reversed()
            );

            // юниты ходят по очереди
            for (Unit unit : turnQueue) {

                // юнит мог умереть до своего хода
                if (!unit.isAlive()) {
                    continue;
                }

                // если врагов больше нет — бой окончен
                if (isEnemyArmyDead(unit, playerArmy, computerArmy)) {
                    return;
                }

                Unit target = unit.getProgram().attack();

                if (target != null && target.isAlive()) {
                    printBattleLog.printBattleLog(unit, target);
                }

                // даём визуализации/логам обновиться
                Thread.sleep(1);
            }
        }
    }

    private boolean hasAliveUnits(Army army) {
        for (Unit unit : army.getUnits()) {
            if (unit != null && unit.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnemyArmyDead(Unit unit, Army playerArmy, Army computerArmy) {
        // если ходит игрок — проверяем компьютер
        if (playerArmy.getUnits().contains(unit)) {
            return !hasAliveUnits(computerArmy);
        }
        // если ходит компьютер — проверяем игрока
        return !hasAliveUnits(playerArmy);
    }
}