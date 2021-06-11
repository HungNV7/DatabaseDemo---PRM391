package hungnv.demo.databasedemo.dtos;

import java.io.Serializable;

public class StudentDto implements Serializable {
    private String id, name;
    private float mark;

    public StudentDto() {}

    public StudentDto(String id, String name, float mark) {
        this.id = id;
        this.name = name;
        this.mark = mark;
    }

    public String toString() {
        return id + "-" + name +"-" + mark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }
}
