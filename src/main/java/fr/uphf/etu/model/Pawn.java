package fr.uphf.etu.model;

import com.google.common.base.MoreObjects;
import javafx.animation.FillTransition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Classe représentant un pion sur le plateau.
 * Il est contenu par le noeud sur lequel il se situe.
 */
public class Pawn extends Circle {
    /**
     * Le joueur auquel appartient ce pion.
     */
    private final Player player;

    /**
     * La coordonnée x sur le plateau.
     */
    private int x;

    /**
     * La coordonnée y sur le plateau.
     */
    private int y;

    /**
     * Détermine si ce pion est actuellement en train d'être déplacé (mouvement ou secouement).
     */
    private boolean moving = false;

    /**
     * Constructeur de pion.
     *
     * @param player le joueur auquel appartient ce pion.
     * @param x      la coordonnée x sur le plateau.
     * @param y      la coordonnée y sur le plateau.
     */
    public Pawn(Player player, int x, int y) {
        super(x * 75, y * 75, 17, player.getColor());
        this.player = player;
        this.x = x;
        this.y = y;
    }

    /**
     * Définit la couleur du cercle représentant le pion en fonction de {@code selection}.
     * Cette méthode utilise {@link FillTransition} pour un changement de couleur fluide.
     *
     * @param selection {@code true} pour sélectionné, {@code false} pour désélectionner.
     * @see FillTransition
     */
    public void setSelection(boolean selection) {
        FillTransition fillTransition = new FillTransition(Duration.seconds(0.25), this);
        fillTransition.setToValue(selection ? player.getColor().darker() : player.getColor());
        fillTransition.play();
    }

    public boolean isMoving() {
        return moving;
    }

    /**
     * Définit ce pion en mouvement ou non.
     *
     * @param moving {@code true} si ce pion est en mouvement.
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     * Définit la position sur le plateau.
     *
     * @param x la coordonnée x sur le plateau.
     * @param y la coordonnée y sur le plateau.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("player", player)
                .add("x", x)
                .add("y", y)
                .toString();
    }
}