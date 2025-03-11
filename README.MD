Stratégie du mode Shifumi
Dans cette version du jeu, le joueur et l'ordinateur ne choisissent plus leur arme totalement au hasard. Une stratégie simple est mise en place :

Pour le joueur humain :
Le premier coup est toujours « Pierre ».
Ensuite, l'arme choisie dépend du coup précédent. On sélectionne l'arme qui aurait battu celle de l'adversaire au tour précédent.

Pour l'ordinateur :
Il essaie généralement de contrer l'arme que le joueur vient de choisir (dans 70% des cas).
Dans 30 % des cas restants, l'ordinateur fait un choix moins bon exprès pour laisser une chance au joueur humain de gagner.
Cette approche permet de rendre le jeu moins prévisible et plus agréable à jouer.
Le code lié à cette stratégie est entièrement isolé du reste du projet, respectant ainsi une bonne architecture logicielle.