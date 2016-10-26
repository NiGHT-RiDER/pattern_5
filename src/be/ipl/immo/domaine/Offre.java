package be.ipl.immo.domaine;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import be.ipl.immo.exceptions.ArgumentInvalideException;
import be.ipl.immo.exceptions.PlusDOffresPossiblesException;
import be.ipl.immo.util.Util;

public class Offre {
    public enum EtatOffre {
        SOUMISE{
            @Override
            public boolean accepterOffre(Offre o) {
                if (o.bien.getEtat() == Bien.EtatBien.COMPROMIS_SIGNE || o.bien.getEtat() == Bien.EtatBien.VENDU)
                    return false;
                if (o.bien.getEtat() == Bien.EtatBien.OPTION) {
                    Iterator<Offre> offres = o.bien.offres();
                    while (offres.hasNext()) {
                        Offre of = offres.next();
                        if (of != o && of.etat == EtatOffre.ACCEPTEE)
                            return false;
                    }
                }
                o.etat = EtatOffre.ACCEPTEE;
                Iterator<Offre> offres = o.bien.offres();
                while (offres.hasNext()) {
                    Offre of = offres.next();
                    if (of != o)
                        of.etat = EtatOffre.REFUSEE;
                }
                try {
                    o.bien.mettreOption(o);
                } catch (ArgumentInvalideException e) {
                    throw new InternalError();
                }
                return true;
            }

            @Override
            public boolean refuserOffre(Offre o) {
                if (o.bien.getEtat() != Bien.EtatBien.INITIAL && o.bien.getEtat() != Bien.EtatBien.OPTION)
                    return false;
                o.etat = EtatOffre.REFUSEE;
                return true;
            }

            @Override
            public boolean annulerOffre(Offre o) {
                if (o.bien.getEtat() == Bien.EtatBien.VENDU)
                    return false;
                o.etat = EtatOffre.ANNULEE;
                return true;

            }
        },

        ACCEPTEE{
            @Override
            public boolean refuserOffre(Offre o) {
                if (o.bien.getEtat() != Bien.EtatBien.INITIAL && o.bien.getEtat() != Bien.EtatBien.OPTION)
                    return false;
                o.etat = EtatOffre.REFUSEE;
                if (o.bien.getEtat() == Bien.EtatBien.OPTION)
                    o.bien.refuser();
                return true;
            }

            @Override
            public boolean annulerOffre(Offre o) {
                if (o.bien.getEtat() == Bien.EtatBien.VENDU)
                    return false;
                o.etat = EtatOffre.ANNULEE;
                if (o.bien.getEtat() != Bien.EtatBien.INITIAL)
                    o.bien.annuler();
                return true;

            }
        },

        REFUSEE{

        },

        ANNULEE{

        };

        public boolean accepterOffre(Offre o){
            return false;
        }

        public boolean refuserOffre(Offre o){
            return false;
        }

        public boolean annulerOffre(Offre o){
            return false;
        }
    }

    // - accepte l'offre pour autant
    // * que l'offre n'est ni annul�e, ni refus�e, ni d�j� accept�e
    // * qu'une autre offre n'ait pas �t� accept�e
    // - place une option sur le bien
    // - refuse les autres offres de ce bien
    public boolean accepterOffre() {
        return etat.accepterOffre(this);
    }

    // Cette m�thode refuse une offre. Si celle-ci �tait accept�e, le bien sera
    // r�initialis�. Ceci n'est possible que si le compromis n'a pas �t� sign�
    // ni le bien vendu.
    // On ne peut pas refuser une offre annul�e (ou d�j� refus�e)
    public boolean refuserOffre() {
        return etat.refuserOffre(this);
    }

    // Cette m�thode annule une offre. Si elle �tait accept�e, le bien sera
    // r�initialis�. Ceci n'est pas possible si le bien est vendu.
    // On ne peut pas annuler une offre refus�e (ou d�j� annul�e)
    public boolean annulerOffre() {
        return etat.annulerOffre(this);
    }

    private int id;
    private EtatOffre etat;
    private Calendar dateLimite;
    private double montant;
    private String nomAcheteur;

    private final Bien bien;

    public Offre(Calendar dateLimite, double montant, String nomAcheteur,
                 Bien bien) throws ArgumentInvalideException, PlusDOffresPossiblesException {
        Util.checkObject(dateLimite);
        if (dateLimite.before(new GregorianCalendar()))
            throw new ArgumentInvalideException();
        Util.checkPositive(montant);
        Util.checkString(nomAcheteur);
        Util.checkObject(bien);
        this.dateLimite = (Calendar) dateLimite.clone();
        this.montant = montant;
        this.nomAcheteur = nomAcheteur;
        this.bien = bien;
        try {
            this.bien.ajouterOffre(this);
        } catch (ArgumentInvalideException e) {
            throw new InternalError();
        }
        this.etat = EtatOffre.SOUMISE;
    }

    public int getId() {
        return id;
    }

    public EtatOffre getEtat() {
        return etat;
    }

    public Calendar getDateLimite() {
        return (Calendar) dateLimite.clone();
    }

    public double getMontant() {
        return montant;
    }

    public String getNomAcheteur() {
        return nomAcheteur;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Offre other = (Offre) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public Bien getBien() {
        return bien;
    }

}
