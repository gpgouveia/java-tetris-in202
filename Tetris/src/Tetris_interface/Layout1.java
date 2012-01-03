/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tetris_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;
import tetris.Position;

public class Layout1 extends JFrame {

    private JPanel base, topPanel, initialPanel, selectionPanel, optionsPanel, somPanel, game1pPanel, game2pPanel;
    private JLabel[] currentPiece, nextPiece; //array with the position of the 4 boxes of the 2 pieces
    private JLabel[][] screen;// Sreen 10 x 13 with the pointer for all the lavels in use
    //constants
    private int pieceSize = 20, screenWidth = 10, screenHeight = 13, levelScore = 20, levelScoreAnt = 0, scoreFactor = 50, levelNumber = 0;
    //options components
    private JTextField leftKey, rightKey, downKey, rotateKey, playerName;
    private JCheckBox mouseBox;
    //sound components
    private JComboBox themeBox;
    private JSlider volumeSlider;
    //1 players components
    private JPanel gameScreen1pPanel, gameNext1pPanel;
    private JProgressBar scoreBar;
    private JTextField score, timePassed;
    private JLabel level;
    //listenets
    static ActionListener gameViewReady = null;

    public Layout1() {

        make_top();
        make_initial();
        make_selection();
        make_options();
        make_som();
        make_game1p();
        make_game2p();
        make_base();
        make_UI();
        screen = new JLabel[screenWidth][screenHeight];
        currentPiece = new JLabel[4];
        nextPiece = new JLabel[4];
    }
    //cria os panels usados

    private void make_top() {

        topPanel = new JPanel(new AbsoluteLayout());

        JToolBar toolbar = new JToolBar();
        try {
            ImageIcon newgameIcon = new ImageIcon(getClass().getResource("newgame.png"));
            ImageIcon configIcon = new ImageIcon(getClass().getResource("config.png"));
            ImageIcon somIcon = new ImageIcon(getClass().getResource("som.png"));
            ImageIcon closeIcon = new ImageIcon(getClass().getResource("close.png"));

            JButton newgame = new JButton("New Game", newgameIcon);
            JButton config = new JButton("Configuration", configIcon);
            JButton som = new JButton("Sound", somIcon);
            JButton close = new JButton("Exit", closeIcon);

            newgame.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    func_newgame();
                }
            });

            config.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    func_config();
                }
            });

            som.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    func_som();
                }
            });

            close.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    func_exit();
                }
            });

            toolbar.add(newgame, new AbsoluteConstraints(20, 20));
            toolbar.add(config, new AbsoluteConstraints(80, 20));
            toolbar.add(som, new AbsoluteConstraints(140, 20));
            toolbar.add(close, new AbsoluteConstraints(200, 20));

            topPanel.add(toolbar, new AbsoluteConstraints(0, 0));
        } catch (Exception e) {
            System.out.println("Problem in top icons load");
            e.printStackTrace();
        }
    }

    private void make_initial() {
        initialPanel = new JPanel(new AbsoluteLayout());
        JLabel menuImage = new JLabel();
        try {
            menuImage.setIcon(new ImageIcon(getClass().getResource("/Tetris_interface/menuimage.png")));

            initialPanel.add(menuImage, new AbsoluteConstraints(0, 50, -1, -1));

            JLabel initialMenu = new JLabel("TETRIS");
            initialMenu.setFont(new java.awt.Font("Neuropol", 0, 48));

            initialPanel.add(initialMenu, new AbsoluteConstraints(70, 5, -1, -1));

            JLabel credits1 = new JLabel("By: Gustavo PACIANOTTO G.");
            credits1.setFont(new Font("Segoe Print", 0, 13));
            JLabel credits2 = new JLabel("     Adriano Tacilo RIBEIRO");
            credits2.setFont(new Font("Segoe Print", 0, 13));
            JLabel credits3 = new JLabel("     Ademir Felipe TELES");
            credits3.setFont(new Font("Segoe Print", 0, 13));

            initialPanel.add(credits1, new AbsoluteConstraints(127, 180, -1, -1));
            initialPanel.add(credits2, new AbsoluteConstraints(127, 202, -1, -1));
            initialPanel.add(credits3, new AbsoluteConstraints(127, 224, -1, -1));
        } catch (Exception e) {
            System.out.println("Problem in top icons load");
            e.printStackTrace();
        }

    }

    private void make_selection() {
        selectionPanel = new JPanel(new AbsoluteLayout());

        JLabel selectionMenu = new JLabel("Game Mode");
        selectionMenu.setFont(new java.awt.Font("Neuropol", 0, 24));

        JSeparator separator = new JSeparator();

        JButton player1 = new JButton("1 Player");
        player1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                func_1player();
            }
        });

        JButton player2 = new JButton("2 Players");
        player2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                func_2players();
            }
        });

        selectionPanel.add(selectionMenu, new AbsoluteConstraints(80, 20));
        selectionPanel.add(separator, new AbsoluteConstraints(15, 50, 290, 11));

        selectionPanel.add(player1, new AbsoluteConstraints(2, 55, 320, 130));
        selectionPanel.add(player2, new AbsoluteConstraints(2, 182, 320, 130));

    }

    private void make_options() {

        optionsPanel = new JPanel(new AbsoluteLayout());

        JLabel optionTitle = new JLabel("Options");
        optionTitle.setFont(new java.awt.Font("Neuropol", 0, 24));

        JSeparator separator = new JSeparator();

        optionsPanel.add(optionTitle, new AbsoluteConstraints(110, 20, -1, -1));
        optionsPanel.add(separator, new AbsoluteConstraints(15, 50, 290, 11));

        //controls panel
        JPanel controlsPanel = new JPanel(new AbsoluteLayout());
        controlsPanel.setBorder(BorderFactory.createTitledBorder(null, "Controls", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Sybil Green", 0, 14)));

        JLabel moveLeft = new JLabel("Move Left");
        moveLeft.setFont(new Font("Segoe Print", 0, 12));

        leftKey = new JTextField("Q");
        leftKey.setFont(new Font("Segoe Print", 0, 12));
        leftKey.setHorizontalAlignment(JTextField.CENTER);
        leftKey.setBackground(new Color(controlsPanel.getBackground().getRed(), controlsPanel.getBackground().getGreen(), controlsPanel.getBackground().getBlue()));

        JLabel moveRight = new JLabel("Move Right");
        moveRight.setFont(new Font("Segoe Print", 0, 12));

        rightKey = new JTextField("D");
        rightKey.setFont(new Font("Segoe Print", 0, 12));
        rightKey.setHorizontalAlignment(JTextField.CENTER);
        rightKey.setBackground(new Color(controlsPanel.getBackground().getRed(), controlsPanel.getBackground().getGreen(), controlsPanel.getBackground().getBlue()));

        JLabel moveDown = new JLabel("Move Down");
        moveDown.setFont(new Font("Segoe Print", 0, 12));

        downKey = new JTextField("S");
        downKey.setFont(new Font("Segoe Print", 0, 12));
        downKey.setHorizontalAlignment(JTextField.CENTER);
        downKey.setBackground(new Color(controlsPanel.getBackground().getRed(), controlsPanel.getBackground().getGreen(), controlsPanel.getBackground().getBlue()));

        JLabel rotate = new JLabel("Rotate");
        rotate.setFont(new Font("Segoe Print", 0, 12));

        rotateKey = new JTextField("Z");
        rotateKey.setFont(new Font("Segoe Print", 0, 12));
        rotateKey.setHorizontalAlignment(JTextField.CENTER);
        rotateKey.setBackground(new Color(controlsPanel.getBackground().getRed(), controlsPanel.getBackground().getGreen(), controlsPanel.getBackground().getBlue()));


        mouseBox = new JCheckBox("Use mouse");
        mouseBox.setFont(new Font("Segoe Print", 0, 12));

        controlsPanel.add(moveLeft, new AbsoluteConstraints(30, 30, -1, -1));
        controlsPanel.add(leftKey, new AbsoluteConstraints(115, 32, 25, 22));

        controlsPanel.add(moveRight, new AbsoluteConstraints(160, 30, -1, -1));
        controlsPanel.add(rightKey, new AbsoluteConstraints(240, 32, 25, 22));

        controlsPanel.add(moveDown, new AbsoluteConstraints(30, 60, -1, -1));
        controlsPanel.add(downKey, new AbsoluteConstraints(115, 63, 25, 22));

        controlsPanel.add(rotate, new AbsoluteConstraints(160, 60, -1, -1));
        controlsPanel.add(rotateKey, new AbsoluteConstraints(240, 63, 25, 22));

        controlsPanel.add(mouseBox, new AbsoluteConstraints(30, 90, -1, -1));

        optionsPanel.add(controlsPanel, new AbsoluteConstraints(20, 60, 290, 120));

        //begin of the player name panel
        JPanel playerPanel = new JPanel(new AbsoluteLayout());
        playerPanel.setBorder(BorderFactory.createTitledBorder(null, "Player Name", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Sybil Green", 0, 14)));

        playerName = new JTextField("ENSTA Project");
        playerName.setFont(new Font("Segoe Print", 0, 12));
        playerName.setHorizontalAlignment(JTextField.CENTER);
        playerName.setBorder(null);
        playerName.setBackground(new Color(playerPanel.getBackground().getRed(), playerPanel.getBackground().getGreen(), playerPanel.getBackground().getBlue()));



        playerPanel.add(playerName, new AbsoluteConstraints(12, 20, 260, 30));

        optionsPanel.add(playerPanel, new AbsoluteConstraints(20, 190, 290, 60));

        //bottons

        JButton apply = new JButton("Apply");
        apply.setFont(new Font("Planet Benson 2", 0, 14));
        apply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                func_changeConfig();
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.setFont(new Font("Planet Benson 2", 0, 14));
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                func_initial();
            }
        });
        optionsPanel.add(apply, new AbsoluteConstraints(60, 250, -1, -1));
        optionsPanel.add(cancel, new AbsoluteConstraints(180, 250, -1, -1));

    }

    private void make_som() {
        somPanel = new JPanel(new AbsoluteLayout());
        JLabel optionTitle = new JLabel("Sound");
        optionTitle.setFont(new java.awt.Font("Neuropol", 0, 24));

        JSeparator separator = new JSeparator();

        somPanel.add(optionTitle, new AbsoluteConstraints(110, 20, -1, -1));
        somPanel.add(separator, new AbsoluteConstraints(15, 50, 290, 11));
        //options of sounds

        JLabel themeTitle = new JLabel("Theme");
        themeTitle.setFont(new Font("Segoe Print", 0, 14));

        themeBox = new JComboBox();
        themeBox.setFont(new Font("Segoe Print", 0, 12));
        themeBox.setModel(new DefaultComboBoxModel(new String[]{"Classic", "MarioBros", "PacMan", "Bomberman"}));

        JLabel volumeTitle = new JLabel("Volume");
        volumeTitle.setFont(new Font("Segoe Print", 0, 14));

        volumeSlider = new JSlider();

        somPanel.add(themeTitle, new AbsoluteConstraints(30, 90, -1, -1));
        somPanel.add(themeBox, new AbsoluteConstraints(110, 85, -1, -1));
        somPanel.add(volumeTitle, new AbsoluteConstraints(30, 140, -1, -1));
        somPanel.add(volumeSlider, new AbsoluteConstraints(110, 140, -1, -1));

        //buttons
        JButton apply = new JButton("Apply");
        apply.setFont(new Font("Planet Benson 2", 0, 14));
        apply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                func_changeConfig();
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.setFont(new Font("Planet Benson 2", 0, 14));
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                func_initial();
            }
        });
        somPanel.add(apply, new AbsoluteConstraints(60, 250, -1, -1));
        somPanel.add(cancel, new AbsoluteConstraints(180, 250, -1, -1));

    }

    private void make_game1p() {
        game1pPanel = new JPanel(new AbsoluteLayout());

        JLabel game1pTitle = new JLabel("1 Player");
        game1pTitle.setFont(new java.awt.Font("Neuropol", 0, 24));

        JSeparator separator = new JSeparator();

        game1pPanel.add(game1pTitle, new AbsoluteConstraints(110, 20, -1, -1));
        game1pPanel.add(separator, new AbsoluteConstraints(5, 50, 330, 10));

        //panels

        gameScreen1pPanel = new JPanel(new AbsoluteLayout());
        gameScreen1pPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        gameScreen1pPanel.setFocusable(true);
        gameNext1pPanel = new JPanel(new AbsoluteLayout());
        gameNext1pPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        game1pPanel.add(gameScreen1pPanel, new AbsoluteConstraints(0, 50, 200, 260));
        game1pPanel.add(gameNext1pPanel, new AbsoluteConstraints(200, 50, 90, 60));
        //game status
        scoreBar = new JProgressBar();
        scoreBar.setValue(10);
        game1pPanel.add(scoreBar, new AbsoluteConstraints(205, 210, 120, -1));

        level = new JLabel("Level: 0");
        level.setFont(new java.awt.Font("Neuropol", 0, 14));
        game1pPanel.add(level, new AbsoluteConstraints(235, 190, -1, -1));

        score = new JTextField();
        score.setText("0");
        score.setFont(new Font("Neuropol", 0, 14)); // NOI18N
        score.setHorizontalAlignment(JTextField.CENTER);
        score.setEditable(false);
        game1pPanel.add(score, new AbsoluteConstraints(205, 150, 60, 25));

        JLabel scoreLabel = new JLabel("SCORE");
        scoreLabel.setFont(new Font("Neuropol", 0, 14));
        game1pPanel.add(scoreLabel, new AbsoluteConstraints(210, 130, -1, -1));

        JLabel timeLabel = new JLabel("TIME");
        timeLabel.setFont(new Font("Neuropol", 0, 14));
        game1pPanel.add(timeLabel, new AbsoluteConstraints(275, 130, -1, -1));

        timePassed = new JTextField("01:02");
        timePassed.setBackground(new java.awt.Color(0, 0, 0));
        timePassed.setFont(new java.awt.Font("Neuropol", 0, 14)); // NOI18N
        timePassed.setForeground(new java.awt.Color(51, 255, 0));
        timePassed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        timePassed.setEditable(false);
        game1pPanel.add(timePassed, new AbsoluteConstraints(265, 150, 60, 25));

        //buttons
        JButton apply = new JButton("Pause");
        apply.setFont(new Font("Planet Benson 2", 0, 14));
        apply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                func_pause();
            }
        });
        JButton cancel = new JButton("Restart");
        cancel.setFont(new Font("Planet Benson 2", 0, 14));
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                func_restart();
            }
        });
        game1pPanel.add(apply, new AbsoluteConstraints(215, 230, 100, 30));
        game1pPanel.add(cancel, new AbsoluteConstraints(215, 260, 100, -1));

    }

    private void make_game2p() {
        game2pPanel = new JPanel(new AbsoluteLayout());
        JLabel menu = new JLabel("ainda naum entendi como vai funcionar esta janela, sorry");
        game2pPanel.add(menu, new AbsoluteConstraints(0, 0));

    }

    private void make_base() {

        base = new JPanel(new AbsoluteLayout());
        base.add(topPanel, new AbsoluteConstraints(0, 0));
        base.add(initialPanel, new AbsoluteConstraints(0, 20, 330, 340));
        base.add(selectionPanel, new AbsoluteConstraints(0, 10, 330, 320));
        selectionPanel.setVisible(false);
        base.add(optionsPanel, new AbsoluteConstraints(0, 10, 330, 320));
        optionsPanel.setVisible(false);
        base.add(somPanel, new AbsoluteConstraints(0, 10, 330, 320));
        somPanel.setVisible(false);
        base.add(game1pPanel, new AbsoluteConstraints(0, 10, 330, 320));
        game1pPanel.setVisible(false);
        base.add(game2pPanel, new AbsoluteConstraints(0, 10, 330, 320));
        game2pPanel.setVisible(false);

    }
    //cria a interface do JFrame

    private void make_UI() {
        getContentPane().setLayout(new AbsoluteLayout());
        add(base, new AbsoluteConstraints(0, 0));
        pack();
        setTitle("Tetris");
        setSize(330, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);


    }
    //functions by Events

    private void func_initial() {
        initialPanel.setVisible(true);
        selectionPanel.setVisible(false);
        optionsPanel.setVisible(false);
        somPanel.setVisible(false);
        game1pPanel.setVisible(false);
        game2pPanel.setVisible(false);
    }

    private void func_newgame() {
        initialPanel.setVisible(false);
        selectionPanel.setVisible(true);
        optionsPanel.setVisible(false);
        somPanel.setVisible(false);
        game1pPanel.setVisible(false);
        game2pPanel.setVisible(false);
    }

    private void func_config() {
        initialPanel.setVisible(false);
        selectionPanel.setVisible(false);
        optionsPanel.setVisible(true);
        somPanel.setVisible(false);
        game1pPanel.setVisible(false);
        game2pPanel.setVisible(false);
    }

    private void func_som() {
        initialPanel.setVisible(false);
        selectionPanel.setVisible(false);
        optionsPanel.setVisible(false);
        somPanel.setVisible(true);
        game1pPanel.setVisible(false);
        game2pPanel.setVisible(false);
    }

    private void func_1player() {
        initialPanel.setVisible(false);
        selectionPanel.setVisible(false);
        optionsPanel.setVisible(false);
        somPanel.setVisible(false);
        game1pPanel.setVisible(true);
        game2pPanel.setVisible(false);

        if (gameViewReady != null) {
            gameViewReady.actionPerformed(null);
        }

        //so pra demonstrar, retirar depois
        JLabel bluePiece = new JLabel(new ImageIcon(getClass().getResource("/Tetris_interface/Blue.png")));
        JLabel brownPiece = new JLabel(new ImageIcon(getClass().getResource("/Tetris_interface/Brown.png")));
        JLabel cianoPiece = new JLabel(new ImageIcon(getClass().getResource("/Tetris_interface/Ciano.png")));
        JLabel greenPiece = new JLabel(new ImageIcon(getClass().getResource("/Tetris_interface/Green.png")));
        JLabel purplePiece = new JLabel(new ImageIcon(getClass().getResource("/Tetris_interface/Purple.png")));
        JLabel redPiece = new JLabel(new ImageIcon(getClass().getResource("/Tetris_interface/Red.png")));
        JLabel yellowPiece = new JLabel(new ImageIcon(getClass().getResource("/Tetris_interface/Yellow.png")));

        gameScreen1pPanel.add(bluePiece, new AbsoluteConstraints(0 * pieceSize, gameScreen1pPanel.getHeight() - pieceSize, -1, -1));//pieceSize, pieceSize));
        gameScreen1pPanel.add(brownPiece, new AbsoluteConstraints(1 * pieceSize, gameScreen1pPanel.getHeight() - pieceSize, pieceSize, pieceSize));
        gameScreen1pPanel.add(cianoPiece, new AbsoluteConstraints(2 * pieceSize, gameScreen1pPanel.getHeight() - pieceSize, pieceSize, pieceSize));
        gameScreen1pPanel.add(greenPiece, new AbsoluteConstraints(3 * pieceSize, gameScreen1pPanel.getHeight() - pieceSize, pieceSize, pieceSize));
        gameScreen1pPanel.add(purplePiece, new AbsoluteConstraints(4 * pieceSize, gameScreen1pPanel.getHeight() - pieceSize, pieceSize, pieceSize));
        gameScreen1pPanel.add(redPiece, new AbsoluteConstraints(5 * pieceSize, gameScreen1pPanel.getHeight() - pieceSize, pieceSize, pieceSize));
        gameScreen1pPanel.add(yellowPiece, new AbsoluteConstraints(6 * pieceSize, gameScreen1pPanel.getHeight() - pieceSize, pieceSize, pieceSize));

        screen[1][1] = yellowPiece;
        currentPiece[0] = bluePiece;
        currentPiece[1] = brownPiece;
        currentPiece[2] = cianoPiece;
        currentPiece[3] = greenPiece;
        gameScreen1pPanel.add(screen[1][1], new AbsoluteConstraints(7 * pieceSize, gameScreen1pPanel.getHeight() - pieceSize, pieceSize, pieceSize));

    }

    private void func_2players() {
        initialPanel.setVisible(false);
        selectionPanel.setVisible(false);
        optionsPanel.setVisible(false);
        somPanel.setVisible(false);
        game1pPanel.setVisible(false);
        game2pPanel.setVisible(true);
    }

    private void func_exit() {
        System.exit(0);
    }

    private void func_changeConfig() {
        //atualiza hotkeys
        if (mouseBox.isSelected()) {
            //habilita movimento pelo mouse
        } else {
            //desabilita o mouse
        }

        func_initial();
    }

    private void func_pause() {
    }

    private void func_restart() {
        Position[] tester = new Position[4];
        System.out.println("oi");
        try {
            tester[0] = new Position(5, 5);
            tester[1] = new Position(3, 5);
            tester[2] = new Position(4, 5);
            tester[3] = new Position(5, 6);

            setPiecePosition(tester);

            System.out.println("xau");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //publics functions

    public void setPiecePosition(Position[] newPiece) {
        //if the bloc shouldn't keep hide, pass position X or/and Y =-1
        currentPiece[0].setLocation(xPos(newPiece[0].getX()), yPos(newPiece[0].getY()));
        currentPiece[1].setLocation(xPos(newPiece[1].getX()), yPos(newPiece[1].getY()));
        currentPiece[2].setLocation(xPos(newPiece[2].getX()), yPos(newPiece[2].getY()));
        currentPiece[3].setLocation(xPos(newPiece[3].getX()), yPos(newPiece[3].getY()));
        screen[newPiece[0].getX()][newPiece[0].getY()] = currentPiece[0];
        screen[newPiece[1].getX()][newPiece[1].getY()] = currentPiece[1];
        screen[newPiece[2].getX()][newPiece[2].getY()] = currentPiece[2];
        screen[newPiece[3].getX()][newPiece[3].getY()] = currentPiece[3];

    }

    public void newPiece(Position[] newPosCurrentPiece, Position[] newPosNextPiece, String colorPiece) {
        //this function receive the 4 positions of the new piece and her color

        currentPiece = nextPiece;
//put the piece in the game
        if (currentPiece != null) {
            currentPiece[0].setLocation(xPos(newPosCurrentPiece[0].getX()), yPos(newPosCurrentPiece[0].getY()));
            currentPiece[1].setLocation(xPos(newPosCurrentPiece[1].getX()), yPos(newPosCurrentPiece[1].getY()));
            currentPiece[2].setLocation(xPos(newPosCurrentPiece[2].getX()), yPos(newPosCurrentPiece[2].getY()));
            currentPiece[3].setLocation(xPos(newPosCurrentPiece[3].getX()), yPos(newPosCurrentPiece[3].getY()));
            screen[newPosCurrentPiece[0].getX()][newPosCurrentPiece[0].getY()] = currentPiece[0];
            screen[newPosCurrentPiece[1].getX()][newPosCurrentPiece[1].getY()] = currentPiece[1];
            screen[newPosCurrentPiece[2].getX()][newPosCurrentPiece[2].getY()] = currentPiece[2];
            screen[newPosCurrentPiece[3].getX()][newPosCurrentPiece[3].getY()] = currentPiece[3];

            //add one new piece in NextPiece box
            gameNext1pPanel.removeAll();
        }
        try {
            nextPiece[0] = new JLabel(new ImageIcon(getClass().getResource("/Tetris_interface/" + colorPiece + ".png")));
            nextPiece[1] = new JLabel(new ImageIcon(getClass().getResource("/Tetris_interface/" + colorPiece + ".png")));
            nextPiece[2] = new JLabel(new ImageIcon(getClass().getResource("/Tetris_interface/" + colorPiece + ".png")));
            nextPiece[3] = new JLabel(new ImageIcon(getClass().getResource("/Tetris_interface/" + colorPiece + ".png")));
            
            int xBase = gameNext1pPanel.getWidth() / 2;
            int yBase = gameNext1pPanel.getHeight() / 2;

            nextPiece[0].setLocation(xBase + newPosNextPiece[0].getX() * pieceSize, yBase + newPosNextPiece[0].getY());
            nextPiece[1].setLocation(xBase + newPosNextPiece[1].getX() * pieceSize, yBase + newPosNextPiece[1].getY());
            nextPiece[2].setLocation(xBase + newPosNextPiece[2].getX() * pieceSize, yBase + newPosNextPiece[2].getY());
            nextPiece[3].setLocation(xBase + newPosNextPiece[3].getX() * pieceSize, yBase + newPosNextPiece[3].getY());

        } catch (Exception e) {
            System.out.println("Problem in top icons load");
            e.printStackTrace();
        }
    }

    public void eraseLine(int line) {
        //linhas começam do zero
        int i, j;
        for (i = 0; i < screenWidth; i++) {
            screen[i][line].setVisible(false);
            screen[i][line].setEnabled(false);
        }
        for (j = line; j < screenHeight; j++) {
            for (i = 0; i < screenWidth; i++) {
                screen[i][j] = screen[i][j + 1];
                screen[i][j].setLocation(xPos(i), yPos(j));
            }
        }
    }

    public void setScore(int newScore, int newLevel, int scoreMin, int scoreMax) {
        //if change of level, set the new level maximum score and change the label
        score.setText(String.valueOf(newScore));
        level.setText("Level:" + newLevel);
        scoreBar.setValue((newScore - scoreMin) * 100 / (scoreMax - scoreMin));
    }

    public void setKeyListener(KeyListener newListener) {
        gameScreen1pPanel.addKeyListener(newListener);
    }

    public void setMouseListener(MouseListener newListener) {
        gameScreen1pPanel.addMouseListener(newListener);
    }

    public void setMouseMotionListener(MouseMotionListener newListener) {
        gameScreen1pPanel.addMouseMotionListener(newListener);
    }
    //internal use funcition

    private int xPos(int newX) {
        return newX * pieceSize;
    }

    private int yPos(int newY) {
        return gameScreen1pPanel.getHeight() - newY * pieceSize;
    }

    public static void addGameViewReady(ActionListener newGameViewReady) {
        gameViewReady = newGameViewReady;
    }

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Layout1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Layout1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Layout1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Layout1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Layout1 ex = new Layout1();
                ex.setVisible(true);
            }
        });
    }
}