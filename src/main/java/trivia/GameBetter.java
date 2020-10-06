package trivia;

import java.util.*;

class Player {
    private final String name;
    private int place;
    private int purse;
    private boolean inPenaltyBox = false;

    Player(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public int place() {
        return place;
    }

    public void move(int roll) {
        place += roll;
        if (place > 11)
            place -= 12;
    }

    public int purse() {
        return purse;
    }

    void addGold(int add) {
        purse += add;
    }

    public boolean inPenaltyBox() {
        return inPenaltyBox;
    }

    public void sendToPenaltyBox() {
        inPenaltyBox = true;
    }
}

public class GameBetter implements IGame {

    ArrayList<Player> players = new ArrayList<>();

    List<String> popQuestions = new ArrayList<>();
    List<String> scienceQuestions = new ArrayList<>();
    List<String> sportsQuestions = new ArrayList<>();
    List<String> rockQuestions = new ArrayList<>();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public GameBetter() {
        for (int i = 0; i < 50; i++) {
            popQuestions.add("Pop Question " + i);
            scienceQuestions.add(("Science Question " + i));
            sportsQuestions.add(("Sports Question " + i));
            rockQuestions.add(createRockQuestion(i));
        }
    }

    public String createRockQuestion(int index) {
        return "Rock Question " + index;
    }

    public boolean add(String playerName) {
        players.add(new Player(playerName));
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public void roll(int roll) {
        System.out.println(currentPlayer().name() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (currentPlayer().inPenaltyBox()) {
            takePenaltyTurn(roll);
        } else {
            takeTurn(roll);
        }

    }

    private void takePenaltyTurn(int roll) {
        if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;
            System.out.println(currentPlayer().name() + " is getting out of the penalty box");
            takeTurn(roll);
        } else {
            System.out.println(currentPlayer().name() + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
        }
    }

    private void takeTurn(int roll) {
        currentPlayer().move(roll);

        System.out.println(currentPlayer().name()
                + "'s new location is "
                + currentPlayer().place());
        System.out.println("The category is " + currentCategory());
        askQuestion();
    }

    private Player currentPlayer() {
        return players.get(currentPlayer);
    }

    private void askQuestion() {
        System.out.println(fetchNextQuestion());
    }

    private String fetchNextQuestion() {
        return switch (currentCategory()) {
            case "Pop" -> popQuestions.remove(0);
            case "Science" -> scienceQuestions.remove(0);
            case "Sports" -> sportsQuestions.remove(0);
            case "Rock" -> rockQuestions.remove(0);
            default -> throw new IllegalArgumentException();
        };
    }


    private String currentCategory() {
        if (currentPlayer().place() == 0) return "Pop";
        if (currentPlayer().place() == 4) return "Pop";
        if (currentPlayer().place() == 8) return "Pop";
        if (currentPlayer().place() == 1) return "Science";
        if (currentPlayer().place() == 5) return "Science";
        if (currentPlayer().place() == 9) return "Science";
        if (currentPlayer().place() == 2) return "Sports";
        if (currentPlayer().place() == 6) return "Sports";
        if (currentPlayer().place() == 10) return "Sports";
        return "Rock";
    }

    public boolean wasCorrectlyAnswered() {
        if (currentPlayer().inPenaltyBox()) {
            if (isGettingOutOfPenaltyBox) {
                rewardPlayer();

                boolean winner = didPlayerWin();
                changeToNextPlayer();

                return winner;
            } else {
                changeToNextPlayer();
                return true;
            }
        } else {
            rewardPlayer();

            boolean winner = didPlayerWin();
            changeToNextPlayer();

            return winner;
        }
    }

    private void changeToNextPlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    private void rewardPlayer() {
        System.out.println("Answer was correct!!!!");
        currentPlayer().addGold(1);
        System.out.println(currentPlayer().name()
                + " now has "
                + currentPlayer().purse()
                + " Gold Coins.");
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().name() + " was sent to the penalty box");
        currentPlayer().sendToPenaltyBox();
        changeToNextPlayer();
        return true;
    }


    private boolean didPlayerWin() {
        return !(currentPlayer().purse() == 6);
    }
}
