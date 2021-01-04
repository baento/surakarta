package fr.uphf.etu.controller;

import fr.uphf.etu.Maths;
import fr.uphf.etu.Surakarta;
import fr.uphf.etu.model.*;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Une classe pour contrôler le plateau.
 */
public class BoardController {
    /**
     * Le contrôleur de la partie.
     */
    private final GameController gameController;

    /**
     * Le plateau.
     */
    private final Board board;

    /**
     * Constructeur du contrôleur.
     * Initialise le plateau.
     * @param gameController le contrôleur de la partie.
     */
    public BoardController(GameController gameController) {
        this.gameController = gameController;

        this.board = new Board();
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Tente de déplacer un pion vers le noeud de destination.
     * Si le noeud de destination n'est pas atteignable (obstacle, capture sans passage par une courbe, ...), alors le pion joue une animation de secouement.
     *
     * @param pawn        le pion à déplacer.
     * @param destination le noeud de destination.
     * @return {@code true} si le pion a pu être déplacé, sinon {@code} false.
     */
    public boolean move(Pawn pawn, Node destination) {
        Node node = this.board.node(pawn.getX(), pawn.getY());

        if (this.reachable(node, destination)) {
            Surakarta.getLogger().trace("Déplacement de {} vers {}", pawn, destination);
            Path path = this.path(node, destination);
            gameController.getSurakarta().animateMove(node, destination, path);
            return true;
        } else {
            gameController.getSurakarta().animateShake(pawn);
            return false;
        }
    }

    /**
     * Construit une liste contenant tous les noeuds qui possèdent un pion du joueur passé en paramètre.
     *
     * @param player le joueur.
     * @return une liste contenant les noeuds ayant un pion appartenant au joueur.
     */
    public List<Node> playerNodes(Player player) {
        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (this.board.node(i, j).getPlayer() == player) {
                    nodes.add(this.board.node(i, j));
                }
            }
        }

        return nodes;
    }

    /**
     * Construit un chemin entre le noeud d'origine et le noeud de destination.
     * Si aucun chemin n'existe, {@code null} est retourné.
     *
     * @param origin      le noeud de départ.
     * @param destination le noeud d'arrivée.
     * @return un chemin, ou {@code null} si aucun chemin n'existe.
     */
    public Path path(Node origin, Node destination) {
        if (destination.getPlayer() == Player.NONE && (int) Maths.distance(origin, destination) <= 1) {
            MoveTo moveTo = new MoveTo(origin.getX() * 75, origin.getY() * 75);
            LineTo lineTo = new LineTo(destination.getX() * 75, destination.getY() * 75);

            return new Path(moveTo, lineTo);
        } else if (destination.getPlayer() != origin.getPlayer()) {
            for (Direction direction : Direction.cardinals()) {
                if (reachable(origin, destination, direction)) {
                    return path(origin, destination, direction);
                }
            }
        }

        return null;
    }

    /**
     * Détermine si le noeud de destination est atteignable à partir du chemin d'origine, peu importe la direction.
     * Celui-ci est atteignable si le noeud de destination est libre et directement adjacent au noeud de départ, ou bien s'il existe un chemin passant par une boucle et sans obstacle.
     *
     * @param origin      le noeud de départ.
     * @param destination le noeud d'arrivée.
     * @return {@code true} si l'algorithme parvient à trouver un chemin vers le noeud de destination.
     * @see BoardController#reachable(Node, Node, Direction)
     */
    public boolean reachable(Node origin, Node destination) {
        if (destination.getPlayer() == Player.NONE && (int) Maths.distance(origin, destination) <= 1) {
            Surakarta.getLogger().trace("Un simple déplacement est possible de {} à {}", origin, destination);
            return true;
        } else if (destination.getPlayer() != origin.getPlayer()) {
            for (Direction direction : Direction.cardinals()) {
                if (reachable(origin, destination, direction)) {
                    Surakarta.getLogger().trace("Une capture est possible de {} à {} dans la direction {}", origin, destination, direction);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Détermine si le noeud de destination est atteignable à partir du noeud d'origine, en partant de la direction passée en paramètre.
     * Cette fonction retourne {@code true} si une prise est possible, sans obstacle et passant par une boucle.
     *
     * @param origin      le noeud de départ.
     * @param destination le noeud d'arrivée.
     * @param direction   la direction de progression.
     * @return true si le noeud de destination est atteignable en partant du noeud d'origine dans la direction donnée.
     */
    public boolean reachable(Node origin, Node destination, Direction direction) {
        Surakarta.getLogger().trace("Tentative de détermination d'accessibilité depuis {} à {} par {}", origin, destination, direction);
        boolean curvePassed = false;
        Node currentNode = origin;

        if (destination.getPlayer() == Player.NONE) { //Le noeud d'arrivée est libre, aucune prise possible
            Surakarta.getLogger().trace("Le noeud d'arrivée est vide, pas de prise possible");
            return false;
        }

        while (currentNode != null && currentNode != destination) {
            if (currentNode.differentTracks(destination) || currentNode != origin && currentNode.getPlayer() != Player.NONE) { //Il y a un obstacle
                Surakarta.getLogger().trace("Il y a un obstacle, pas de prise possible");
                return false;
            }

            Curve curve = this.board.curve(currentNode);

            if (curve != null && curve.computeDirection(currentNode, direction) != Direction.NONE) { //Progression par une courbe
                curvePassed = true;

                Direction previousDirection = direction;
                direction = curve.computeDirection(currentNode, previousDirection);
                currentNode = curve.computeNode(currentNode, previousDirection);
                Surakarta.getLogger().trace("Passage par la courbe {}, nouvelle direction : {} (précédemment {})", curve, direction, previousDirection);
            } else { //Progression en ligne droite
                currentNode = this.board.node(currentNode, direction);
                Surakarta.getLogger().trace("Progression en ligne droite vers {} jusque {}", direction, currentNode);
            }
        }

        return curvePassed && currentNode != null;
    }

    /**
     * Détermine le chemin à prendre du noeud de départ au noeud d'arrivée, en progressant dans la direction passée en paramètre.
     *
     * @param origin      le noeud de départ.
     * @param destination le noeud d'arrivée.
     * @param direction   la direction de progression.
     * @return un chemin, ou {@code null} si aucun chemin valide ne peut être construit (obstacle, noeud d'arrivée vide, pas de boucle parcourue...).
     */
    public Path path(Node origin, Node destination, Direction direction) {
        Path path = new Path(new MoveTo(origin.getX() * 75, origin.getY() * 75));
        boolean curvePassed = false;
        Node currentNode = origin;

        if (currentNode.differentTracks(destination) || destination.getPlayer() == Player.NONE) {
            return null;
        }

        while (currentNode != destination) {
            if (currentNode != origin && currentNode.getPlayer() != Player.NONE) { //Il y a un obstacle
                return null;
            }

            Curve curve = this.board.curve(currentNode);

            if (curve != null && curve.computeDirection(currentNode, direction) != Direction.NONE) { //Passage par une courbe
                curvePassed = true;

                path.getElements().add(curve.path(currentNode, direction));

                Direction previousDirection = direction;
                direction = curve.computeDirection(currentNode, previousDirection);
                currentNode = curve.computeNode(currentNode, previousDirection);
            } else {
                currentNode = this.board.node(currentNode, direction);

                if (currentNode != null) {
                    path.getElements().add(new LineTo(currentNode.getX() * 75, currentNode.getY() * 75));
                } else break;
            }
        }

        return curvePassed ? path : null;
    }
}
