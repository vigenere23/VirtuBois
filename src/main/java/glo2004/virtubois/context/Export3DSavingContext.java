package glo2004.virtubois.context;

public class Export3DSavingContext {
    private static final SavingContext savingContext = new SavingContext();

    public static SavingContext getInstance() {
        return savingContext;
    }
}
