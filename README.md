# INF138 Project Template

Ένα απλό πρότυπο οργάνωσης του κώδικα και της τεχνικής τεκμηρίωσης για τις εξαμηνιαίες εργασίες του μαθήματος Τεχνολογία Λογισμικού ([INF138](https://eclass.aueb.gr/courses/INF138/)) του Τμήματος Πληροφορικής Οικονομικού Πανεπιστημίου Αθηνών.

Η τρέχουσα έκδοση περιλαμβάνει την [προδιαγραφή των απαιτήσεων λογισμικού](docs/markdown/software-requirements.md) με προσαρμογή του `IEEE Std 830-1998` για την ενσωμάτωση απαιτήσεων σε μορφή περιπτώσεων χρήσης. Για περισσότερες λεπτομέρειες μπορείτε να ανατρέξετε στο βιβλίο [Μ Γιακουμάκης, Ν. Διαμαντίδης, Τεχνολογία Λογισμικού, Σταμούλης, 2021](https://www.softeng.gr).

## Μετατροπή Umlet διαγραμμάτων

Η μετατροπή σε εικόνα των Umlet διαγραμμάτων που τοποθετούνται στο φάκελο docs/uml γίνεται εκτελώντας τις εντολές:


```bash
# Περιβάλλον linux
cd docs
./mvnw umlet:convert
```

```bash
# Περιβάλλον windows
cd docs
mvnw umlet:convert
```

Προϋπόθεση είναι η αρχικοποίηση της μεταβλητή περιβάλλοντος JAVA_HOME με την τοποθεσία του Java JDK.

test commit pls work