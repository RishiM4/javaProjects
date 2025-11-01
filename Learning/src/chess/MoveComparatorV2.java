package src.chess;

import java.util.Comparator;

public class MoveComparatorV2 implements Comparator<src.chess.BoardV3.Move>{

    @Override
    public int compare(src.chess.BoardV3.Move o1, src.chess.BoardV3.Move o2) {
        if (o2.weight > o1.weight) {
            return 1;
        }
        else if (o1.weight > o2.weight) {
            return -1;
        }
        //less than negative, equal 0, greater than positive
        if (o1.isCapture && !o2.isCapture) {
            return -1;
        }
        else if (o2.isCapture && !o1.isCapture) {
            
            return 1;
        }
        else if (o1.isCapture && o2.isCapture) {
            //add logic here
        }
        return 0;
    }

}
