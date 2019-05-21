package com.example.demo.entity;


import javax.persistence.*;
import java.util.Set;

@Entity
public class LigneFacture {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Facture facture;

    @ManyToOne
    private Article article;

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Column
    private int quantite;

    public Double getSousTotal(){
        return getArticle().getPrix()*quantite;
    }

}
