package src;

public class Bit {
    private int state = 0;
    public Bit(int s) {
        this.state = s;
    }
    public void setState(int s) {
        this.state = s;
    }
    public int getState() {
        return this.state;
    }
    @Override
    public String toString() {
        return state + "";
    }
    public int switchState() {
        if (state == 0) {
            state = 1;
        }
        else {
            state = 0;
        }
        return state;
    }
}
