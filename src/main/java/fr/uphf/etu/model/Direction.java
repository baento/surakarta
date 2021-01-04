package fr.uphf.etu.model;

import java.util.Arrays;

/**
 * Une classe représentant les directions cardinales et intercardinales.
 */
public enum Direction {
    NONE(0, 0),
    NORTH(0, -1),
    NORTH_EAST(1, -1),
    EAST(1, 0),
    SOUTH_EAST(1, 1),
    SOUTH(0, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(-1, -1);

    /**
     * Le décalage X permettant de transformer des coordonnées.
     */
    private final int offsetX;

    /**
     * Le décalage Y permettant de transformer des coordonnées.
     */
    private final int offsetY;

    Direction(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Retourne la direction qui possède les décalages passés en paramètres.
     * Si aucune direction ne correspond, {@link Direction#NONE} est retournée.
     *
     * @param x la coordonnée de décalage X.
     * @param y la coordonnée de décalage Y.
     * @return la direction associée, ou {@link Direction#NONE} si aucune autre ne correspond.
     */
    public static Direction valueOf(int x, int y) {
        for (Direction direction : values()) {
            if (direction.offsetX == x && direction.offsetY == y) return direction;
        }

        return NONE;
    }

    /**
     * @return un tableau contenant toutes les directions cardinales.
     */
    public static Direction[] cardinals() {
        return new Direction[]{NORTH, EAST, SOUTH, WEST};
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    /**
     * Détermine si cette direction est cardinale (c'est-à-dire qu'il s'agit du Nord, de l'Est, du Sud ou de l'Ouest).
     *
     * @return {@code true} si cette direction n'est pas {@link Direction#NONE} et qu'elle appartient à l'ensemble {Nord, Est, Sud, Ouest}.
     */
    public boolean isCardinal() {
        return Direction.NONE != this && Arrays.binarySearch(new Direction[]{NORTH, EAST, SOUTH, WEST}, this) >= 0;
    }

    /**
     * @return la direction opposée.
     */
    public Direction opposite() {
        return nthNext(4);
    }

    /**
     * @return la direction précédente, dans le sens des aiguilles d'une montre.
     */
    public Direction previous() {
        return nthNext(-1);
    }

    /**
     * Détermine la précédente direction qui est cardinale. Si la direction actuelle n'est pas cardinale, alors la direction qui la précède est retournée.
     *
     * @return la direction cardinale précédente, dans le sens des aiguilles d'une montre.
     */
    public Direction previousCardinal() {
        return this.isCardinal() ? nthNext(-2) : previous();
    }

    /**
     * @return la direction suivante, dans le sens des aiguilles d'une montre.
     */
    public Direction next() {
        return nthNext(1);
    }

    /**
     * Détermine la direction suivante qui est cardinale. Si la direction actuelle n'est pas cardinale, alors la direction qui la suit est retournée.
     *
     * @return la direction cardinale suivante, dans le sens des aiguilles d'une montre.
     */
    public Direction nextCardinal() {
        return this.isCardinal() ? nthNext(2) : next();
    }

    /**
     * Retourne la n-ième prochaine direction.
     * Si la direction est {@link Direction#NONE}, alors le résultat sera {@link Direction#NONE} quel que soit n.
     *
     * @param n le nombre de prochains.
     * @return la n-ième prochaine direction.
     */
    public Direction nthNext(int n) {
        if (this == NONE) return NONE;

        int index = ordinal() + n;

        while (index >= values().length) {
            index -= values().length - 1;
        }

        while (index <= 0) {
            index += values().length - 1;
        }

        return Direction.values()[index];
    }
}
