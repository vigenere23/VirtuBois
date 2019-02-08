package backend.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Paquet
{
    private double _height;
    private double _width;
    private double _length;
    private LocalDate _date;
    private LocalTime _time;
    private String _type;
    private String _essence;
    private int _codebarre;


    public Paquet(
            double height, double width, double length,
            LocalDate date, LocalTime time,
            String type, String essence,
            int codebarre)
    {
        _height = height;
        _width = width;
        _length = length;
        _date = date;
        _time = time;
        _type = type;
        _essence = essence;
        _codebarre = codebarre;
    }

    public int getCodebarre()
    {
        return _codebarre;
    }

    public String getEssence()
    {
        return _essence;
    }

    public LocalDate getDate()
    {
        return _date;
    }

    public double getHeight()
    {
        return _height;
    }

    public void setHeight(double height)
    {
        _height = height;
    }

    public double getLength()
    {
        return _length;
    }

    public void setLength(double length)
    {
        _length = length;
    }

    public double getWidth()
    {
        return _width;
    }

    public void setWidth(double width)
    {
        _width = width;
    }

    public String getType()
    {
        return _type;
    }

    public void setType(String type)
    {
        _type = type;
    }

    public LocalTime getTime()
    {
        return _time;
    }
}
