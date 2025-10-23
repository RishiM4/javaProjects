package chess;

import java.util.Comparator;

public class MoveComparator implements Comparator<chess.BoardV3.Move>{

    @Override
    public int compare(chess.BoardV3.Move o1, chess.BoardV3.Move o2) {
        //less than negative, equal 0, greater than positive
        if (o1.isCapture && !o2.isCapture) {
            return -1;
        }
        else if (o2.isCapture && !o1.isCapture) {
            return 1;
        }
        else if (o1.isCapture && o2.isCapture) {
            
        }
        return 0;
    }

}
