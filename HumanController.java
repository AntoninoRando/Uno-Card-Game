public class HumanController extends Controller {
    public HumanController(Player bringer) {
        this.bringer = bringer;
    }

    public String getInput() {
        return "";
    }

    public void playCard(int index) {
        Card card = bringer.getCard(index);

        if (!card.isPlayable()) {
            System.out.println("Can't play that card!");
            return;
        }
        
        GameManager.setTerrainCard(card);
        bringer.removeCard(index);
    }
}
