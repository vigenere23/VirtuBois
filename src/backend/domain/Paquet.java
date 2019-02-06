import java.sql.Date;
import java.sql.Time;

public class Paquet
{
    private double height;
    private double width;
    private double length;
    private Date date;
    private Time time;
    private String type;
    private String essence;
    private int codebarre;


    public Paquet(double p_height, double p_width, double p_length, Date p_date, Time p_time, String p_type, String
                  p_essence, int p_codebarre)
    {
        height = p_height;
        width = p_width;
        length = p_length;
        date = p_date;
        time = p_time;
        type = p_type;
        essence = p_essence;
        codebarre = p_codebarre;
    }

    public int getCodebarre()
    {
        return codebarre;
    }

    public String getEssence()
    {
        return essence;
    }

    public Date getDate()
    {
        return date;
    }

    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public double getLength()
    {
        return length;
    }

    public void setLength(double length)
    {
        this.length = length;
    }

    public double getWidth()
    {
        return width;
    }

    public void setWidth(double width)
    {
        this.width = width;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Time getTime()
    {
        return time;
    }
}
