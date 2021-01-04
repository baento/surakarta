package fr.uphf.etu.model;

import javafx.scene.paint.Color;

/**
 * Une classe représentant les joueurs.
 */
public enum Player {
    NONE(),
    P1(Color.SALMON),
    P2(Color.LIGHTSTEELBLUE);

    /**
     * La couleur du joueur.
     */
    private final Color color;

    Player() {
        this(Color.TRANSPARENT);
    }

    Player(Color color) {
        this.color = color;
    }

    /**
     * Retourne la couleur du joueur.
     *
     * @return la couleur du joueur.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Retourne le joueur qui suit dans l'ordre de définition de l'énumération.
     * Cette fonction ne tient pas compte de la valeur {@link Player#NONE} pour déterminer le suivant, ainsi on a :
     * <table border="1">
     *     <caption>Valeurs et suivants</caption>
     *     <thead><tr><td>Valeur</td><td>Suivant</td></tr></thead>
     *     <tr><td>{@link Player#NONE}</td><td>{@link Player#P1}</td></tr>
     *     <tr><td>{@link Player#P1}</td><td>{@link Player#P2}</td></tr>
     *     <tr><td>{@link Player#P2}</td><td>{@link Player#P1}</td></tr>
     * </table>
     *
     * @return le joueur suivant.
     */
    public Player next() {
        int index = ordinal() + 1;

        if (index >= values().length) {
            index = 1;
        }

        return Player.values()[index];
    }

    /**
     * Crée une chaîne de caractères contenant le numéro du joueur et une indication si ce joueur est une IA.
     *
     * @param aiFlag si le joueur est une IA.
     * @return une chaîne.
     */
    public String formatDisplay(boolean aiFlag) {
        StringBuilder builder = new StringBuilder(String.valueOf(ordinal()));

        if (aiFlag) {
            builder.append(" (IA)");
        }

        return builder.toString();
    }
}