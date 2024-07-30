import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GestionNoteEcole {

    // Classe interne pour représenter une classe d'étudiants
    public static class Classe {
        private String nom;
        private List<Etudiant> etudiants;
        private List<Matiere> matieres;

        public Classe(String nom) {
            this.nom = nom;
            this.etudiants = new ArrayList<>();
            this.matieres = new ArrayList<>();
        }

        public String getNom() {
            return nom;
        }

        public List<Etudiant> getEtudiants() {
            return etudiants;
        }

        public List<Matiere> getMatieres() {
            return matieres;
        }

        public void ajouterEtudiant(Etudiant etudiant) {
            etudiants.add(etudiant);
        }

        public void ajouterMatiere(Matiere matiere) {
            matieres.add(matiere);
        }
    }

    // Classe interne pour représenter un étudiant
    public static class Etudiant {
        private String nom;
        private Map<Matiere, List<Double>> notes;

        public Etudiant(String nom) {
            this.nom = nom;
            this.notes = new HashMap<>();
        }

        public String getNom() {
            return nom;
        }

        public Map<Matiere, List<Double>> getNotes() {
            return notes;
        }

        public void ajouterNote(Matiere matiere, double note) {
            notes.computeIfAbsent(matiere, k -> new ArrayList<>()).add(note);
        }

        public double calculerMoyenne(Matiere matiere) {
            List<Double> notesMatiere = notes.get(matiere);
            if (notesMatiere == null || notesMatiere.isEmpty()) return 0;
            return notesMatiere.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        }

        public double calculerMoyenneGenerale() {
            return notes.values().stream()
                    .flatMap(List::stream)
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0);
        }
    }

    // Classe interne pour représenter une matière
    public static class Matiere {
        private String nom;

        public Matiere(String nom) {
            this.nom = nom;
        }

        public String getNom() {
            return nom;
        }
    }

    // Classe interne pour représenter une note
    public static class Note {
        private Etudiant etudiant;
        private Matiere matiere;
        private double valeur;

        public Note(Etudiant etudiant, Matiere matiere, double valeur) {
            this.etudiant = etudiant;
            this.matiere = matiere;
            this.valeur = valeur;
        }

        public Etudiant getEtudiant() {
            return etudiant;
        }

        public Matiere getMatiere() {
            return matiere;
        }

        public double getValeur() {
            return valeur;
        }
    }

    // Classe interne pour la gestion de l'école
    public static class GestionEcole {
        private List<Classe> classes;

        public GestionEcole() {
            this.classes = new ArrayList<>();
        }

        public void ajouterClasse(Classe classe) {
            classes.add(classe);
        }

        public List<Classe> getClasses() {
            return classes;
        }

        public Classe trouverClasse(String nomClasse) {
            return classes.stream()
                    .filter(c -> c.getNom().equals(nomClasse))
                    .findFirst()
                    .orElse(null);
        }

        public void enregistrerNote(String nomClasse, String nomMatiere, String nomEtudiant, double note) {
            Classe classe = trouverClasse(nomClasse);
            if (classe != null) {
                Matiere matiere = classe.getMatieres().stream()
                        .filter(m -> m.getNom().equals(nomMatiere))
                        .findFirst()
                        .orElse(null);
                Etudiant etudiant = classe.getEtudiants().stream()
                        .filter(e -> e.getNom().equals(nomEtudiant))
                        .findFirst()
                        .orElse(null);
                if (matiere != null && etudiant != null) {
                    etudiant.ajouterNote(matiere, note);
                }
            }
        }
    }

    private GestionEcole gestionEcole;
    private Scanner scanner;

    public GestionNoteEcole() {
        this.gestionEcole = new GestionEcole();
        this.scanner = new Scanner(System.in);
    }

    public void afficherMenu() {
        System.out.println("1. Ajouter une nouvelle classe");
        System.out.println("2. Ajouter un nouvel étudiant à une classe existante");
        System.out.println("3. Ajouter une nouvelle matière enseignée à l'école");
        System.out.println("4. Enregistrer les notes");
        System.out.println("5. Calculer les moyennes");
        System.out.println("6. Établir le classement");
        System.out.println("7. Quitter");
    }

    public void demarrer() {
        boolean quitter = false;
        while (!quitter) {
            afficherMenu();
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer le saut de ligne

            switch (choix) {
                case 1:
                    ajouterClasse();
                    break;
                case 2:
                    ajouterEtudiant();
                    break;
                case 3:
                    ajouterMatiere();
                    break;
                case 4:
                    enregistrerNotes();
                    break;
                case 5:
                    calculerMoyennes();
                    break;
                case 6:
                    etablirClassement();
                    break;
                case 7:
                    quitter = true;
                    break;
                default:
                    System.out.println("Choix non valide. Veuillez réessayer.");
            }
        }
    }

    private void ajouterClasse() {
        System.out.println("Nom de la classe : ");
        String nomClasse = scanner.nextLine();
        Classe classe = new Classe(nomClasse);
        gestionEcole.ajouterClasse(classe);
        System.out.println("Classe ajoutée avec succès.");
    }

    private void ajouterEtudiant() {
        System.out.println("Nom de la classe : ");
        String nomClasse = scanner.nextLine();
        Classe classe = gestionEcole.trouverClasse(nomClasse);
        if (classe != null) {
            System.out.println("Nom de l'étudiant : ");
            String nomEtudiant = scanner.nextLine();
            Etudiant etudiant = new Etudiant(nomEtudiant);
            classe.ajouterEtudiant(etudiant);
            System.out.println("Étudiant ajouté avec succès.");
        } else {
            System.out.println("Classe non trouvée.");
        }
    }

    private void ajouterMatiere() {
        System.out.println("Nom de la classe : ");
        String nomClasse = scanner.nextLine();
        Classe classe = gestionEcole.trouverClasse(nomClasse);
        if (classe != null) {
            System.out.println("Nom de la matière : ");
            String nomMatiere = scanner.nextLine();
            Matiere matiere = new Matiere(nomMatiere);
            classe.ajouterMatiere(matiere);
            System.out.println("Matière ajoutée avec succès.");
        } else {
            System.out.println("Classe non trouvée.");
        }
    }

    private void enregistrerNotes() {
        System.out.println("Nom de la classe : ");
        String nomClasse = scanner.nextLine();
        System.out.println("Nom de la matière : ");
        String nomMatiere = scanner.nextLine();
        Classe classe = gestionEcole.trouverClasse(nomClasse);
        if (classe != null) {
            Matiere matiere = classe.getMatieres().stream()
                    .filter(m -> m.getNom().equals(nomMatiere))
                    .findFirst()
                    .orElse(null);
            if (matiere != null) {
                for (Etudiant etudiant : classe.getEtudiants()) {
                    System.out.println("Note pour " + etudiant.getNom() + " : ");
                    double note = scanner.nextDouble();
                    scanner.nextLine(); // Consommer le saut de ligne
                    etudiant.ajouterNote(matiere, note);
                }
                System.out.println("Notes enregistrées avec succès.");
            } else {
                System.out.println("Matière non trouvée.");
            }
        } else {
            System.out.println("Classe non trouvée.");
        }
    }

    private void calculerMoyennes() {
        System.out.println("Nom de la classe : ");
        String nomClasse = scanner.nextLine();
        Classe classe = gestionEcole.trouverClasse(nomClasse);
        if (classe != null) {
            for (Etudiant etudiant : classe.getEtudiants()) {
                System.out.println("Moyenne de " + etudiant.getNom() + " : " + etudiant.calculerMoyenneGenerale());
            }
        } else {
            System.out.println("Classe non trouvée.");
        }
    }

    private void etablirClassement() {
        System.out.println("Nom de la classe : ");
        String nomClasse = scanner.nextLine();
        Classe classe = gestionEcole.trouverClasse(nomClasse);
        if (classe != null) {
            System.out.println("Nom de la matière : ");
            String nomMatiere = scanner.nextLine();
            Matiere matiere = classe.getMatieres().stream()
                    .filter(m -> m.getNom().equals(nomMatiere))
                    .findFirst()
                    .orElse(null);
            if (matiere != null) {
                classe.getEtudiants().stream()
                        .sorted((e1, e2) -> Double.compare(e2.calculerMoyenne(matiere), e1.calculerMoyenne(matiere)))
                        .forEach(etudiant -> System.out.println(etudiant.getNom() + " : " + etudiant.calculerMoyenne(matiere)));
            } else {
                System.out.println("Matière non trouvée.");
            }
        } else {
            System.out.println("Classe non trouvée.");
        }
    }

    public static void main(String[] args) {
        GestionNoteEcole gestionNoteEcole = new GestionNoteEcole();
        gestionNoteEcole.demarrer();
    }
}
