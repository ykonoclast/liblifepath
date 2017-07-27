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
    private ChoiceTree m_choiceTree;

    /**
     * la liste des nodes ayant déjà été sélectionnés au cours de ce parcours
     * (hors retour en arrière)
     */
    private ArrayList<Node> m_nodesChoisis;

    /**
     * la liste des mots clés définis au cours de ce parcours (hors retour en
     * arrière)
     */
    private HashSet m_motsClesDefinis;

    /**
     * liste des choix autorisés dans l'état actuel, on utilise une
     * LinkedHashMap pour avoir à la fois les clés/valeurs (id/lbl) et
     * l'ordonnancement des éléments
     */
    private LinkedHashMap m_choixPossibles;

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
    }

    /**
     *
     * @return une map contenant les choix possibles (description courte) en
     * clés et les id techniques (pour soumission ultérieure) en valeurs
     */
    public Choice getCurrentChoice()
    {
	Node nodeCourant = m_choiceTree.getNode(m_currentId);

	//on parcourt la liste des successeurs du node actuels
	for (Object idSucc : nodeCourant.succ)
	{
	    //on récupère les infos du successeur dans l'arbre
	    Node nodeSucc = m_choiceTree.getNode((String) idSucc);

	    if (Collections.disjoint(nodeSucc.interdit, m_motsClesDefinis))
	    {//le node successeur n'est pas interdit
		if (!Collections.disjoint(nodeSucc.obligatoire, m_motsClesDefinis))
		{//les requis du node successeurs sont satisfaits
		    m_choixPossibles.put(idSucc, nodeSucc.lbl);//on ajoute alors le node successeurs aux choix possibles
		}
	    }
	}
	//on renvoie la liste des choix possibles avec la description du node actuel
	return new Choice(m_choixPossibles, m_choiceTree.getNode(m_currentId).desc);
    }

    /**
     * permet au code appelant d'exprimer un choix de navigation parmi les
     * options proposées par le node actuel, tout choix impossible est ignoré
     *
     * @param p_id
     * @return la même chose que la méthode getCurrentChoice une fois le choix
     * actuel mis à jour
     */
    public Choice choose(String p_id)
    {
	if (m_choixPossibles.containsKey(p_id))
	{//le choix effectué est autorisé
	    m_currentId = p_id;
	}
	return getCurrentChoice();
    }

    /**
     * petite classe encapsulant les éléments d'un choix : la description du
     * node actuel ainsi que les prochains choix possibles
     */
    public class Choice
    {

	public final Map choices;
	public final String desc;

	public Choice(Map p_choices, String p_desc)
	{
	    choices = p_choices;
	    desc = p_desc;
	}
    }
}
