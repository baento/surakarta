package fr.uphf.etu.controller;

import fr.uphf.etu.Surakarta;
import fr.uphf.etu.model.Direction;
import fr.uphf.etu.model.Node;
import fr.uphf.etu.model.Player;

import java.util.*;

/**
 * Une classe pour contrôler la partie du jeu.
 */
public class GameController {
    /**
     * Le contrôleur de pions.
     */
    public final PawnController pawnController;
    /**
     * L'application.
     */
    private final Surakarta surakarta;
    /**
     * Le contrôleur de noeuds.
     */
    private final NodeController nodeController;

    /**
     * Le contrôleur du plateau.
     */
    private final BoardController boardController;

    /**
     * Associe à chaque joueur son score.
     */
    private final Map<Player, Integer> points;

    /**
     * Associe {@code true} à chaque joueur qui est commandé par une IA.
     */
    private final Map<Player, Boolean> ai;

    /**
     * Le joueur actuel.
     */
    private Player currentPlayer;


    public GameController(Surakarta surakarta) {
        this.surakarta = surakarta;

        this.pawnController = new PawnController(this);
        this.nodeController = new NodeController(this);
        this.boardController = new BoardController(this);

        this.points = new EnumMap<>(Player.class);
        this.ai = new EnumMap<>(Player.class);

        this.currentPlayer = Player.NONE;
    }

    public Surakarta getSurakarta() {
        return surakarta;
    }

    public PawnController getPawnController() {
        return pawnController;
    }

    public NodeController getNodeController() {
        return nodeController;
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Détermine quel est le joueur qui a atteint 12 points, et donc a gagné la partie.
     * @return le joueur ayant atteint 12 points, ou {@link Player#NONE} si aucun joueur ne correspond à ce prérequis.
     */
    public Player getWinner() {
        for (Map.Entry<Player, Integer> entry : this.points.entrySet()) {
            if (entry.getValue() >= 12) {
                return entry.getKey();
            }
        }

        return Player.NONE;
    }

    /**
     * Retourne le score du joueur.
     * @param player le joueur.
     * @return le score du joueur, ou <i>0</i> si aucun score n'est attribué pour le moment.
     */
    public int getScore(Player player) {
        return points.getOrDefault(player, 0);
    }

    /**
     * Incrémente le score du joueur.
     * @param player le joueur.
     */
    public void incScore(Player player) {
        this.points.merge(player, 1, Integer::sum);
    }

    /**
     * Retourne {@code true} si le joueur est une IA.
     * @param player le joueur.
     * @return {@code true} si le joueur est contrôlé par une IA.
     */
    public boolean isAI(Player player) {
        return ai.getOrDefault(player, false);
    }

    /**
     * Définit le joueur passé en paramètre comme piloté par une IA.
     *
     * @param player le joueur.
     */
    public void setAI(Player player) {
        Surakarta.getLogger().debug("Joueur {} défini comme contrôlé par une IA", player);
        this.ai.put(player, true);
    }

    /**
     * Lance le tour suivant.
     * Si un joueur est déterminé gagnant, alors la partie se termine.
     * Si le joueur devenant actuel est une IA, son tour est joué automatiquement.
     */
    public void nextTurn() {
        Player winner = this.getWinner();
        if (winner != Player.NONE) { //Un joueur a gagné
            this.surakarta.endPopup(winner);
            return;
        }

        this.currentPlayer = currentPlayer.next(); //Joueur suivant
        Surakarta.getLogger().info("Début du tour, au tour du joueur {}", this.currentPlayer.formatDisplay(this.isAI(this.currentPlayer)));
        this.surakarta.refreshUI();

        if (this.isAI(this.currentPlayer)) {
            this.AITurn(); //Faire jouer l'IA
        }
    }

    /**
     * Fait jouer l'IA.
     * Elle tente d'abord de faire une capture de pion avant de déplacer aléatoirement un de ses pions.
     */
    public void AITurn() {
        List<Node> playerNodes = this.boardController.playerNodes(this.currentPlayer);
        Collections.shuffle(playerNodes); //Choix du pion à déplacer aléatoire

        List<Direction> directions = Arrays.asList(Direction.values());
        Collections.shuffle(directions); //Ordre des directions aléatoire

        for (Node origin : playerNodes) { //Essayer une capture
            for (Node destination : this.boardController.playerNodes(this.currentPlayer.next())) { //Les noeuds contenant des pions du joueur adverse
                for (Direction direction : directions) {
                    if (direction != Direction.NONE) {
                        if (direction.isCardinal() && this.boardController.reachable(origin, destination, direction)) { //Une prise est possible
                            Surakarta.getLogger().trace("Capture de {} à partir de {} dans la direction {}", destination, origin, direction);
                            this.pawnController.pawnClick(origin.getPawn());
                            this.nodeController.nodeClick(destination);
                            return;
                        }
                    }
                }
            }
        }

        for (Node origin : playerNodes) { //Déplacement aléatoire
            for (Direction direction : directions) {
                if (direction != Direction.NONE) {
                    Node destination = this.boardController.getBoard().node(origin, direction);

                    if (destination != null && this.boardController.reachable(origin, destination)) { //Un simple déplacement est possible
                        this.pawnController.pawnClick(origin.getPawn());
                        this.nodeController.nodeClick(destination);
                        return;
                    }
                }
            }
        }
    }
}
