package fr.uphf.etu;

import fr.uphf.etu.model.Node;

/**
 * Une classe permettant d'effectuer des calculs dans le jeu.
 */
public class Maths {

    /**
     * Calcule la distance euclidienne entre deux noeuds.
     *
     * @param origin      le noeud d'origine.
     * @param destination le noeud de destination.
     * @return la distance euclidienne entre le noeud d'origine et la destination.
     * @see Maths#distance(Node, int, int)
     */
    public static double distance(Node origin, Node destination) {
        return distance(origin, destination.getX(), destination.getY());
    }

    /**
     * Calcule la distance euclidienne d'un noeud à des coordonnées.
     *
     * @param origin le noeud d'origine.
     * @param x      la coordonnée x de destination.
     * @param y      la coordonnée y de destination.
     * @return la distance euclidienne entre le noeud d'origine et les coordonnées passées en paramètre.
     */
    public static double distance(Node origin, int x, int y) {
        return Math.sqrt(Math.pow(origin.getX() - x, 2) + Math.pow(origin.getY() - y, 2));
    }

    /**
     * Calcule l'angle entre deux noeuds.
     *
     * @param origin      le noeud d'origine.
     * @param destination le noeud de destination.
     * @return l'angle (en radians) entre le noeud d'origine et de destination.
     */
    public static double angle(Node origin, Node destination) {
        return Math.atan2(destination.getY() - origin.getY(), destination.getX() - origin.getX());
    }

    /**
     * Normalise un angle entre <i>0</i> et <i>2pi</i> radians.
     *
     * @param angle l'angle à normaliser (en radians).
     * @return l'angle normalisé entre <i>0</i> et <i>2pi</i> radians.
     */
    public static double normalize(double angle) {
        return angle - (2 * Math.PI) * Math.floor(angle / (2 * Math.PI));
    }
}