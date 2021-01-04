package fr.uphf.etu.controller;

import fr.uphf.etu.Surakarta;
import fr.uphf.etu.model.Node;
import fr.uphf.etu.model.Pawn;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Une classe pour contrôler les pions.
 */
public class PawnController implements EventHandler<MouseEvent> {
    /**
     * Le contrôleur de partie.
     */
    private final GameController gameController;

    /**
     * Le pion actuellement sélectionné, s'il y en a un.
     */
    private Pawn selection;

    /**
     * Constructeur du contrôleur.
     * @param gameController le contrôleur de partie.
     */
    public PawnController(GameController gameController) {
        this.gameController = gameController;
    }

    public Pawn getSelection() {
        return selection;
    }

    /**
     * Définit le pion sélectionné.
     * Si un pion était précédemment sélectionné, il est d'abord désélectionné.
     *
     * @param pawn le pion.
     */
    public void setSelection(Pawn pawn) {
        if (this.selection != null) {
            this.selection.setSelection(false);
        }

        if (pawn != null) {
            pawn.setSelection(true);
        }

        this.selection = pawn;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof Pawn) {
            if (!this.gameController.isAI(this.gameController.getCurrentPlayer())) { //Les clics ne sont pas permis quand l'IA joue
                pawnClick((Pawn) event.getSource());
            }
        }
    }

    /**
     * Capture un pion, ajoute un point au joueur qui le capture et le supprime des éléments graphiques.
     *
     * @param pawn le pion capturé.
     */
    public void capture(Pawn pawn) {
        Surakarta.getLogger().info("Capture du pion {}", pawn);
        this.gameController.incScore(pawn.getPlayer().next());

        this.gameController.getSurakarta().removePawn(pawn);
        this.gameController.getSurakarta().refreshUI();
    }

    /**
     * Appelé lorsqu'on clique (ou qu'on simule un clic, pour une IA) sur un pion.
     *
     * @param pawn le pion cliqué.
     */
    public void pawnClick(Pawn pawn) {
        if (this.selection != null) { //Un pion est déjà sélectionné
            if (this.selection.getPlayer() != this.gameController.getCurrentPlayer()) { //Le pion sélectionné n'appartient pas au joueur actuel
                this.setSelection(null);
                return;
            }

            if (pawn.getPlayer() != this.gameController.getCurrentPlayer()) { //Le pion cliqué appartient à l'adversaire
                BoardController boardController = this.gameController.getBoardController();
                Node node = boardController.getBoard().node(pawn.getX(), pawn.getY());

                if (boardController.move(this.selection, node)) { //On déplace le pion sélectionné pour capturer le pion cliqué
                    return;
                }
            }
        }

        if (pawn.getPlayer() == this.gameController.getCurrentPlayer()) { //On sélectionne ce pion
            this.setSelection(pawn);
        }
    }
}
