package com.monographic.subject.ticTacToe.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Board")
public class BoardBE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board")
    List<FieldBE> fields = new ArrayList<>();

    public BoardBE() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public List<FieldBE> getFields() {
        return fields;
    }

    public void setFields(List<FieldBE> fields) {
        this.fields = fields;
        fields.stream().forEach(field -> field.setBoard(this));
    }

}
