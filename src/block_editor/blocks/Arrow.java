public class Arrow {
    private double startX, startY, endX, endY;
    private void redraw() {

    }
    public Arrow(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
    public void setStartX(double X) {
        this.startX = X;
        redraw();
    }
    public void setStartY(double Y) {
        this.startY = Y;
        redraw();
    }
    public void setEndX(double X) {
        this.endX = X;
        redraw();
    }
    public void setEndY(double Y) {
        this.endY = Y;
        redraw();
    }
    public double getStartX() {
        return startX;
    }
    public double getStartY() {
        return startY;
    }
    public double getEndX() {
        return endX;
    }
    public double getEndY() {
        return endY;
    }
}