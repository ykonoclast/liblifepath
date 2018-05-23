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
package org.duckdns.spacedock.liblifepath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
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
     * map des nodes : les id sont les clés et les infos du node les valeurs.
     */
    Map<String, Node> m_tabNodes;

    /**
     * constructeur : convertit les JSON en HashMap
     *
     */
    ChoiceTree()
    {
	//chargement des données depuis les fichiers JSON
	List<JsonArray> m_listNodes = new ArrayList<>();

	JsonObject object;

	object = loadJsonFile("liblifepath", "commun.json");
	m_listNodes.add(object.getJsonArray("nodes"));

	object = loadJsonFile("liblifepath", "noble.json");
	m_listNodes.add(object.getJsonArray("nodes"));

	object = loadJsonFile("liblifepath", "alien.json");
	m_listNodes.add(object.getJsonArray("nodes"));

	object = loadJsonFile("liblifepath", "guilde.json");
	m_listNodes.add(object.getJsonArray("nodes"));

	object = loadJsonFile("liblifepath", "pretre.json");
	m_listNodes.add(object.getJsonArray("nodes"));

	//création de la Map, On utilise une HashMap car on souhaite utiliser la structure clés/valeurs de plus l'ordre n'importe pas non plus que des capacités de parcours avancées ou l'accès aléatoire
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
		Set<String> obligatoire = new HashSet();
		JsonArray tabOblig = jsonNode.getJsonArray("obligatoire");
		for (int i = 0; i < tabOblig.size(); ++i)
		{
		    obligatoire.add(tabOblig.getString(i));
		}

		//on charge la liste des mots-clés interdits
		Set<String> interdit = new HashSet();
		JsonArray tabInterdit = jsonNode.getJsonArray("interdit");
		for (int i = 0; i < tabInterdit.size(); ++i)
		{
		    interdit.add(tabInterdit.getString(i));
		}

		//on charge la liste des mots-clés définis
		Set<String> def = new HashSet();
		JsonArray tabDef = jsonNode.getJsonArray("def");
		for (int i = 0; i < tabDef.size(); ++i)
		{
		    def.add(tabDef.getString(i));
		}

		//on charge la liste des successeurs
		Set<String> succ = new HashSet();
		JsonArray tabSucc = jsonNode.getJsonArray("next");
		for (int i = 0; i < tabSucc.size(); ++i)
		{
		    succ.add(tabSucc.getString(i));
		}

		//on ajoute l'ensemble des données chargées à la HashMap
		m_tabNodes.put(id, new Node(lbl, desc, obligatoire, interdit, def, succ));
	    }
	}
    }

    Node getNode(String p_id)
    {
	return m_tabNodes.get(p_id);
    }
}
