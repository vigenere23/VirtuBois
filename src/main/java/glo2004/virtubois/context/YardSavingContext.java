package glo2004.virtubois.context;

public class YardSavingContext {

    private static final SavingContext savingContext = new SavingContext();

    public static SavingContext getInstance() {
        return savingContext;
    }
}
