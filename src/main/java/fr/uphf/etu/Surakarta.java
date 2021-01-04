package fr.uphf.etu;

import fr.uphf.etu.animation.ShakeTransition;
import fr.uphf.etu.controller.GameController;
import fr.uphf.etu.controller.PawnController;
import fr.uphf.etu.model.Board;
import fr.uphf.etu.model.Node;
import fr.uphf.etu.model.Pawn;
import fr.uphf.etu.model.Player;
import javafx.animation.FillTransition;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * La classe principale du programme.
 */
public class Surakarta extends Application {
    /**
     * Le loggeur global de l'application.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Contient les scores cumulés de chaque joueur.
     */
    private final Map<Player, Integer> totalScores = new EnumMap<>(Player.class);
    /**
     * Le groupe contenant les éléments graphiques.
     */
    private final Group group;
    /**
     * Le texte affiché au dessus du plateau.
     * Il contient "Joueur 1" ou "Joueur 2" en selon le joueur actuel.
     */
    private final Text statusText;
    /**
     * Le texte affiché contenant le score du joueur 1 pour la partie actuelle.
     */
    private final Text scoreP1Text;
    /**
     * Le texte affiché contenant le score du joueur 1 au total.
     */
    private final Text scoreP1TotalText;
    /**
     * Le texte affiché contenant le score du joueur 2.
     */
    private final Text scoreP2Text;
    /**
     * Le texte affiché contenant le score du joueur 2 au total.
     */
    private final Text scoreP2TotalText;
    /**
     * Le contrôleur de la partie actuelle.
     */
    private GameController game;
    /**
     * La fenêtre principale du jeu.
     */
    private Stage stage;

    /**
     * Constructeur de la classe.
     * Initialise le plateau et place les pions aux positions initiales.
     */
    public Surakarta() {
        this.group = new Group();

        this.statusText = new Text();
        this.statusText.setFill(Player.NONE.getColor());
        this.statusText.setFont(Font.font("Arial", 24));

        this.scoreP1Text = new Text();
        this.scoreP1Text.setFill(Player.P1.getColor());
        this.scoreP1Text.setFont(Font.font("Arial", 24));

        this.scoreP1TotalText = new Text();
        this.scoreP1TotalText.setFill(Player.P1.getColor());
        this.scoreP1TotalText.setFont(Font.font("Arial", 12));

        this.scoreP2Text = new Text();
        this.scoreP2Text.setFill(Player.P2.getColor());
        this.scoreP2Text.setFont(Font.font("Arial", 24));

        this.scoreP2TotalText = new Text();
        this.scoreP2TotalText.setFill(Player.P2.getColor());
        this.scoreP2TotalText.setFont(Font.font("Arial", 12));

        this.initialize();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Initialise une partie.
     */
    public void initialize() {
        logger.info("Initialisation d'une partie");
        this.game = new GameController(this);
        this.group.getChildren().clear();

        this.buildGroup(this.group, this.game);
        this.refreshUI();
    }

    /**
     * Construit le groupe d'éléments graphiques à partir du contrôleur de partie passé en paramètre.
     *
     * @param groupe le groupe à construire.
     * @param gameController le contrôleur du jeu.
     */
    private void buildGroup(Group groupe, GameController gameController) {
        logger.debug("Construction du groupe");
        Board board = gameController.getBoardController().getBoard();

        Rectangle rectangle = new Rectangle(-175, -175, 725, 725);
        rectangle.setFill(Color.TRANSPARENT);
        groupe.getChildren().add(rectangle);

        //Construction de la grille
        for (int i = 0; i < 6; i++) {
            Line horizontalLine = new Line(0, i * 75, 375, i * 75);
            Line verticalLine = new Line(i * 75, 0, (i * 75), 375);

            //Pistes rouge et bleue
            if (i == 1 || i == 4) {
                horizontalLine.setStrokeWidth(5);
                verticalLine.setStrokeWidth(5);
                horizontalLine.setStroke(Color.LIGHTCORAL);
                verticalLine.setStroke(Color.LIGHTCORAL);
            } else if (i == 2 || i == 3) {
                horizontalLine.setStrokeWidth(5);
                verticalLine.setStrokeWidth(5);
                horizontalLine.setStroke(Color.LIGHTSKYBLUE);
                verticalLine.setStroke(Color.LIGHTSKYBLUE);
            }

            groupe.getChildren().addAll(horizontalLine, verticalLine);
        }

        //Construction des courbes
        groupe.getChildren().addAll(board.curves());

        List<Pawn> pawns = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Node node = board.node(i, j);
                node.setOnMouseClicked(game.getNodeController());

                if (node.getPlayer() != Player.NONE) {
                    Pawn pawn = node.getPawn();
                    pawn.setOnMouseClicked(game.getPawnController());
                    pawns.add(pawn);
                }
            }

            groupe.getChildren().addAll(board.nodes()[i]);
        }

        groupe.getChildren().addAll(pawns);
    }

    /**
     * Déplace un pion d'un noeud d'origine au noeud de destination.
     * Si un pion est présent au noeud de destination, il est capturé.
     * Une transition est utilisée selon le chemin passé pour un déplacement fluide.
     * A la fin de la transition, le tour suivant est lancé.
     *
     * @param origin      le noeud de départ.
     * @param destination le noeud d'arrivée.
     * @param path        le chemin à suivre.
     * @see PawnController#capture(Pawn)
     */
    public void animateMove(Node origin, Node destination, Path path) {
        Pawn pawn = origin.getPawn();

        if (pawn != null) {
            logger.trace("Animation de déplacement {} de {} à {}", pawn, origin, destination);
            pawn.toFront(); //Le pion doit recouvrir sa prise
            pawn.setMoving(true);

            origin.setPawn(null);

            //Animation de déplacement
            PathTransition pathTransition = new PathTransition();
            pathTransition.setNode(pawn);
            pathTransition.setPath(path);
            pathTransition.setOrientation(PathTransition.OrientationType.NONE);
            pathTransition.setDuration(Duration.seconds(0.25D * path.getElements().size()));
            pathTransition.setOnFinished(e -> {
                logger.trace("Animation de déplacement - terminée");
                Pawn oldPawn = destination.getPawn();

                if (oldPawn != null) {
                    game.pawnController.capture(oldPawn);
                }

                destination.setPawn(pawn);
                pawn.setPosition(destination.getX(), destination.getY());

                pawn.setMoving(false);
                game.getPawnController().setSelection(null);

                game.nextTurn();
            });
            pathTransition.playFromStart();
        }
    }

    /**
     * Joue un {@link ShakeTransition} qui "secoue" horizontalement le cercle représentant le pion.
     * Le pion est marqué "en mouvement" pour éviter de superposer les animations de déplacement et de secouement.
     * S'il est déjà en mouvement, alors le pion ne se secouera pas.
     *
     * @param pawn le pion à animer.
     * @see ShakeTransition
     * @see Pawn#setMoving(boolean)
     * @see Pawn#isMoving()
     */
    public void animateShake(Pawn pawn) {
        if (!pawn.isMoving()) {
            logger.trace("Animation de secouement de {}", pawn);
            ShakeTransition shakeTransition = new ShakeTransition(Duration.seconds(0.25D), pawn);
            shakeTransition.setOriginalTranslateX(pawn.getTranslateX());
            shakeTransition.setOnFinished(v -> {
                pawn.setTranslateX(shakeTransition.getOriginalTranslateX());
                pawn.setMoving(false);
            });
            shakeTransition.setCycleCount(2);
            shakeTransition.playFromStart();

            pawn.setMoving(true);
        }
    }

    /**
     * Retire le pion des éléments graphiques.
     *
     * @param pawn le pion à retirer.
     */
    public void removePawn(Pawn pawn) {
        logger.debug("Suppression du pion {}", pawn);
        group.getChildren().remove(pawn);
    }

    /**
     * Rafraîchit l'interface graphique, et met à jour les textes de statut et de scores.
     */
    public void refreshUI() {
        logger.trace("Rafraîchissement des statuts");

        Player player = this.game.getCurrentPlayer();
        this.statusText.setText("Joueur " + player.formatDisplay(this.game.isAI(player)));

        FillTransition fillTransition = new FillTransition(Duration.seconds(0.25), this.statusText);
        fillTransition.setToValue(player.getColor());
        fillTransition.playFromStart();

        this.scoreP1Text.setText(String.valueOf(this.game.getScore(Player.P1)));
        this.scoreP2Text.setText(String.valueOf(this.game.getScore(Player.P2)));

        this.scoreP1TotalText.setText(String.valueOf(this.totalScores.getOrDefault(Player.P1, 0)));
        this.scoreP2TotalText.setText(String.valueOf(this.totalScores.getOrDefault(Player.P2, 0)));
    }

    /**
     * Affiche une popup de fin de partie.
     * Une partie peut être relancée par cette popup.
     *
     * @param winner le joueur ayant gagné la partie.
     */
    public void endPopup(Player winner) {
        this.totalScores.merge(Player.P1, this.game.getScore(Player.P1), Integer::sum);
        this.totalScores.merge(Player.P2, this.game.getScore(Player.P2), Integer::sum);
        this.refreshUI();

        logger.info("Fin de la partie, le joueur {} gagné et a gagné {} points ({} total), le joueur {} a perdu et a gagné {} points ({} total)", Player.P1, this.game.getScore(Player.P1), totalScores.get(Player.P1), Player.P2, this.game.getScore(Player.P2), totalScores.get(Player.P2));

        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous relancer une partie ?");
        alert.setTitle("Fin de partie");
        alert.setHeaderText("Le joueur " + winner.formatDisplay(this.game.isAI(winner)) + " a gagn\u00e9 et cumule " + this.totalScores.get(winner) + " points !\nLe perdant poss\u00e8de maintenant " + this.totalScores.get(winner.next()) + " points.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(this.stage);

        alert.setOnHidden(e -> {
            ButtonType result = alert.getResult();

            if (result == ButtonType.OK) {
                this.initialize();
                this.startPopup();
            } else {
                logger.info("Fin du jeu");
                System.exit(0);
            }
        });

        alert.show();
    }

    /**
     * Affiche une popup en début de partie.
     * La popup définit les IA qui joueront dans cette partie.
     */
    public void startPopup() {
        logger.info("Affichage de la popup de début");

        ButtonType buttonType1v1 = new ButtonType("1 vs 1");
        ButtonType buttonType1vIA = new ButtonType("1 vs IA");
        ButtonType buttonTypeIAvIA = new ButtonType("IA vs IA");
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Choisissez votre mode de jeu", buttonType1v1, buttonType1vIA, buttonTypeIAvIA, buttonTypeCancel);
        alert.setTitle("D\u00e9but de partie");
        alert.setHeaderText("Mode de jeu");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(this.stage);

        alert.setOnHidden(e -> {
            ButtonType result = alert.getResult();

            if (result == buttonTypeCancel) {
                logger.info("Fin du jeu");
                System.exit(0);
                return;
            }

            if (result == buttonType1vIA) {
                this.game.setAI(Player.P2);
            } else if (result == buttonTypeIAvIA) {
                this.game.setAI(Player.P1);
                this.game.setAI(Player.P2);
            }

            this.game.nextTurn();
        });

        alert.show();
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        primaryStage.setTitle("Surakarta");

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));
        borderPane.setCenter(this.statusText);

        //Scores de gauche, P1
        VBox leftScores = new VBox(this.scoreP1Text, this.scoreP1TotalText);
        leftScores.setAlignment(Pos.CENTER_LEFT);
        borderPane.setLeft(leftScores);

        //Scores de droite, P2
        VBox rightScores = new VBox(this.scoreP2Text, this.scoreP2TotalText);
        rightScores.setAlignment(Pos.CENTER_RIGHT);
        borderPane.setRight(rightScores);

        primaryStage.setScene(new Scene(new VBox(borderPane, new StackPane(this.group)), 768, 800));
        primaryStage.setResizable(false);
        primaryStage.show();

        this.startPopup();
    }

    public static Logger getLogger() {
        return logger;
    }
}