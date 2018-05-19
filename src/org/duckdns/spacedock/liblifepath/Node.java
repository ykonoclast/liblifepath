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

import java.util.Set;

/**
 * petite classe interne pour représenter les différents choix possibles et ce
 * qu'ils impliquent, on utilise des champs finaux en accès libre, ils seront
 * non-modifiables mais aisément accéssibles
 *
 * @author ykonoclast
 */
class Node
{

    /**
     * texte qui sera affiché sur le bouton de choix, description courte
     */
    final String lbl;
    /**
     * texte qui sera affiché si le choix est validé, description longue
     */
    final String desc;
    /**
     * les mot-clés obligatoires pour que ce choix soit sélectionnable, on
     * utilise un set pour cette valeur et les autres car l'on se moque de
     * l'ordre, on ne veut pas de doublon et l'accès aléatoire n'est pas
     * nécessaire
     */
    final Set obligatoire;
    /**
     * les mot-clés interdits pour que ce choix soit sélectionnable
     */
    final Set interdit;
    /**
     * les mot-clés que ce choix va définir
     */
    final Set def;
    /**
     * les successeurs de ce node
     */
    final Set succ;

    Node(String p_lbl, String p_desc, Set p_obligatoire, Set p_interdit, Set p_def, Set p_succ)
    {
	lbl = p_lbl;
	desc = p_desc;
	obligatoire = p_obligatoire;
	interdit = p_interdit;
	def = p_def;
	succ = p_succ;
    }
}
