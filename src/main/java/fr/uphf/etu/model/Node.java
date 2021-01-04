package fr.uphf.etu.model;

import com.google.common.base.MoreObjects;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Classe représentant un noeud sur le plateau.
 * Un noeud peut contenir un pion ou non.
 */
public class Node extends Circle {
    /**
     * La coordonnée x sur le plateau.
     */
    private final int x;

    /**
     * La coordonnée y sur le plateau.
     */
    private final int y;

    /**
     * Le pion sur ce noeud.
     */
    private Pawn pawn;

    /**
     * Constructeur de noeud.
     *
     * @param x la coordonnée x sur le plateau.
     * @param y la coordonnée y sur le plateau.
     */
    public Node(int x, int y) {
        super(x * 75, y * 75, 20, Color.WHITESMOKE);
        this.setStroke(Color.GRAY);
        this.x = x;
        this.y = y;
    }

    public Pawn getPawn() {
        return pawn;
    }

    /**
     * Associe un pion à ce noeud.
     *
     * @param pawn le pion.
     */
    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    /**
     * @return le joueur auquel appartient le pion de ce noeud, {@link Player#NONE} si ce noeud ne possède pas de pion.
     */
    public Player getPlayer() {
        return pawn != null ? pawn.getPlayer() : Player.NONE;
    }

    /**
     * Crée le pion du joueur sur ce noeud.
     *
     * @param player le joueur auquel appartient le pion.
     */
    public void setPlayer(Player player) {
        this.pawn = new Pawn(player, x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Trouve dans quelle direction le noeud de destination se trouve par rapport au noeud.
     *
     * @param destination le noeud de destination.
     * @return la direction vers le noeud de destination.
     */
    public Direction direction(Node destination) {
        int dx = x - destination.x;
        int dy = y - destination.y;

        return Direction.valueOf((int) Math.signum(dx), (int) Math.signum(dy));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("x", x)
                .add("y", y)
                .toString();
    }
}
