package fr.uphf.etu.controller;

import fr.uphf.etu.Surakarta;
import fr.uphf.etu.model.Node;
import fr.uphf.etu.model.Pawn;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Une classe pour contrôler les noeuds.
 */
public class NodeController implements EventHandler<MouseEvent> {
    /**
     * Le contrôleur de partie.
     */
    private final GameController gameController;

    /**
     * Le constructeur du contrôleur.
     * @param gameController le contrôleur de partie.
     */
    public NodeController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof Node) {
            if (!gameController.isAI(gameController.getCurrentPlayer())) { //Les clics ne sont pas permis quand l'IA joue
                nodeClick((Node) event.getSource());
            }
        }
    }

    /**
     * Appelé lorsqu'on clique (ou simule un clic, pour les IA) sur un noeud.
     *
     * @param node le noeud cliqué.
     */
    public void nodeClick(Node node) {
        PawnController pawnController = this.gameController.getPawnController();

        if (node.getPawn() != null) { //Le noeud contient un pion, on considère que le clic était destiné au pion
            Surakarta.getLogger().trace("Clic reçu par le noeud {} qui contient un pion {}, transfert au contrôleur de pion", node, node.getPawn());
            pawnController.pawnClick(node.getPawn());
            return;
        }

        Pawn selection = pawnController.getSelection();

        if (selection != null) { //Un pion est sélectionné
            BoardController boardController = this.gameController.getBoardController();
            boardController.move(selection, node); //On déplace le pion sélectionné sur ce noeud
        }
    }
}