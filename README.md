# task-manager

Proiectul va consta intr-o aplicatie client - server pe socket-uri sau cu apel de metode la distanta, implementata intr-un limbaj la alegere (C#, Java, C+/C, etc.), putand fi realizat individual sau in echipa de maxim patru persoane, si va fi prezentat pe calculator in ultima activitate de seminar.

Tema aleasă: 11. Gestiunea obiectelor dintr-o baza de date:

* Server-ul gestioneaza o lista de obiecte preluate dintr-o baza de date, fiecare obiect fiind identificat printr-o cheie unica;
* Client-ul poate interoga server-ul pentru a selecta o lista de obiecte cu anumite valori ale cheii;
* Server-ul tine pentru fiecare lista valorii cheilor pentru obiectele selectate;
* Clientii pot actualiza sau sterge obiecte dupa cheie;
* In momentul in care un obiect este modificat, server-ul va notifica acest lucru tuturor clientilor care selectasera in prealabil obiectul respectiv;
* Schimbarile asupra obiectelor sunt salvate in memoria server-ului si in baza de date.

Punctajul aferent cerintelor este urmatorul:
* Server concurent care permite subscrierea si notificarea clientilor conform cerintelor functionale - 1.5p;
* Client capabil sa subscrie, sa invoce server-ul si sa trateze invocarile inapoi venite de la server - 1.5p.
