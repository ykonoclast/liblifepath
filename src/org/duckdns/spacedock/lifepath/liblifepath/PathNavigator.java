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

/**
 *
 * @author ykonoclast
 */
public class PathNavigator
{

    /**
     * instance unique du PathNavigator
     */
    private static PathNavigator m_instance = null;//doit être statique pour être appelé par getInstance en contexte statique

    /**
     * pseudo-constructeur public garantissant un et un seul PathNavigator par
     * lancement de l'application
     *
     * @return le PathNavigator actuel si il a été créé, un nouveau sinon
     */
    public static PathNavigator getInstance()//doit être statique pour être appelé hors instanciation d'un objet
    {
	if (m_instance == null)
	{
	    m_instance = new PathNavigator();
	}
	return (m_instance);
    }

    /**
     * véritable constructeur, appelé seulement si le SessionManager n'a pas
     * encore été créé
     */
    private PathNavigator()
    {

    }

}
