package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> result = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            if (row == null) {
                continue;
            }

            int rowSize = row.size();

            for (int y = 0; y < rowSize; y++) {
                Unit unit = row.get(y);

                if (unit == null || !unit.isAlive()) {
                    continue;
                }
                result.add(unit);

                if (isLeftArmyTarget) {
                    // цель — левая армия → проверяем, не закрыт ли СПРАВА
                    int rightY = y + 1;
                    if (rightY >= rowSize || row.get(rightY) == null) {
                        result.add(unit);
                    }
                } else {
                    // цель — правая армия → проверяем, не закрыт ли СЛЕВА
                    int leftY = y - 1;
                    if (leftY < 0 || row.get(leftY) == null) {
                        result.add(unit);
                    }
                }
            }
        }

        return result;
    }
}
