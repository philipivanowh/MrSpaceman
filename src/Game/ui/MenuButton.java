package Game.ui;

import Game.GameState;
import Game.Input;
import Game.utils.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class MenuButton {
    

    private int x;
    private int y;
    private int width;
    private int height;
    private Color idleColor = Color.white;
    private Color hoverColor = Color.gray;
    private Color clickColor = Color.lightGray;
    private String buttonString = "Button";
    private GameState gameState;

    public MenuButton(int x, int y, int width, int height, Color idleColor, Color hoverColor, Color clickColor, String buttonString,GameState gameState) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.idleColor = idleColor;
        this.hoverColor = hoverColor;
        this.clickColor = clickColor;   
        this.gameState = gameState;
        this.buttonString = buttonString;
        
    }

     public MenuButton(int x, int y, int width, int height, String buttonString ,GameState gameState) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.gameState = gameState;
        this.buttonString = buttonString;
    }

    public void handleInput(){
        //When button is clicked
        if(Input.mouseIsClicked() && mouseInBounds(Input.getMouseRelativeToScreen())) {
            GameState.state = this.gameState;
        } 

    }

    public void render(Graphics g){

        
        if(mouseInBounds(Input.getMouseRelativeToScreen())) {
            g.setColor(hoverColor);
            g.fillRect(x-(int)(width*.05/2), y-(int)(height*.05/2), (int)(width+width*.05), (int)(height+height*.05));
        } else {
            g.setColor(idleColor);
            g.drawRect(x, y, width, height);
        }


        Font fn1 = new Font("Arial", Font.BOLD, 20);
        g.setFont(fn1);
        g.drawString(buttonString, x + width / 2 - g.getFontMetrics().stringWidth(buttonString) / 2, y + height / 2 + g.getFontMetrics().getHeight() / 4);
    }

    private boolean mouseInBounds(Vector2D mousePos) {
        return mousePos.x >= x && mousePos.x <= x + width && mousePos.y >= y && mousePos.y <= y + height;
    }

}
