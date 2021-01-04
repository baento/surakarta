package fr.uphf.etu.model;

import com.google.common.base.MoreObjects;
import fr.uphf.etu.Maths;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcTo;

/**
 * Une classe décrivant un arc sur le plateau, contenant les noeuds d'extrémité et de centre utilisés pour les calculs de direction.
 */
public class Curve extends Arc {
    /**
     * Le noeud qui est le centre de l'arc.
     */
    private final Node centerNode;

    /**
     * Le noeud d'extrémité A de l'arc.
     */
    private final Node nodeA;

    /**
     * Le noeud d'extrémité B de l'arc.
     */
    private final Node nodeB;

    /**
     * Constructeur de la courbe.
     * Détermine automatiquement l'orientation de l'arc par les noeuds d'extrémité et de centre.
     * @param centerNode le noeud du centre.
     * @param nodeA le noeud d'extrémité A.
     * @param nodeB le noeud d'extrémité B.
     * @param color la couleur de l'arc.
     */
    public Curve(Node centerNode, Node nodeA, Node nodeB, Color color) {
        super(centerNode.getX() * 75, centerNode.getY() * 75, Maths.distance(nodeA, centerNode) * 75, Maths.distance(nodeB, centerNode) * 75, Math.toDegrees(Maths.normalize(Maths.angle(nodeA, nodeB))) + 45, 270);
        this.setFill(Color.TRANSPARENT);
        this.setStroke(color);
        this.setStrokeWidth(5);

        this.centerNode = centerNode;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
    }

    public Node getNodeA() {
        return nodeA;
    }

    public Node getNodeB() {
        return nodeB;
    }

    /**
     * Détermine la direction de sortie de l'arc lorsqu'on y rentre à partir du noeud et de la direction passée en paramètre.
     * Si le noeud n'est pas une extrémité de l'arc, ou si la direction ne permet pas de rentrer dans l'arc, {@link Direction#NONE} est retournée.
     *
     * @param node      le noeud d'entrée.
     * @param direction la direction d'entrée.
     * @return la direction de sortie, ou {@link Direction#NONE} si le noeud d'entrée n'est pas une extrémité de l'arc, ou si la direction ne permet pas de passer par l'arc.
     */
    public Direction computeDirection(Node node, Direction direction) {
        Direction directionA = centerNode.direction(nodeA);
        Direction directionB = centerNode.direction(nodeB);

        return direction == directionA && node == nodeB ? directionB.opposite() : direction == directionB && node == nodeA ? directionA.opposite() : Direction.NONE;
    }

    /**
     * Détermine le noeud de sortie de l'arc lorsqu'on y rentre à partir du noeud et de la direction passée en paramètre.
     * Si le noeud n'est pas une extrémité de l'arc, ou si la direction ne permet pas de rentrer dans l'arc, {@code null} est retourné.
     *
     * @param node      le noeud d'entrée.
     * @param direction la direction d'entrée.
     * @return le noeud de sortie, ou {@code null} si le noeud d'entrée n'est pas une extrémité de l'arc, ou si la direction ne permet pas de passer par l'arc.
     */
    public Node computeNode(Node node, Direction direction) {
        Direction directionA = centerNode.direction(nodeA);
        Direction directionB = centerNode.direction(nodeB);

        return direction == directionB && node == nodeA ? nodeB : direction == directionA && node == nodeB ? nodeA : null;
    }

    /**
     * Génère un chemin passant par l'arc, en prenant en compte le sens de parcours selon le noeud d'entrée.
     * Si le noeud d'entrée ou la direction ne permettent pas de passer par l'arc, {@code null} est retourné.
     *
     * @param node      le noeud d'entrée.
     * @param direction la direction d'entrée.
     * @return un chemin permettant de parcourir l'arc.
     */
    public ArcTo path(Node node, Direction direction) {
        Direction computedDirection = computeDirection(node, direction);
        Node destination = computeNode(node, direction);

        return computedDirection != Direction.NONE ? new ArcTo(this.getRadiusX(), this.getRadiusY(), 0, destination.getX() * 75, destination.getY() * 75, true, computedDirection.nextCardinal() == direction) : null;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("centerNode", centerNode)
                .add("nodeA", nodeA)
                .add("directionA", centerNode.direction(nodeA))
                .add("nodeB", nodeB)
                .add("directionB", centerNode.direction(nodeB))
                .toString();
    }
}