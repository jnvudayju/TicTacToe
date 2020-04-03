import java.awt.event.*;
import java.awt.*;

public class TicTacToeGUI extends Frame implements ActionListener {
    private static final long serialVersionUID = 1L; // Default Serial ID
    public Graphics g;
    private int PUZZLE_SIZE = 3;
    private static boolean HIDE_MENU = false;
    private static boolean HIDE_PLAYER = false;
    private int GRID_SIZE = 100;
    private boolean playNextFlag = false;
    private boolean playPauseFlag = false;
    private boolean playPrevFlag = false;
    Button buttonBack;

    Button buttonPrev;
    Button buttonNext;
    Button buttonPlayPause;

    Button buttonHumanVsHuman;
    Button buttonHumanVsComputer;
    Button buttonComputerVsComputer;

    TicTacToeGUI() {
        setVisible(true);
        setLayout(null);
        setSize(570, 700);
        setLocation(400, 50);
        setFont(new Font("Forte", Font.ITALIC, 35));
        setBackground(Color.WHITE);

        buttonBack = new Button("Back");
        buttonBack.setBounds(420, 600, 100, 60);
        add(buttonBack);
        buttonBack.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void menuButton(boolean show) {
        buttonHumanVsHuman = new Button("HUMAN VS HUMAN");
        buttonHumanVsComputer = new Button("HUMAN VS COMPUTER");
        buttonComputerVsComputer = new Button("COMPUTER VS COMPUTER");
        buttonHumanVsHuman.setBounds(90, 170, 380, 60);
        buttonHumanVsComputer.setBounds(70, 270, 440, 60);
        buttonComputerVsComputer.setBounds(40, 370, 500, 60);
        add(buttonHumanVsHuman);
        add(buttonHumanVsComputer);
        add(buttonComputerVsComputer);
        buttonHumanVsHuman.addActionListener(this);
        buttonHumanVsComputer.addActionListener(this);
        buttonComputerVsComputer.addActionListener(this);

    }

    public void playerButtons(boolean show) {
        buttonPrev = new Button("<<");
        buttonNext = new Button(">>");
        buttonPlayPause = new Button("Play");
        buttonPrev.setBounds(100, 540, 100, 60);
        buttonPlayPause.setBounds(220, 540, 100, 60);
        buttonNext.setBounds(340, 540, 100, 60);
        add(buttonPrev);
        add(buttonPlayPause);
        add(buttonNext);
        buttonPrev.addActionListener(this);
        buttonPlayPause.addActionListener(this);
        buttonNext.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonHumanVsHuman) {
            HIDE_MENU = true;
            buttonHumanVsHuman.removeNotify();
            buttonHumanVsComputer.removeNotify();
            buttonComputerVsComputer.removeNotify();
        }
        if (e.getSource() == buttonHumanVsComputer) {
            HIDE_MENU = true;
            buttonHumanVsHuman.removeNotify();
            buttonHumanVsComputer.removeNotify();
            buttonComputerVsComputer.removeNotify();
        }
        if (e.getSource() == buttonComputerVsComputer) {
            HIDE_MENU = true;
            buttonHumanVsHuman.removeNotify();
            buttonHumanVsComputer.removeNotify();
            buttonComputerVsComputer.removeNotify();
        }
    }

    public static void main(String[] args) {
        TicTacToeGUI gui = new TicTacToeGUI();
        gui.menuButton(true);
    }

}