package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        final int WIDTH = 27;
        final int HEIGHT = 21;

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        // Занятые клетки
        boolean[][] occupied = new boolean[WIDTH][HEIGHT];
        for (Unit unit : existingUnitList) {
            if (unit == null || !unit.isAlive()) {
                continue;
            }
            int x = unit.getxCoordinate();
            int y = unit.getyCoordinate();

            // цель разрешено посещать
            if (x == targetX && y == targetY) {
                continue;
            }

            occupied[x][y] = true;
        }

        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Edge[][] parent = new Edge[WIDTH][HEIGHT];

        Queue<Edge> queue = new ArrayDeque<>();
        queue.add(new Edge(startX, startY));
        visited[startX][startY] = true;

        // 8 направлений (включая диагонали)
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        while (!queue.isEmpty()) {
            Edge current = queue.poll();

            if (current.getX() == targetX && current.getY() == targetY) {
                return restorePath(parent, current);
            }

            for (int i = 0; i < 8; i++) {
                int nx = current.getX() + dx[i];
                int ny = current.getY() + dy[i];

                if (nx < 0 || nx >= WIDTH || ny < 0 || ny >= HEIGHT) {
                    continue;
                }
                if (visited[nx][ny] || occupied[nx][ny]) {
                    continue;
                }

                visited[nx][ny] = true;
                parent[nx][ny] = current;
                queue.add(new Edge(nx, ny));
            }
        }

        return Collections.emptyList();
    }
    private List<Edge> restorePath(Edge[][] parent, Edge end) {
        List<Edge> path = new ArrayList<>();
        Edge current = end;

        while (current != null) {
            path.add(current);
            current = parent[current.getX()][current.getY()];
        }

        Collections.reverse(path);
        return path;
    }

}
