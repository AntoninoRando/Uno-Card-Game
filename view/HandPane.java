package view;

import java.util.Collection;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HandPane extends HBox {
    // private final HashMap<Integer, int[]> cardPositions = (HashMap<Integer, int[]>) Stream.of(new Object[][] {
    //         { 1, new int[] { 0 } },
    //         { 2, new int[] { -2, 2 } },
    //         { 3, new int[] { -2, 0, 2 } },
    //         { 4, new int[] { -4, -2, 2, -2 } },
    //         { 5, new int[] { -4, -2, 0, 2, 4 } },
    //         { 6, new int[] { -6, -4, -2, 0, 2, 4, 6 } },
    //         { 7, new int[] { -6, -4, -2, 0, 2, 4, 6 } }
    // }).collect(Collectors.toMap(p -> (int) p[0], p -> (int[]) p[1]));

    public HandPane() {
        getStyleClass().add("hand");
    }

    public void addCard(ImageView card) {
        getChildren().add(card);
        adjustCards();
    }

    public void addCards(Collection<? extends ImageView> cards) {
        getChildren().addAll(cards);
        adjustCards();
    }

    private void adjustCards() {
        // int n = getChildren().size();
        // int[] rotate =  cardPositions.get(n);

        // for (int i = 0; i < n; i++) {
        
        // }
        // int i = 0;
        // while (i < player.getHand().size()) {
        //     Node card = new CardContainer(player.getHand().get(i)).getNode();
        //     card.setRotate(rotate[i]);
        //     card.setTranslateY(Integer.max(-rotate[i], rotate[i])*5);
        //     card.setTranslateX(-i*30);
        //     root.getChildren().add(card);
        //     i++;
        // }
    }
}
