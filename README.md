# Ransomware-Java

Progetto realizzato a scopo didattico.
Non mi assumo alcuna responsabilità per l'uso improprio di questo codice. 
Utilizzatelo con responsabilità e rispettando le leggi locali, nazionali e internazionali.

---

#Funzionalita

**Cifratura file e directory**

**Decifrazione file e directory**

---

#Struttura del progetto

-server.java: runnando il programma su una macchina, la metteremo in ascolto su una porta, per ricevere dei dati, in questo caso la chiave di decriptazione dei file

-Crittografia.java: codice che verrà eseguito sulla macchina vittima, criptando i dati, e richiedendo successivamente la chiave per poter decriptare i dati

---

#IMPORTANTE

Si consiglia di non utilizzare il programma sulla propria macchina personale, ma di testarlo su una sandbox o macchina virtuale.

---

#Istruzioni per l'uso

Per utilizzare correttamente il ransomware, è necessario che il server sia in ascolto.
Nel metodo mandaDati presente in Crittografia.java è necessario inserire l'indirizzo IP della macchina che farà da server,
mentre nel mail la stringa "directory" indica il percorso della directory da criptare.

