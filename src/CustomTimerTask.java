import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class CustomTimerTask extends TimerTask {
    private CopyOnWriteArrayList<PrintWriter> outputs;
    int i = 0;
    DatabaseOperations databaseOperations;
    public CustomTimerTask(CopyOnWriteArrayList<PrintWriter> outputs) throws SQLException {
        this.outputs = outputs;
        databaseOperations = new DatabaseOperations();
            }
    @Override
    public void run() {
        if (i == outputs.size()) {
            i = 0;
        }
        outputs.get(i).println("drawer");
        try {
            String randomQ = databaseOperations.randomQuestion();
            for (PrintWriter o : outputs) {
                o.println(randomQ);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < outputs.size(); j++) {
            if (j != i) {
                outputs.get(j).println("guesser");
            }
        }
        i++;

    }

}
