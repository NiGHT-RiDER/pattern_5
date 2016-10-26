package be.ipl.immo.domaine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import be.ipl.immo.domaine.Offre.EtatOffre;
import be.ipl.immo.exceptions.ArgumentInvalideException;
import be.ipl.immo.exceptions.PlusDOffresPossiblesException;
import be.ipl.immo.util.Util;

public class Bien {
    public enum EtatBien {
        INITIAL{
            @Override
            public boolean mettreOption(Bien b, Offre o) throws ArgumentInvalideException {
                if (!b.contientOffre(o))
                    return false;
                if (o.getEtat() != EtatOffre.ACCEPTEE && o.getEtat() != EtatOffre.SOUMISE)
                    return false;
                b.etat = EtatBien.OPTION;
                o.accepterOffre();
                return true;
            }

            // TODO refuser
            @Override
            public boolean refuser(Bien b) {
                b.etat = EtatBien.INITIAL;
                for (Offre offre: b.offres) {
                    if (offre.getEtat() == EtatOffre.ACCEPTEE)
                        offre.refuserOffre();
                }
                return true;
            }

        }, OPTION{
            // TODO refuser
            @Override
            public boolean refuser(Bien b) {
                b.etat = EtatBien.INITIAL;
                for (Offre offre: b.offres) {
                    if (offre.getEtat() == EtatOffre.ACCEPTEE)
                        offre.refuserOffre();
                }
                return true;
            }

        },
        COMPROMIS_SIGNE{

        },
        VENDU{

        };
        // rentre bien getetat
        // offre getetat private

        public boolean refuser(Bien b){return false;}
        public boolean annuler(Bien b){return false;}
        public boolean signerCompromis(Bien b,Offre o){return false;}
        public boolean mettreOption(Bien b,Offre o) throws ArgumentInvalideException {return false;}
        public boolean vendre(Bien b,Offre o){return false;}

    }

    public enum TypeBien {
        MAISON, APPARTEMENT;
    }

    // Place l'offre comme option sur le bien
    // Ceci ne peut se faire que si le bien est � l'�tat initial
    // pour une de ses offres qui n'est ni annul�e ni refus�e
    // La m�thode appelle la m�thode compagne accepterOffre de Offre
    public boolean mettreOption(Offre offre) throws ArgumentInvalideException {
        return etat.mettreOption(this,offre);
    }

    // R�initialise l'�tat du bien en refusant (�ventuellement) une offre accept�e pr�c�demment
    // Ne peut pas se faire si le compromis est sign� ou le bien vendu
    // Si une offre est accept�e, on la refuse via refuserOffre d'Offre
    public boolean refuser() {
        if (this.etat == EtatBien.COMPROMIS_SIGNE || this.etat == EtatBien.VENDU)
            return false;
        this.etat = EtatBien.INITIAL;
        for (Offre offre: offres) {
            if (offre.getEtat() == EtatOffre.ACCEPTEE)
                offre.refuserOffre();
        }
        return true;
    }

    // R�initialise l'�tat du bien en annulantt (�ventuellement) une offre accept�e pr�c�demment
    // Ne peut pas se faire si le bien est vendu
    // Si une offre est accept�e, on l'annule via annulerOffre d'Offre
    public boolean annuler() {
        if (this.etat == EtatBien.VENDU)
            return false;
        this.etat = EtatBien.INITIAL;
        for (Offre offre: offres) {
            if (offre.getEtat() == EtatOffre.ACCEPTEE)
                offre.annulerOffre();
        }
        return true;
    }

    // Signe le compromis pour l'offre pass�e
    // Une option doit avoir �t� plac�e sur ce bien au pr�alable
    // L'offre pass�e doit �tre une offre accept�e pour ce bien
    public boolean signerCompromis(Offre offre) throws ArgumentInvalideException {
        if (!this.contientOffre(offre))
            return false;
        if (this.etat != EtatBien.OPTION)
            return false;
        if (offre.getEtat() != EtatOffre.ACCEPTEE)
            return false;
        this.etat = EtatBien.COMPROMIS_SIGNE;
        return true;
    }

    // Vend le bien � l'acheteur ayant fait l'offre pass�e
    // Le compromis doit avoir �t� sign�
    // L'offre pass�e doit �tre une offre accept�e pour ce bien
    public boolean vendre(Offre offre) throws ArgumentInvalideException {
        if (!this.contientOffre(offre))
            return false;
        if (this.etat != EtatBien.COMPROMIS_SIGNE)
            return false;
        if (offre.getEtat() != EtatOffre.ACCEPTEE)
            return false;
        this.etat = EtatBien.VENDU;
        return true;
    }

    private String reference;
    private EtatBien etat;
    private String description;
    private TypeBien type;
    private double prix;
    private String nomVendeur;

    // pour l'association bidirectionnelle avec Offre
    private List<Offre> offres = new ArrayList<Offre>();

    public Bien(String reference, TypeBien type, double prix, String nomVendeur)
            throws ArgumentInvalideException {
        Util.checkString(reference);
        Util.checkObject(type);
        Util.checkPositive(prix);
        Util.checkString(nomVendeur);
        this.reference = reference;
        this.type = type;
        this.prix = prix;
        this.nomVendeur = nomVendeur;
        this.etat = EtatBien.INITIAL;
    }

    public String getReference() {
        return reference;
    }

    public EtatBien getEtat() {
        return etat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeBien getType() {
        return type;
    }

    public double getPrix() {
        return prix;
    }

    public String getNomVendeur() {
        return nomVendeur;
    }

    // TODO g�rer l'association bidirectionnelle avec Offre

    // Cette m�thode est destin�e � n'�tre applel�e que par le constructeur
    // d'Offre. Si ce n'est pas le cas, elle renvoie false
    public boolean ajouterOffre(Offre offre) throws ArgumentInvalideException, PlusDOffresPossiblesException {
        if (contientOffre(offre)) // v�rifie d�j� si offre == null
            return false;
        if (this.etat != EtatBien.INITIAL)
            throw new PlusDOffresPossiblesException();
        if (offre.getBien() != this)
            return false;
        offres.add(offre);
        return true;
    }

    public boolean contientOffre(Offre offre) throws ArgumentInvalideException {
        Util.checkObject(offre);
        return offres.contains(offre);
    }

    public Iterator<Offre> offres() {
        return Collections.unmodifiableList(offres).iterator();
    }

    public int nombreDOffres() {
        return offres.size();
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bien other = (Bien) obj;
        return reference.equals(other.reference);
    }

}
