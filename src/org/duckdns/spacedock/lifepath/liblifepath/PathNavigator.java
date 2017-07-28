/*
 * Copyright (C) 2017 ykonoclast
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.duckdns.spacedock.lifepath.liblifepath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * classe naviguant dans les divers choix d'un lifepath
 *
 * @author ykonoclast
 */
public class PathNavigator
{

    /**
     * la référence contenant les données du jeu
     */
    private final ChoiceTree m_choiceTree;

    /**
     * la liste des nodes ayant déjà été sélectionnés au cours de ce parcours
     * (hors retour en arrière)
     */
    private final ArrayList<String> m_nodesChoisis = new ArrayList<>();

    /**
     * la liste des mots clés définis au cours de ce parcours (hors retour en
     * arrière)
     */
    private final HashSet<String> m_motsClesDefinis = new HashSet();

    /**
     * liste des choix autorisés dans l'état actuel, on utilise une
     * LinkedHashMap pour avoir à la fois les clés/valeurs (id/lbl) et
     * l'ordonnancement des éléments
     */
    private final LinkedHashMap<String, String> m_choixPossibles = new LinkedHashMap();

    /**
     * l'identifiant technique du node actuel
     */
    private String m_currentId;

    /**
     * constructeur
     */
    public PathNavigator()
    {
	//chargement de la référence
	m_choiceTree = new ChoiceTree();
	//positionnement au début de l'arbre des choix
	m_currentId = "init";
	m_nodesChoisis.add(m_currentId);
    }

    /**
     *
     * @return une map contenant les choix possibles (description courte) en
     * clés et les id techniques (pour soumission ultérieure) en valeurs
     */
    public LifepathChoice getCurrentChoice()
    {
	Node nodeCourant = m_choiceTree.getNode(m_currentId);
	m_choixPossibles.clear();
	//on parcourt la liste des successeurs du node actuel
	for (Object idSucc : nodeCourant.succ)
	{
	    //on récupère les infos du successeur dans l'arbre
	    Node nodeSucc = m_choiceTree.getNode((String) idSucc);

	    if (nodeSucc.interdit.isEmpty() || Collections.disjoint(nodeSucc.interdit, m_motsClesDefinis))
	    {//le node successeur n'est pas interdit
		if (nodeSucc.obligatoire.isEmpty() || !Collections.disjoint(nodeSucc.obligatoire, m_motsClesDefinis))
		{//les requis du node successeurs sont satisfaits (deux ensembles vides sont disjoints par défaut, d'où le OR pour régler ce cas limite
		    m_choixPossibles.put((String) idSucc, nodeSucc.lbl);//on ajoute alors le node successeurs aux choix possibles
		}
	    }
	}
	//on renvoie la liste des choix possibles avec la description du node actuel
	return new LifepathChoice(m_choixPossibles, m_choiceTree.getNode(m_currentId).desc);
    }

    /**
     * permet au code appelant d'exprimer un choix de navigation parmi les
     * options proposées par le node actuel, tout choix impossible est ignoré
     *
     * @param p_id
     * @return la même chose que la méthode getCurrentChoice une fois le choix
     * actuel mis à jour
     */
    public LifepathChoice choose(String p_id)
    {
	if (m_choixPossibles.containsKey(p_id))
	{//le choix effectué est autorisé
	    m_currentId = p_id;//on le sélectionne
	    m_nodesChoisis.add(p_id);//on l'ajoute à l'historique

	    //ajout des mots clés définis par le nouveau node
	    Iterator iterator = m_choiceTree.getNode(p_id).def.iterator();
	    while (iterator.hasNext())
	    {
		m_motsClesDefinis.add((String) iterator.next());
	    }
	}
	//ce return est important : il rend atomique le fait de choisir et la réactualisation de la liste des choix possibles qui est effectuée dans l'autre méthode
	return getCurrentChoice();
    }

    /**
     * revient un coup en arrière dans le chemin parcouru, ignoré si déjà au
     * début
     *
     * @return
     */
    public LifepathChoice rollback()
    {
	if (m_nodesChoisis.size() > 1)
	{//on est après le node initial (irrécupérable si on efface celui-là)

	    //suppression des mots clés définis par le dernier node
	    Iterator iterator = m_choiceTree.getNode(m_nodesChoisis.get(m_nodesChoisis.size() - 1)).def.iterator();
	    while (iterator.hasNext())
	    {
		m_motsClesDefinis.remove((String) iterator.next());
	    }

	    //rollback effectif
	    m_nodesChoisis.remove(m_nodesChoisis.size() - 1);
	    m_currentId = m_nodesChoisis.get(m_nodesChoisis.size() - 1);
	}
	return getCurrentChoice();//important, rend atomique du rollback la réactualisation des choix possibles
    }

    /**
     * petite classe encapsulant les éléments d'un choix : la description du
     * node actuel ainsi que les prochains choix possibles
     */
    public class LifepathChoice
    {

	public final Map choices;
	public final String desc;

	public LifepathChoice(Map p_choices, String p_desc)
	{
	    choices = p_choices;
	    desc = p_desc;
	}
    }
}
