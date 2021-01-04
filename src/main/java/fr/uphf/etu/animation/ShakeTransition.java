package fr.uphf.etu.animation;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Une classe qui permet d'effectuer une animation qui "secoue" un élément graphique de gauche à droite par translation.
 */
public class ShakeTransition extends Transition {
    /**
     * L'élément graphique.
     */
    private final Node node;

    /**
     * La translation X d'origine de l'élément graphique.
     */
    private double originalTranslateX;

    /**
     * Constructeur de la transition.
     * @param duration la durée de la transition.
     * @param node le node à animer.
     */
    public ShakeTransition(Duration duration, Node node) {
        this.setCycleDuration(duration);
        this.node = node;
    }

    public double getOriginalTranslateX() {
        return originalTranslateX;
    }

    public void setOriginalTranslateX(double originalTranslateX) {
        this.originalTranslateX = originalTranslateX;
    }

    @Override
    protected void interpolate(double frac) {
        this.node.setTranslateX(this.originalTranslateX + Math.sin(frac * Math.PI * getCycleCount()) * 2);
    }
}
