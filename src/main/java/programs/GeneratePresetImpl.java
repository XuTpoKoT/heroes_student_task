package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army army = new Army();
        List<Unit> result = new ArrayList<>();

        // коэффициенты важности
        final double ATTACK_WEIGHT = 0.7;
        final double HEALTH_WEIGHT = 0.3;

        // считаем "ценность" каждого типа
        Map<Unit, Double> unitScore = new HashMap<>();
        for (Unit u : unitList) {
            double score =
                    ATTACK_WEIGHT * ((double) u.getBaseAttack() / u.getCost()) +
                            HEALTH_WEIGHT * ((double) u.getHealth() / u.getCost());
            unitScore.put(u, score);
        }

        // сортируем по убыванию эффективности
        unitList.sort((a, b) ->
                Double.compare(unitScore.get(b), unitScore.get(a)));

        Map<String, Integer> typeCount = new HashMap<>();
        int currentPoints = 0;
        int globalIndex = 0;

        for (Unit template : unitList) {
            String type = template.getUnitType();
            typeCount.putIfAbsent(type, 0);

            while (typeCount.get(type) < 11 &&
                    currentPoints + template.getCost() <= maxPoints) {
                int x = globalIndex % 3;
                int y = globalIndex / 3;

                Unit copy = new Unit(
                        template.getName() + " " + typeCount.get(type),
                        template.getUnitType(),
                        template.getHealth(),
                        template.getBaseAttack(),
                        template.getCost(),
                        template.getAttackType(),
                        template.getAttackBonuses(),
                        template.getDefenceBonuses(),
                        x,
                        y
                );

                result.add(copy);
                typeCount.put(type, typeCount.get(type) + 1);
                currentPoints += template.getCost();
                globalIndex++;
            }
        }

        army.setUnits(result);
        army.setPoints(currentPoints);
        return army;
    }
}