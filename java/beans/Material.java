package beans;

import java.io.Serializable;
import java.util.Objects;

public class Material implements Serializable {
    private Integer id;
    private String name;
    private Integer level;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Objects.equals(id, material.id) && Objects.equals(name, material.name) && Objects.equals(level, material.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, level);
    }
}
