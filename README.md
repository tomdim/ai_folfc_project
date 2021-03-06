# First Order Logic with Forward Chaining - Artificial Intelligence project

## Περιγραφή της διαδικασίας εξαγωγής συμπερασμάτων

Η μέθοδος στον πηγαίο κώδικα που κάνει την παραπάνω δουλειά είναι η:
>public boolean fol_fc_ask(KnowledgeBase KB, AtomicSentence a)<br />
{<br />
  ...<br />
}<br />

Σε αυτή την μέθοδο εξαγωγής συμπερασμάτων, διατηρούμε πολλά στοιχεία που μας βοηθάνε στην εκτέλεση της διαδικασίας.
<br />
<br />
**toProve**: Sentence που περιέχει την AtomicSentence του ask, δηλαδή την ατομική πρόταση που ζητάει ο χρήστης να αποδειχθεί αν ισχύει.<br />
**proofs**: ArrayList που περιέχει αρχικά τα γεγονότα (facts) της ΒΓ και στη συνέχεια προστίθενται ό,τι αποδεικνύουμε από την agenda μας.<br />
**agenda**: ArrayList που περιέχει τις προτάσεις της ΒΓ, εκτός της πρότασης που θέλουμε να αποδείξουμε και των προτάσεων-γεγονότα, δηλαδή περιέχει τις ενδιάμεσες προτάσεις προκειμένου να αποδείξουμε την πρόταση που επιθυμεί ο χρήστης.<br />
**vars**: οι μεταβλητές της ΒΓ, π.χ. x, y, z, ...<br />
**constants**: οι σταθερές της ΒΓ, π.χ. West, Nono, M1, ...<br />
**new_variables**: LinkedHashMap<String, String> που περιέχει τις νέες μεταβλητές, κατά τη διάρκεια της ανάλυσης της ΒΓ, μαζί με τις τιμές τους. Για παράδειγμα, (x1, M1), (x2, Nono), (x3, M1), κ.τ.λ.<br />

>Αφού έχουμε όλα αυτά, μέχρι η agenda να “αδειάσει”:
>>Παίρνουμε μία-μία τις προτάσεις που θέλουμε να αποδείξουμε και αλλάζουμε τις
μεταβλητές με νέες, μέσω της μεθόδου new_vars(). 

Πιο συγκεκριμένα:

>>>(Missile(x)) => Weapon(x) μετά την new_vars γίνεται (Missile(x3)) => Weapon(x3)<br />
Κατά την εκτέλεση της new_vars() προσθέτουμε στο new_variables<String, String> τις νέες μεταβλητές μαζί με τις τιμές τους.

Ο τρόπος με τον οποίον είναι δομημένη η Βάση Γνώσης

>>Knowledge Base > Sentence > Atomic Sentence > Parameters

Μετά, παίρνουμε τις ατομικές προτάσεις από τις οποίες αποτελείται η κάθε πρόταση τηςagenda και ελέγχουμε αν έχουν αποδειχθεί. 

Αν έχουν αποδειχθεί όλες τότε προσθέτουμε την ατομική πρόταση μετά το σύμβολο “=>” (head) στην proofs list.

Όταν τελειώσει η επεξεργασία της agenda ελέγχουμε αν αυτά που έχουμε πλέον στην proofs list επαρκούν έτσι ώστε να αποδειχθεί η πρόταση που περιέχει το ask του χρήστη.

Επίσης, ελέγχουμε και αν οι τιμές των μεταβλητών είναι σωστές!

## Παράδειγμα Χρήσης
>Knowledge Base
>>![alt tag](https://github.com/tomdim/ai_folfc_project/blob/master/example1.jpg)

>Console Input & Output
>>![alt tag](https://github.com/tomdim/ai_folfc_project/blob/master/example2.jpg)
>>![alt tag](https://github.com/tomdim/ai_folfc_project/blob/master/example3.jpg)
>>![alt tag](https://github.com/tomdim/ai_folfc_project/blob/master/example4.jpg)

