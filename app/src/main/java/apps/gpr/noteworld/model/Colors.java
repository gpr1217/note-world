package apps.gpr.noteworld.model;

public class Colors {

    private String color;
    private int colorResId;

    public Colors(String color, int colorResId) {
        this.color = color;
        this.colorResId = colorResId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getColorResId() {
        return colorResId;
    }

    public void setColorResId(int colorResId) {
        this.colorResId = colorResId;
    }
}
