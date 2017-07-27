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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import javax.json.JsonArray;
import javax.json.JsonObject;
import static org.duckdns.spacedock.commonutils.JSONHandler.loadJsonFile;

/**
 * classe représentant l'arbre des choix possibles
 *
 * @author ykonoclast
 */
class ChoiceTree
{

    /**
     * map des nodes : les id sont les clés et les infos du node les valeurs. On
     * utilise une HashMap car on souhaite utiliser la structure clés/valeurs de
     * plus l'ordre n'importe pas non plus que des capacités de parcours
     * avancées ou l'accès aléatoire
     */
    HashMap m_tabNodes;

    /**
     * constructeur : convertit les JSON en HashMap
     *
     */
    ChoiceTree()
    {
	//chargement des données depuis les fichiers JSON
	JsonObject object;

	ArrayList<JsonArray> m_listNodes = new ArrayList<>();

	object = loadJsonFile("liblifepath", "commun.json");
	m_listNodes.add(object.getJsonArray("nodes"));

	object = loadJsonFile("liblifepath", "nobles.json");
	m_listNodes.add(object.getJsonArray("nodes"));

	object = loadJsonFile("liblifepath", "aliens.json");
	m_listNodes.add(object.getJsonArray("nodes"));

	//création de la HashMap
	m_tabNodes = new HashMap();

	//on parcourt chaque tableau de nodes issu de chaque fichier
	for (JsonArray array : m_listNodes)
	{
	    ListIterator iterator = array.listIterator();
	    //pour chaque fichier, on parcourt chaque node
	    while (iterator.hasNext())
	    {
		JsonObject jsonNode = (JsonObject) iterator.next();

		//on charge les données de base
		String id = jsonNode.getString("id");
		String lbl = jsonNode.getString("lbl");
		String desc = jsonNode.getString("desc");

		//on charge la liste des mots-clés obligatoires
		HashSet obligatoire = new HashSet();
		Iterator obligIterator = jsonNode.getJsonArray("obligatoire").iterator();
		while (obligIterator.hasNext())
		{
		    obligatoire.add(obligIterator.next());
		}

		//on charge la liste des mots-clés interdits
		HashSet interdit = new HashSet();
		Iterator interdIterator = jsonNode.getJsonArray("interdit").iterator();
		while (interdIterator.hasNext())
		{
		    interdit.add(interdIterator.next());
		}

		//on charge la liste des mots-clés définis
		HashSet def = new HashSet();
		Iterator defIterator = jsonNode.getJsonArray("def").iterator();
		while (defIterator.hasNext())
		{
		    def.add(defIterator.next());
		}

		//on charge la liste des successeurs
		HashSet succ = new HashSet();
		Iterator succIterator = jsonNode.getJsonArray("next").iterator();
		while (succIterator.hasNext())
		{
		    def.add(succIterator.next());
		}

		//on ajoute l'ensemble des données chargées à la HashMap
		m_tabNodes.put(id, new Node(lbl, desc, obligatoire, interdit, def, succ));
	    }
	}
    }

    Node getNode(String p_id)
    {
	return (Node) m_tabNodes.get((Object) p_id);
    }
}
