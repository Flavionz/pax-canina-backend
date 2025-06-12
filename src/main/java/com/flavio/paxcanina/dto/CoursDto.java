package com.flavio.paxcanina.dto;

public class CoursDto {
    private Integer idCours;
    private String nom;
    private String description;
    private String statut;
    private String imgUrl;

    public CoursDto() {}

    public CoursDto(Integer idCours, String nom, String description, String statut, String imgUrl) {
        this.idCours = idCours;
        this.nom = nom;
        this.description = description;
        this.statut = statut;
        this.imgUrl = imgUrl;
    }

    // Getter & Setter
    public Integer getIdCours() { return idCours; }
    public void setIdCours(Integer idCours) { this.idCours = idCours; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
}
