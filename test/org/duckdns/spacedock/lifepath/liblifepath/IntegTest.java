/*
 * To change this license header, decide License Headers in Project Properties.
 * To change this template file, decide Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.lifepath.liblifepath;

import org.duckdns.spacedock.liblifepath.PathNavigator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ykonoclast
 */
public class IntegTest
{

    @Test
    public void hello()
    {
	PathNavigator navigator = new PathNavigator();
	navigator.getCurrentChoice();

    }
}
