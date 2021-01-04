package fr.uphf.etu.model;

import javafx.scene.paint.Color;

import java.util.stream.Stream;

/**
 * Une classe représentant le plateau du jeu avec les noeuds, les pions et les courbes.
 */
public class Board {
    /**
     * Les noeuds du plateau.
     */
    private final Node[][] nodes;

    /**
     * Représentation des courbes par des arcs et les extrémités.
     */
    private final Curve[] curves;

    /**
     * Constructeur du plateau.
     * Crée les noeuds et les courbes, et place les pions des joueurs.
     */
    public Board() {
        this.nodes = new Node[6][6];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                nodes[i][j] = new Node(i, j); //Création de noeuds
            }

            //Pions du joueur 1
            nodes[i][0].setPlayer(Player.P1);
            nodes[i][1].setPlayer(Player.P1);

            //Pions du joueur 2
            nodes[i][4].setPlayer(Player.P2);
            nodes[i][5].setPlayer(Player.P2);
        }

        //Création de courbes
        this.curves = new Curve[8];
        this.curves[0] = new Curve(nodes[0][0], nodes[0][1], nodes[1][0], Color.LIGHTCORAL);
        this.curves[1] = new Curve(nodes[0][0], nodes[0][2], nodes[2][0], Color.LIGHTSKYBLUE);
        this.curves[2] = new Curve(nodes[5][0], nodes[5][1], nodes[4][0], Color.LIGHTCORAL);
        this.curves[3] = new Curve(nodes[5][0], nodes[5][2], nodes[3][0], Color.LIGHTSKYBLUE);
        this.curves[4] = new Curve(nodes[5][5], nodes[5][4], nodes[4][5], Color.LIGHTCORAL);
        this.curves[5] = new Curve(nodes[5][5], nodes[5][3], nodes[3][5], Color.LIGHTSKYBLUE);
        this.curves[6] = new Curve(nodes[0][5], nodes[0][4], nodes[1][5], Color.LIGHTCORAL);
        this.curves[7] = new Curve(nodes[0][5], nodes[0][3], nodes[2][5], Color.LIGHTSKYBLUE);
    }

    public Node[][] nodes() {
        return nodes;
    }

    public Curve[] curves() {
        return curves;
    }

    /**
     * Retourne le noeud aux coordonnées (x, y).
     * @param x la coordonnée X.
     * @param y la coordonnée Y.
     * @return le pion de coordonnées (x, y) sur le plateau.
     */
    public Node node(int x, int y) {
        return x >= 0 && x < 6 && y >= 0 && y < 6 ? nodes[x][y] : null;
    }

    /**
     * Détermine quel noeud se trouve dans la direction passée en paramètre à partir du noeud donné.
     * Si la direction donnée ferait passer par une courbe, alors le noeud au bout de la courbe est retourné.
     *
     * @param node      le noeud de départ.
     * @param direction la direction.
     * @return le noeud d'arrivée, s'il y en a un.
     */
    public Node node(Node node, Direction direction) {
        Curve curve = this.curve(node);

        if (curve != null && curve.computeDirection(node, direction) != Direction.NONE) {
            return curve.computeNode(node, direction);
        }

        return node(node.getX() + direction.getOffsetX(), node.getY() + direction.getOffsetY());
    }

    /**
     * Retourne la courbe qui a pour extrémité le noeud passé en paramètre.
     * @param node le noeud.
     * @return la courbe auquelle est associé le noeud, sinon {@code null}.
     */
    public Curve curve(Node node) {
        return Stream.of(curves).filter(c -> c.getNodeA() == node || c.getNodeB() == node).findFirst().orElse(null);
    }
}