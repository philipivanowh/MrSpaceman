package Game;

public enum GameState {
    MENU, PLAYING, GAME_OVER,INSTRUCTION, PAUSED, SETTINGS, CREDITS, QUIT;

    public static GameState state = MENU;
}
