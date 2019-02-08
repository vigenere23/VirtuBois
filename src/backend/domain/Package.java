import javafx.util.Pair;

import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.atomic.AtomicInteger;

public class Package
{
    private double height;
    private double width;
    private double length;
    private Date date;
    private Time time;
    private Pair<String, String> type;
    private int barcode;
    private static final AtomicInteger count = new AtomicInteger(000000000000);


    public Package(double p_height, double p_width, double p_length, Date p_date, Time p_time,
                   Pair<String,String> p_type)
    {
        height = p_height;
        width = p_width;
        length = p_length;
        date = p_date;
        time = p_time;
        type = p_type;
        barcode = count.incrementAndGet();
    }

    public int getBarcode()
    {
        return barcode;
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

    public Pair<String,String> getType()
    {
        return type;
    }

    public void setType(Pair<String,String> type)
    {
        this.type = type;
    }

    public Time getTime()
    {
        return time;
    }
}
